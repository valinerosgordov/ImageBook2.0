package ru.imagebook.client.app.ctl.order;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import ru.imagebook.client.app.ctl.payment.BillsPlace;
import ru.imagebook.client.app.ctl.payment.DeliveryPlace;
import ru.imagebook.client.app.service.OrderRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.order.OrderView;
import ru.imagebook.client.common.service.Calc;
import ru.imagebook.client.common.service.CalcImpl;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.shared.model.*;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.imagebook.shared.service.app.CodeAlreadyUsedError;
import ru.imagebook.shared.service.app.IncorrectCodeError;
import ru.imagebook.shared.service.app.IncorrectPeriodCodeError;
import ru.imagebook.shared.service.app.ProductNotAvailableForBonusCodeError;
import ru.minogin.core.client.bean.PropertyChangeEvent;
import ru.minogin.core.client.bean.PropertyChangeListener;

import javax.annotation.Nullable;
import java.util.*;


public class OrderActivity extends AbstractActivity implements OrderPresenter {
    private static final int RELOAD_INCOMING_ORDERS_DELAY_IN_MS = 1000;

    private final OrderView view;
    private AlbumService albumService;
    private SecurityService securityService;
    private final OrderRemoteServiceAsync orderService;
    private final PlaceController placeController;
    private final VendorService vendorService;
    private final I18nService i18nService;

    private List<Color> colors;
    private List<Order<?>> cachedBasketOrders;
    private List<Flyleaf> flyleafs;
    private List<Vellum> vellums;
    private Map<Integer, List<Product>> products;
    private Map<Integer, List<Product>> editorProducts;

    @Inject
    public OrderActivity(OrderView view, VendorService vendorService, I18nService i18nService,
                         OrderRemoteServiceAsync orderService, PlaceController placeController, AlbumService albumService,
                         SecurityService securityService) {
        this.view = view;
        this.albumService = albumService;
        this.securityService = securityService;
        view.setPresenter(this);
        this.vendorService = vendorService;
        this.i18nService = i18nService;
        this.orderService = orderService;
        this.placeController = placeController;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view);
        showOrders();
        //view.showCreateAlbumButton(securityService.isRoot());
    }

    private void showOrders() {
        orderService.loadFlyleafs(new AsyncCallback<List<Flyleaf>>() {
            @Override
            public void onSuccess(List<Flyleaf> result) {
                flyleafs = result;
                loadVellums();
            }
        });
    }

    private void loadVellums() {
        orderService.loadVellums(new AsyncCallback<List<Vellum>>() {
            @Override
            public void onSuccess(List<Vellum> result) {
                vellums = result;

                loadProducts();
                loadProductColors();
                loadIncomingOrders();
                loadBasketOrders();
            }
        });
    }

    private void loadProducts() {
        orderService.loadProducts(new AsyncCallback<Map<Integer, List<Product>>>() {
            @Override
            public void onSuccess(Map<Integer, List<Product>> result) {
                products = result;
                editorProducts = new LinkedHashMap<Integer, List<Product>>();

                if (securityService.hasRole(Role.OPERATOR)) {
                    products.remove(1);
                    products.remove(4);
                    editorProducts.putAll(products);
                }
                else {
                    List<Product> springProducts = products.get(ProductType.SPRING);
                    List<Product> calendars = Lists.newArrayList(Iterables.filter(springProducts, new Predicate<Product>() {
                        @Override
                        public boolean apply(@Nullable Product p) {
                            return Arrays.asList(21, 24, 27, 31, 32).contains(p.getNumber());
                        }
                    }));
                    editorProducts.put(ProductType.SPRING, calendars);
                }
            }
        });
    }

    private void loadProductColors() {
        orderService.loadProductColors(new AsyncCallback<List<Color>>() {
            @Override
            public void onSuccess(List<Color> result) {
                colors = result;
            }
        });
    }

    @Override
    public void loadIncomingOrders() {
        orderService.loadIncomingOrders(new AsyncCallback<List<Order<?>>>() {
            @Override
            public void onSuccess(List<Order<?>> incomingOrders) {
                view.showIncomingOrders(incomingOrders, i18nService.getLocale());

                boolean generationDone = true;
                for (Order<?> order : incomingOrders) {
                    if (order.getState() == OrderState.FLASH_GENERATION
                            || order.getState() == OrderState.JPEG_GENERATION
                            || order.getState() == OrderState.JPEG_ONLINE_GENERATION
                            || order.getState() == OrderState.JPEG_BOOK_GENERATION
                    ) {
                        generationDone = false;
                    }
                }

                if (!generationDone) {
                    reloadWhenFlashGenerated();
                }
            }
        });
    }

    private void reloadWhenFlashGenerated() {
        new Timer() {
            @Override
            public void run() {
                orderService.loadIncomingOrders(new AsyncCallback<List<Order<?>>>() {
                    @Override
                    public void onSuccess(List<Order<?>> orders) {
                        boolean generationDone = true;
                        for (Order<?> order : orders) {
                            if (order.getState() == OrderState.FLASH_GENERATION
                                    || order.getState() == OrderState.JPEG_GENERATION
                                    || order.getState() == OrderState.JPEG_ONLINE_GENERATION
                                    || order.getState() == OrderState.JPEG_BOOK_GENERATION
                            ) {
                                generationDone = false;
                            }
                        }

                        if (!generationDone) {
                            reloadWhenFlashGenerated();
                        } else {
                            showOrders();
                        }
                    }
                });
            }
        }.schedule(RELOAD_INCOMING_ORDERS_DELAY_IN_MS);
    }

    @Override
    public void loadBasketOrders() {
        loadBasketOrders(null);
    }

    @Override
    public void loadBasketOrders(final List<Order<?>> basketOrders) {
        orderService.loadBasketOrders(basketOrders, new AsyncCallback<List<Order<?>>>() {
            @Override
            public void onSuccess(final List<Order<?>> basketOrdersResult) {
                view.getSubmitOrderButton().setEnabled(!basketOrdersResult.isEmpty());

                for (final Order<?> order : basketOrdersResult) {
                    order.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void onPropertyChange(PropertyChangeEvent event) {
                            if (event.getName().equals(Order.QUANTITY)) {
                                Integer newQuantity = event.getNewValue();
                                if (newQuantity <= 0) {
                                    order.directSet(Order.QUANTITY, 1);
                                }

                                loadBasketOrders(basketOrdersResult);
                            }
                        }
                    });
                }

                cachedBasketOrders = basketOrdersResult;
                view.showBasketOrders(basketOrdersResult, i18nService.getLocale(), flyleafs, vellums);
            }
        });
    }

    @Override
    public void onOrderButtonClicked(Order<?> order) {
        if (order.isTrial()) {
            placeController.goTo(new DeliveryPlace(order));
        } else {
            moveOrderToBasket(order);
        }
    }

    private void moveOrderToBasket(Order<?> order) {
        orderService.moveOrderToBasket(order.getId(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadIncomingOrders();
                loadBasketOrders();
            }
        });
    }

    @Override
    public void moveOrdersToBasket(Set<Order<?>> orders) {
        orderService.moveOrdersToBasket(orders, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadIncomingOrders();
                loadBasketOrders();
            }
        });
    }


    @Override
    public void removeOrderFromBasket(Order<?> order) {
        orderService.removeOrderFromBasket(order.getId(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadIncomingOrders();
                loadBasketOrders();
            }
        });
    }

    @Override
    public void removeOrdersFromBasket(Set<Order<?>> orders) {
        orderService.removeOrdersFromBasket(orders, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadIncomingOrders();
                loadBasketOrders();
            }
        });
    }

    @Override
    public void onSubmitOrderButtonClicked() {
        if (view.getSubmitOrderButton().isEnabled() && cachedBasketOrders != null) {
            Vendor vendor = vendorService.getVendor();
            if (vendor.isNoBonusCheck()) {
                boolean bonusCodeSpecified = true;
                for (Order<?> order : cachedBasketOrders) {
                    if (order.getBonusCode() == null) {
                        bonusCodeSpecified = false;
                    }
                }
                if (!bonusCodeSpecified) {
                    view.showSubmitOrderConfirmation();
                } else {
                    submitOrder();
                }
            } else {
                submitOrder();
            }
        }
    }

    @Override
    public void submitOrder() {
        view.getSubmitOrderButton().setEnabled(false);
        orderService.submitOrder(cachedBasketOrders, new AsyncCallback<Bill>() {
            @Override
            public void onSuccess(Bill bill) {
                if (bill.getTotal() == 0) {
                    placeController.goTo(new DeliveryPlace(bill));
                } else {
                    placeController.goTo(new BillsPlace());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                view.getSubmitOrderButton().setEnabled(true);
                super.onFailure(caught);
            }
        });
    }

    @Override
    public void editOrder(Order<?> order) {
        view.showOrderEditForm(order, colors, i18nService.getLocale());
    }

    @Override
    public void setOrderParams(int orderId, Integer colorId, Integer coverLam, Integer pageLam) {
        orderService.setOrderParams(orderId, colorId, coverLam, pageLam, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadBasketOrders(null);
                view.hideOrderEditForm();
            }
        });
    }

    @Override
    public void setFlyleaf(Order<?> order, Flyleaf flyleaf) {
        orderService.setFlyleaf(order.getId(), flyleaf.getId(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadBasketOrders(null);
            }
        });
    }

    @Override
    public Map<Flyleaf, Integer> computeFlyleafsPrices(Order<?> order, List<Flyleaf> flyleafs) {
        Map<Flyleaf, Integer> ret = new HashMap<Flyleaf, Integer>();
        Calc calc = new CalcImpl(order, new PricingData());

        for (Flyleaf flyleaf : flyleafs) {
            ret.put(flyleaf, (int) calc.getFlyleafPrice(flyleaf));
        }

        return ret;
    }

    @Override
    public Map<Vellum, Integer> computeVellumsPrices(Order<?> order, List<Vellum> vellums) {
        Map<Vellum, Integer> ret = new HashMap<Vellum, Integer>();
        Calc calc = new CalcImpl(order, new PricingData());

        for (Vellum vellum : vellums) {
            ret.put(vellum, (int) calc.getVellumPrice(vellum));
        }

        return ret;
    }

    @Override
    public void setVellum(Order<?> order, Vellum vellum) {
        Integer vellumId = (vellum == null) ? null : vellum.getId();
        orderService.setVellum(order.getId(), vellumId, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadBasketOrders(null);
            }
        });
    }

    @Override
    public void createAlbumButtonClicked() {
        view.showCreateAlbumForm(editorProducts);
    }

    @Override
    public void createAlbum(Integer productId, Integer pageCount) {
        orderService.createAlbum(productId, pageCount, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String albumId) {
                albumService.editAlbum(albumId);
            }
        });
    }

    @Override
    public void enterBonusCode(Order<?> order) {
        view.showOrderBonusCodeForm(order);
    }

    @Override
    public void prepareToApplyBonusCode(final int orderId, final String code, final String deactivationCode) {
        orderService.prepareToApplyBonusCode(orderId, code, deactivationCode, new AsyncCallback<BonusAction>() {
            @Override
            public void onSuccess(BonusAction bonusAction) {
                view.showConfirmActionCodeDialog(bonusAction, orderId, code, deactivationCode);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.hideOrderBonusCodeForm();
                if (caught instanceof IncorrectCodeError) {
                    view.alertIncorrectCode();
                } else if (caught instanceof IncorrectPeriodCodeError) {
                    IncorrectPeriodCodeError ex = (IncorrectPeriodCodeError) caught;
                    view.alertIncorrectCodePeriod(ex.getBonusActionDateStart(), ex.getBonusActionDateEnd());
                } else {
                    super.onFailure(caught);
                }
            }
        });
    }

    @Override
    public void applyBonusCode(int orderId, String code, String deactivationCode) {
        orderService.applyBonusCode(orderId, code, deactivationCode, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadBasketOrders(null);
                view.hideOrderBonusCodeForm();
            }

            @Override
            public void onFailure(Throwable caught) {
                view.hideOrderBonusCodeForm();
                if (caught instanceof IncorrectCodeError) {
                    view.alertIncorrectCode();
                } else if (caught instanceof CodeAlreadyUsedError) {
                    view.alertCodeAlreadyUsed();
                } else if (caught instanceof ProductNotAvailableForBonusCodeError) {
                    view.alertProductNotAvailable();
                } else if (caught instanceof IncorrectPeriodCodeError) {
                    IncorrectPeriodCodeError ex = (IncorrectPeriodCodeError) caught;
                    view.alertIncorrectCodePeriod(ex.getBonusActionDateStart(), ex.getBonusActionDateEnd());
                } else {
                    super.onFailure(caught);
                }
            }
        });
    }

    @Override
    public void showActionCode(BonusAction action) {
        view.showConfirmActionCodeDialog(action);
    }

    @Override
    public void deleteOrders(Set<Order<?>> orders) {
        orderService.deleteOrders(orders, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.alertOrdersDeleted();
                loadIncomingOrders();
            }
        });
    }
}
