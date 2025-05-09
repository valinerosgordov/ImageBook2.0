package ru.imagebook.client.app.ctl.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import ru.imagebook.client.app.ctl.process.ProcessPlace;
import ru.imagebook.client.app.service.DeliveryRemoteServiceAsync;
import ru.imagebook.client.app.service.OrderRemoteServiceAsync;
import ru.imagebook.client.app.service.PostamateUnavailableException;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.common.AddressValidator;
import ru.imagebook.client.app.view.payment.DeliveryConstants;
import ru.imagebook.client.app.view.payment.DeliveryConstantsWithLookup;
import ru.imagebook.client.app.view.payment.DeliveryView;
import ru.imagebook.client.app.view.payment.MajorCitySuggestOracle;
import ru.imagebook.client.app.view.personal.PersonalConstants;
import ru.imagebook.client.common.service.BannerRemoteServiceAsync;
import ru.imagebook.client.common.service.BillCalculator;
import ru.imagebook.client.common.service.PostHouseCalc;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.common.service.delivery.DeliveryDiscountServiceAsync;
import ru.imagebook.client.common.service.delivery.PostHouseType;
import ru.imagebook.client.common.service.delivery.SDEKDeliveryType;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DeliveryDiscount;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.DeliveryTypeInfo;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.app.MajorData;
import ru.imagebook.shared.model.app.PickPointData;
import ru.imagebook.shared.model.app.SDEKCourierData;
import ru.imagebook.shared.model.app.SDEKPackage;
import ru.imagebook.shared.model.app.SDEKPickupData;
import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.gwt.client.rpc.VoidAsyncCallback;


public class DeliveryActivity extends AbstractActivity implements DeliveryPresenter {
    // use Map.Entry as alternative Pair class
    private static final List<Map.Entry<Integer, PostHouseType>> BILL_DELIVERY_TYPES_SHOW = Arrays.asList(
        Maps.immutableEntry(DeliveryType.TRIAL, (PostHouseType) null),
        Maps.immutableEntry(DeliveryType.POST, PostHouseType.NORMAL),
        Maps.immutableEntry(DeliveryType.POST, PostHouseType.FIRST_CLASS),
        Maps.immutableEntry(DeliveryType.MAJOR, (PostHouseType) null),
        //Maps.immutableEntry(DeliveryType.POSTAMATE, (PostHouseType) null),
        Maps.immutableEntry(DeliveryType.EXW, (PostHouseType) null),
        Maps.immutableEntry(DeliveryType.SDEK, (PostHouseType) null)
    );

    private final DeliveryView view;
    private final UserService userService;
    private final OrderRemoteServiceAsync orderService;
    private final DeliveryRemoteServiceAsync deliveryService;
    private final DeliveryDiscountServiceAsync deliveryDiscountService;
    private final DeliveryConstants deliveryConstants;
    private final DeliveryConstantsWithLookup constantsWithLookup;
    private final CommonConstants appConstants;
    private final PersonalConstants personalConstants;
    private final PlaceController placeController;
    private final BannerRemoteServiceAsync bannerService;

    private Order<?> order;
    private Bill bill;
    private Integer orderId;
    private User user;
    private Address selectedAddress;
    private DeliveryTypeInfo currentDeliveryTypeInfo;
    private String sdekCity;
    private Map<Integer,DeliveryDiscount> discountsMap;

    @AssistedInject
    public DeliveryActivity(DeliveryView view, UserService userService, OrderRemoteServiceAsync orderService,
                            DeliveryRemoteServiceAsync deliveryService, DeliveryConstants deliveryConstants,
                            DeliveryConstantsWithLookup constantsWithLookup,
                            CommonConstants appConstants, PersonalConstants personalConstants,
                            BannerRemoteServiceAsync bannerService, PlaceController placeController,
                            DeliveryDiscountServiceAsync deliveryDiscountService,
                            @Assisted Order<?> order, @Assisted Bill bill) {
        this.view = view;
        view.setPresenter(this);
        this.userService = userService;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
        this.deliveryDiscountService = deliveryDiscountService;
        this.bannerService = bannerService;
        this.deliveryConstants = deliveryConstants;
        this.constantsWithLookup = constantsWithLookup;
        this.appConstants = appConstants;
        this.personalConstants = personalConstants;
        this.placeController = placeController;

        this.order = order;
        this.bill = bill;
        this.user = userService.getUser();
        this.orderId = (order != null) ? order.getId() : null;
        this.selectedAddress = null;
    }

    @Override
    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        Preconditions.checkNotNull(userService.getUser(), "User must be defined for DeliveryFrom");
        Preconditions.checkState((bill != null || order != null), "Bill or orderId must be defined");

        int billCost = bill != null ? BillCalculator.computeBaseCost(bill) : 0;
        deliveryDiscountService.getDiscounts(billCost, new AsyncCallback<Map<Integer, DeliveryDiscount>>() {
            @Override
            public void onSuccess(Map<Integer, DeliveryDiscount> deliveryDiscountsMap) {
                discountsMap = deliveryDiscountsMap;
                panel.setWidget(view);

                boolean isTrialOrder = order != null && bill == null;
                if (isTrialOrder) {
                    fixTrialOrder();
                } else {
                    showDeliveryMethods();
                }
            }
        });
    }

    private void fixTrialOrder() {
        orderService.fixTrialOrder(order, new AsyncCallback<Order<?>>() {
            @Override
            public void onSuccess(Order<?> fixedTrialOrder) {
                order = fixedTrialOrder;
                showDeliveryMethods();
            }
        });
    }

    private void showDeliveryMethods() {
        showAppPaymentDeliveryBanner();
        view.initDeliveryFieldsPanel(user);

        List<DeliveryTypeInfo> deliveryTypeInfos = loadDeliveryTypeInfos();
        if (!deliveryTypeInfos.isEmpty()) {
            view.showDeliveryMethods(deliveryTypeInfos);
        }
    }

    private void showDeliveryMethodUnavailable() {
        String deliveryTypeLabel =
            constantsWithLookup.getString("deliveryTypeName" + getSelectedDeliveryType().getDeliveryType());
        view.showDeliveryMethodError(deliveryTypeLabel, deliveryConstants.deliveryServiceNotAvailable());
    }

    @Override
    public void initMajorCityField() {
        view.setMajorCityFieldOracle(new MajorCitySuggestOracle() {
            @Override
            public void doRequest(final Request request, final Callback callback) {
                deliveryService.loadMajorCities(request.getQuery(), request.getLimit(),
                    new AsyncCallback<List<String>>() {
                        @Override
                        public void onSuccess(List<String> cities) {
                            view.hideDeliveryMethodError();
                            MajorCitySuggestOracle.Response response = new MajorCitySuggestOracle.Response();
                            response.setSuggestions(cities);
                            callback.onSuggestionsReady(request, response);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            showDeliveryMethodUnavailable();
                        }
                    });
            }
        });
    }

    private DeliveryTypeInfo getSelectedDeliveryType() {
        return currentDeliveryTypeInfo;
    }

    private boolean trialDeliveryAvailable() {
        boolean isTrialDelivery = false;

        Order<?> singleBillOrder = null;
        if (bill != null && bill.getOrders().size() == 1) {
            singleBillOrder = bill.getOrders().iterator().next();
        } else if (bill == null && order != null) {
            singleBillOrder = order;
        }

        if (singleBillOrder != null && singleBillOrder.getQuantity() <= 1) {
            Product product = singleBillOrder.getProduct();
            /**
             * Доставка простым письмом - только через свойства продукта
             * @see http://jira.minogin.ru/browse/IMAGEBOOK-343
             */
            isTrialDelivery = product != null && product.isTrialDelivery();
            //isTrialDelivery = product != null && (product.isTrialAlbum() || product.isTrialDelivery());
        }

        return isTrialDelivery;
        //return isTrialOrder() || isTrialDelivery;
    }

    private boolean isTrialOrder() {
        boolean trialOrder = false;
        if (bill == null && order != null) {
            trialOrder = true;
        } else if (bill != null && bill.getOrders().size() == 1) {
            Order<?> singleBillOrder = bill.getOrders().iterator().next();
            if (singleBillOrder.getQuantity() <= 1) {
                Product product = singleBillOrder.getProduct();
                trialOrder = (product != null) && product.isTrialAlbum();
            }
        }
        return trialOrder;
    }

    private List<DeliveryTypeInfo> loadDeliveryTypeInfos() {
        List<DeliveryTypeInfo> deliveryTypeInfos = new ArrayList<DeliveryTypeInfo>();

        for (Map.Entry<Integer, PostHouseType> entry : BILL_DELIVERY_TYPES_SHOW) {
            Integer deliveryType = entry.getKey();
            PostHouseType postHouseType = entry.getValue();

            int cost = 0;

            // Show only PostHouseType.TRIAL for trial delivery if bill (if present) contains only one order
            if (!trialDeliveryAvailable() && DeliveryType.TRIAL == deliveryType) {
                continue;
            }

            if (deliveryType == DeliveryType.POST) {
                PostHouseCalc postHouseCalc = new PostHouseCalc();

                if (postHouseType == PostHouseType.FIRST_CLASS && !isFirstClassAllowed(postHouseCalc)) {
                    continue;
                }
                cost = (getWeight() != null) ? postHouseCalc.getCost(postHouseType, getWeight()) : cost;
            }

            DeliveryTypeInfo info = new DeliveryTypeInfo(deliveryType, postHouseType, cost);

            String postHouseTypeString = (postHouseType != null) ? postHouseType.name() : "";
            info.setLabel(constantsWithLookup.getString("deliveryTypeName" + deliveryType + postHouseTypeString));
            info.setInfo(constantsWithLookup.getString("deliveryTypeInfo" + deliveryType + postHouseTypeString));
            info.setInfoComment(constantsWithLookup.getString("deliveryTypeInfoComment" + deliveryType));
            info.setComment(constantsWithLookup.getString("deliveryTypeComment" + deliveryType + postHouseTypeString));

            deliveryTypeInfos.add(info);
        }
        return deliveryTypeInfos;
    }

    private Integer getWeight() {
        Integer weight = null;
        if (bill != null) {
            weight = bill.getWeight();
        } else if (order != null) {
            weight = order.getTotalWeight();
        }
        return weight;
    }

    private boolean isFirstClassAllowed(PostHouseCalc postHouseCalc) {
        return (bill != null) ? postHouseCalc.isFirstClassAllowed(bill) : postHouseCalc.isFirstClassAllowed(order);
    }

    @Override
    public void onDeliveryMethodSelected(DeliveryTypeInfo deliveryTypeInfo) {
        if (deliveryTypeInfo == null) {
            return;
        }

        view.hideSDEKWidget();
        view.hideMajorCity();
        view.hidePickpointWidget();
        view.hideFields();

        currentDeliveryTypeInfo = deliveryTypeInfo;
        if (bill == null && isTrialOrder()) {
            // Create bill for trial order and paid delivery
            orderService.createBillForTrialOrder(order.getId(), new Bill(order.getUser(), new Date()), user.getId(),
                new AsyncCallback<Bill>() {
                    @Override
                    public void onSuccess(Bill trialBill) {
                        bill = trialBill;
                        bill.setTrial(true);
                        showDeliveryForm(currentDeliveryTypeInfo);
                    }
                });
        } else {
            showDeliveryForm(currentDeliveryTypeInfo);
        }
    }

    private void showDeliveryForm(DeliveryTypeInfo deliveryTypeInfo) {
        if (bill != null) {
            bill.setDeliveryType(deliveryTypeInfo.getDeliveryType());

            DeliveryDiscount deliveryDiscount = discountsMap.get(deliveryTypeInfo.getDeliveryType());
            bill.setDeliveryDiscountPc(deliveryDiscount != null ? deliveryDiscount.getDiscountPc() : null);

            bill.setDeliveryCost(deliveryTypeInfo.getCost());
            bill.setDeliveryComment(deliveryTypeInfo.getComment());
            bill.setMshDeliveryService(null);
            bill.setOrientDeliveryDate(null);
            bill.setDeliveryTime(null);
        }

        selectedAddress = null;

        if (deliveryTypeInfo.getDeliveryType() == DeliveryType.EXW) {
            view.showAddressListWithPickupForm(user);
        } else if (deliveryTypeInfo.getDeliveryType() == DeliveryType.MAJOR) {
            view.resetMajorCity();
            view.showMajorCity();
        } else if (deliveryTypeInfo.getDeliveryType() == DeliveryType.POSTAMATE) {
            view.showPickpointWidget(user);
        } else if (deliveryTypeInfo.getDeliveryType() == DeliveryType.SDEK) {
            deliveryService.loadSDEKPackagesData(bill, new AsyncCallback<List<SDEKPackage>>() {
                @Override
                public void onSuccess(List<SDEKPackage> sdekPackages) {
                    view.showSDEKWidget(sdekPackages);
                }
            });
        } else { // others
            view.showAddressListWithPostHouseForm(user);
        }
        /*
        Please don't delete commented code
		if (deliveryTypeInfo.getDeliveryType() == DeliveryType.POST) {
			view.showPostDeliveryWarning(deliveryTypeInfo.getPostHouseType());
		} else {
			view.hidePostDeliveryWarning();
		}*/
    }

    @Override
    public void onDeliveryAddressSelected(Address address) {
        selectedAddress = (address == null) ? new Address() : address;
        Integer deliveryType = getSelectedDeliveryType().getDeliveryType();

        view.hideFormIncompleteLabel();
        view.resetErrorFields(deliveryType);

        if (deliveryType == DeliveryType.EXW) {
            view.fillPickupFields(selectedAddress);
        } else if (deliveryType == DeliveryType.MAJOR) {
            view.fillMajorFields(selectedAddress);
        } else if (deliveryType == DeliveryType.MULTISHIP) {
            throw new UnsupportedOperationException("Multiship is no longer supported");
        } else if (deliveryType == DeliveryType.POSTAMATE) {
            view.fillPickpointFields(selectedAddress);
        } else if (deliveryType == DeliveryType.DDELIVERY) {
            throw new UnsupportedOperationException("DDelivery is no longer supported");
        } else if (deliveryType == DeliveryType.SDEK) {
            view.fillSDEKFields(selectedAddress);
        } else {
            view.fillPostHouseFields(selectedAddress);
        }

        view.showDeliveryForm();
    }

    @Override
    public void onMajorCitySelecting() {
        view.hideFields();
        view.hideMajorConsAndTimePanel();
    }

    @Override
    public void onMajorCitySelected(final String majorCityName) {
        deliveryService.getMajorCostAndTime(majorCityName, bill.getWeight(), new AsyncCallback<MajorData>() {
            @Override
            public void onSuccess(MajorData result) {
                view.hideDeliveryMethodError();
                view.showMajorCostAndTime(result);
                view.showAddressListWithMajorForm(user);

                bill.setDeliveryCost(result.getCost());
                bill.setDeliveryTime(result.getTime());

                selectedAddress.setCity(majorCityName);
            }

            @Override
            public void onFailure(Throwable caught) {
                showDeliveryMethodUnavailable();
            }
        });
    }

    @Override
    public void onPayButtonClick() {
        if (bill != null && bill.getDeliveryType() == null) {
            view.showDeliveryTypeNotSelectedMessage();
            return;
        }

        final Integer selectedDeliveryType = getSelectedDeliveryType().getDeliveryType();

        view.hideValidationErrors();
        view.hideFormIncompleteLabel();
        view.resetErrorFields(selectedDeliveryType);

        this.selectedAddress = (selectedAddress == null) ? new Address() : selectedAddress;

        if (selectedDeliveryType == DeliveryType.EXW) {
            if (view.pickupFormComplete() && view.pickupFormValid()) {
                view.fetchPickupFields(selectedAddress);
                view.showProgressIndicator();
                attachAddress();
            } else if (!view.pickupFormComplete()) {
                view.alertFormIncomplete();
                view.highlightIncompletePickupFields();
            } else if (!view.pickupFormValid()) {
                view.highlightInvalidPickupFields();
                view.showPickupValidationErrors();
            }
        } else if (selectedDeliveryType == DeliveryType.MAJOR) {
            if (view.majorFormComplete() && view.majorFormValid()) {
                view.fetchMajorFields(selectedAddress);
                new SuggestOptionalAddressValidator().validateOptional();
            } else if (!view.majorFormComplete()) {
                view.alertFormIncomplete();
                view.highlightIncompleteMajorFields();
            } else if (!view.majorFormValid()) {
                view.highlightInvalidMajorFields();
                view.showMajorValidationErrors();
            }
        } else if (selectedDeliveryType == DeliveryType.POSTAMATE) {
            if (view.pickpointFormComplete() && view.pickpointFormValid()) {
                view.fetchPickpointFields(selectedAddress, bill);
                view.showProgressIndicator();
                bill.setDeliveryComment(deliveryConstants.postamateId() + " " + bill.getPickpointPostamateID() + ", "
                    + deliveryConstants.postameteAddress() + ": " + bill.getPickpointAddress());
                attachAddress();
            } else if (!view.pickpointFormComplete()) {
                view.alertFormIncomplete();
                view.highlightIncompletePickpointFields();
            } else if (!view.pickpointFormValid()) {
                view.highlightInvalidPickpointFields();
                view.showPickpointFormValidationErrors();
            }
        } else if (selectedDeliveryType == DeliveryType.DDELIVERY) {
            throw new UnsupportedOperationException("DDelivery is no longer supported");
        } else if (selectedDeliveryType == DeliveryType.SDEK) {
            if (view.sdekFormComplete() && view.sdekFormValid()) {
                view.fetchSDEKFields(selectedAddress);
                selectedAddress.setCity(sdekCity);
                if (SDEKDeliveryType.COURIER.name().equals(bill.getDdeliveryType())) {
                    new SuggestOptionalAddressValidator().validateOptional();
                } else {
                    attachAddress();
                }
            } else if (!view.sdekFormComplete()) {
                view.alertFormIncomplete();
                view.highlightIncompleteSDEKFields();
            } else if (!view.sdekFormValid()) {
                view.highlightInvalidSDEKFields();
                view.showSDEKValidationErrors();
            }
        } else { // post house or post house first class or simple letter (trial delivery)
            if (view.postHouseFormComplete() && view.postHouseFormValid()) {
                view.fetchPostHouseFields(selectedAddress);
                new SuggestOptionalAddressValidator().validateOptional();
            } else if (!view.postHouseFormComplete()) {
                view.alertFormIncomplete();
                view.highlightIncompletePostHouseFields();
            } else if (!view.postHouseFormValid()) {
                view.highlightInvalidPostHouseFields();
                view.showPostHouseValidationErrors();
            }
        }
    }

    private void attachAddress() {
        view.disablePayButton();
        orderService.attachAddress(bill, orderId, selectedAddress, new AsyncCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                Bill resultBill = (result != null) ? (Bill) result.get("bill") : null;
                Set<Order<?>> notifyOrders = new HashSet<Order<?>>();

                boolean isTrialDelivery = (resultBill == null) || (resultBill.isTrial() && resultBill.getTotal() == 0);
                if (isTrialDelivery) {
                    attachAddressToTrial(resultBill, orderId, selectedAddress, notifyOrders);
                } else if (resultBill.getTotal() == 0 || user.isAdvOrders()) {
                    markBillAsAnAdvertisingAndPaid(resultBill);
                } else {
                    placeController.goTo(new PaymentMethodsPlace(resultBill));
                }

                if (resultBill != null) {
                    notifyOrders.addAll(bill.getOrders());
                }

                orderService.notifyAddressCommentSpecified(notifyOrders, selectedAddress, new VoidAsyncCallback());
            }
        });
    }

    private void attachAddressToTrial(Bill bill, Integer orderId, Address address, final Set<Order<?>> notifyOrders) {
        orderService.attachAddressToTrial(bill, orderId, address, new AsyncCallback<Order<?>>() {
            @Override
            public void onSuccess(Order<?> result) {
                notifyOrders.add(result);
                orderTrial(result.getId());
            }
        });
    }

    private void orderTrial(Integer orderId) {
        orderService.orderTrial(orderId, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                placeController.goTo(new ProcessPlace());
            }
        });
    }

    private void markBillAsAnAdvertisingAndPaid(Bill bill) {
        orderService.markBillAsAnAdvertisingAndPaid(bill, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (user.isAdvOrders()) {
                    view.notifyBillPaid();
                }
                placeController.goTo(new ProcessPlace());
            }
        });
    }

    @Override
    public void onPickpointPostamateSelected() {
        view.fetchPickpointWidgetFields(bill);

        PickPointData pickPointData = new PickPointData();
        pickPointData.setPostamateID(bill.getPickpointPostamateID());
        pickPointData.setAddress(bill.getPickpointAddress());
        pickPointData.setWeightGr(bill.getWeight());
        //pickPointData.setRateZone(bill.getPickpointRateZone());
        //pickPointData.setTrunkCoeff(bill.getPickpointTrunkCoeff());

        deliveryService.getPickPointCostAndTime(pickPointData, new AsyncCallback<PickPointData>() {
            @Override
            public void onSuccess(PickPointData pickPointData) {
                bill.setDeliveryCost(pickPointData.getCost());
                bill.setPickpointRateZone(pickPointData.getRateZone());
                bill.setPickpointTrunkCoeff(pickPointData.getTrunkCoeff());

                view.hideDeliveryMethodError();
                view.showPickpointCostAndTime(pickPointData);
                view.showPickpointForm(user);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.hideFields();
                view.hidePickpointCostAndTime();
                if (caught instanceof PostamateUnavailableException) {
                    String deliveryTypeLabel = constantsWithLookup.getString(
                        "deliveryTypeName" + getSelectedDeliveryType().getDeliveryType());
                    String msg = deliveryConstants.pickpointPostamateUnavailable();
                    view.showDeliveryMethodError(deliveryTypeLabel, msg);
                } else {
                    showDeliveryMethodUnavailable();
                }
            }
        });
    }

    @Override
    public void onSDEKPickupSelected(SDEKPickupData pickupData) {
        if (bill == null) {
            return;
        }
        sdekCity = pickupData.getCityName();
        bill.setDeliveryType(DeliveryType.SDEK);
        bill.setSdekDeliveryType(SDEKDeliveryType.PICKUP.name());
        bill.setSdekPickupPointId(pickupData.getId());
        bill.setSdekCityId(pickupData.getCityId());
        bill.setSdekPickupPointAddress(pickupData.getCityName() + ", " + pickupData.getAddress());
        bill.setSdekTarifCode(pickupData.getTarifCode());
        bill.setDeliveryCost(pickupData.getPrice());
        bill.setDeliveryTime(pickupData.getMinTerm());
        //TODO bill.setDeliveryComment(pickupData.getAddressComment());
        view.showSDEKForm(SDEKDeliveryType.PICKUP, user);
    }

    @Override
    public void onSDEKCourierSelected(SDEKCourierData courierData) {
        if (bill == null) {
            return;
        }
        sdekCity = courierData.getCityName();
        bill.setDeliveryType(DeliveryType.SDEK);
        bill.setSdekDeliveryType(SDEKDeliveryType.COURIER.name());
        bill.setSdekPickupPointId(courierData.getId());
        bill.setSdekCityId(courierData.getCityId());
        bill.setSdekTarifCode(courierData.getTarifCode());
        bill.setDeliveryCost(courierData.getPrice());
        bill.setDeliveryTime(courierData.getMinTerm());
        selectedAddress = null;
        view.showSDEKForm(SDEKDeliveryType.COURIER, user);
    }

    @Override
    public int computeDeliveryDiscountPc(int deliveryType) {
        DeliveryDiscount deliveryDiscount = discountsMap.get(deliveryType);
        return deliveryDiscount != null ? deliveryDiscount.getDiscountPc() : 0;
    }

    private class SuggestOptionalAddressValidator extends AddressValidator {
        public SuggestOptionalAddressValidator() {
            super(
                DeliveryActivity.this.selectedAddress,
                DeliveryActivity.this.appConstants,
                DeliveryActivity.this.personalConstants
            );
        }

        @Override
        protected void done() {
            view.showProgressIndicator();
            attachAddress();
        }
    }

    private void showAppPaymentDeliveryBanner() {
        bannerService.getAppPaymentDeliveryText(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String bannerText) {
                if (Strings.isNullOrEmpty(bannerText)) {
                    return;
                }
                view.showBanner(bannerText);
            }
        });
    }
}
