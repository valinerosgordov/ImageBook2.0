package ru.imagebook.client.app.view.payment;

import java.util.HashMap;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.ctl.payment.DeliveryPresenter;
import ru.imagebook.client.app.view.CompilerFactory;
import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.client.app.view.personal.PersonalConstants;
import ru.imagebook.client.common.service.CostCalculatorImpl;
import ru.imagebook.client.common.service.delivery.PostHouseType;
import ru.imagebook.client.common.service.delivery.SDEKDeliveryType;
import ru.imagebook.client.common.view.order.OrderConstants;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.DeliveryTypeInfo;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.app.MajorData;
import ru.imagebook.shared.model.app.PickPointData;
import ru.imagebook.shared.model.app.SDEKPackage;
import ru.minogin.core.client.lang.template.Compiler;


@Singleton
public class DeliveryViewImpl implements DeliveryView {
    interface DeliveryUiBinder extends UiBinder<Widget, DeliveryViewImpl> {
    }
    private static DeliveryUiBinder uiBinder = GWT.create(DeliveryUiBinder.class);

    @UiField
    HTMLPanel bannerPanel;
    @UiField
    HTML bannerHtml;
    @UiField
    HTMLPanel deliveryMethodsPanel;
    @UiField
    HTMLPanel deliveryFieldsRadioGroup;
    @UiField
    HTMLPanel deliveryFieldsPanel;
    @UiField
    Label deliveryFieldsLabel;
    @UiField
    HTMLPanel deliveryAddressPanel;
    @UiField
    SimplePanel deliveryFormPanel;
    @UiField
    Label formIncompleteLabel;
    @UiField
    HTMLPanel payButtonPanel;
    @UiField
    Button payButton;
    @UiField
    Image ajaxLoader;
    @UiField
    SimplePanel formErrorsContainer;
    @UiField
    HTMLPanel majorCityFieldPanel;
    @UiField(provided = true)
    SuggestBox majorCityField;
    @UiField
    HTMLPanel majorCostAndTimeFieldPanel;
    @UiField
    ParagraphElement majorCostAndTimeField;
    @UiField
    SimplePanel deliveryMethodErrorsContainer;
    @UiField
    SimplePanel pickpointWidgetContainer;
    @UiField
    SimplePanel sdekWidgetContainer;

    private final OrderConstants orderConstants;
    private final PersonalConstants personalConstants;
    private final DeliveryConstants deliveryConstants;
    private final CompilerFactory compilerFactory;

    private RadioButton psedoResetAddressRadioButton;

    private HashMap<PostHouseType, HTML> postDeliveryWarningElements = new HashMap<PostHouseType, HTML>();

    private DeliveryPresenter presenter;
    private PickupForm pickupForm;
    private PostHouseForm postHouseForm;
    private MajorForm majorForm;
    private PickpointWidget pickpointWidget;
    private PickpointForm pickpointForm;
    private RadioButton selectedAddressRadioButton;
    private SuggestOracle majorCityFieldOracle;
    private SDEKForm sdekForm;
    private SDEKWidget sdekWidget;

    @Inject
    public DeliveryViewImpl(DeliveryConstants deliveryConstants, OrderConstants orderConstants,
                            PersonalConstants personalConstants, CompilerFactory compilerFactory,
                            PickpointWidget pickpointWidget, SDEKWidget sdekWidget) {
        this.deliveryConstants = deliveryConstants;
        this.orderConstants = orderConstants;
        this.personalConstants = personalConstants;
        this.compilerFactory = compilerFactory;
        this.pickpointWidget = pickpointWidget;
        this.sdekWidget = sdekWidget;
    }

    @Override
    public void setPresenter(DeliveryPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setMajorCityFieldOracle(SuggestOracle majorCityFieldOracle) {
        this.majorCityFieldOracle = majorCityFieldOracle;
    }

    @Override
    public Widget asWidget() {
        // major init
        presenter.initMajorCityField();
        majorCityField = new SuggestBox(majorCityFieldOracle);

        Widget ui = uiBinder.createAndBindUi(this);

        // pickpoint init
        pickpointWidgetContainer.setWidget(pickpointWidget.asWidget());
        pickpointWidget.addPostamateSelectionEventHandler(new PostamateSelectionEventHandler() {
            @Override
            public void onPostamateSelected(PostamateSelectionEvent postamateSelectionEvent) {
                presenter.onPickpointPostamateSelected();
            }
        });

        // sdek init
        sdekWidgetContainer.setWidget(sdekWidget.asWidget());
        sdekWidget.setPresenter(presenter);

        return ui;
    }

    @Override
    public void initDeliveryFieldsPanel(User user) {
        List<Address> addresses = user.getAddresses();
        if (addresses.isEmpty()) {
            return;
        }

        Compiler compiler = compilerFactory.getCompiler();

        // User's address list
        int row = 0;
        for (final Address address : addresses) {
            String addressText = compiler.compile(personalConstants.addressTemplate(), address);
            RadioButton addressRadioButton = new RadioButton("address");
            addressRadioButton.setHTML(addressText);
            addressRadioButton.setStyleName("address-name");
            addressRadioButton.addValueChangeHandler(new AddressValueChangeHandler(address));
            deliveryAddressPanel.add(addressRadioButton);
        }

        // Other address
        RadioButton otherAddressRadioButton = new RadioButton("address", orderConstants.otherAddress());
        otherAddressRadioButton.setStyleName("address-name");
        otherAddressRadioButton.addValueChangeHandler(new AddressValueChangeHandler(null));
        deliveryAddressPanel.add(otherAddressRadioButton);

        psedoResetAddressRadioButton = new RadioButton("address");
        psedoResetAddressRadioButton.setVisible(false);
        deliveryAddressPanel.add(psedoResetAddressRadioButton);
    }

    @Override
    public void showDeliveryMethods(List<DeliveryTypeInfo> deliveryTypeInfos) {
        for (final DeliveryTypeInfo deliveryTypeInfo : deliveryTypeInfos) {
            addDeliveryCostInfoToLabel(deliveryTypeInfo);

            RadioButton deliveryTypeButton;
            if (deliveryTypeInfo.getDeliveryType() == DeliveryType.POST) {
                deliveryTypeButton = new RadioButton("deliveryType", new SafeHtml() {
                    @Override
                    public String asString() {
                        return deliveryTypeInfo.getLabel();
                    }
                });
            }else {
                deliveryTypeButton = new RadioButton("deliveryType", deliveryTypeInfo.getLabel());
            }

            deliveryTypeButton.addStyleName("delivery-name");

            /*
            Please don't delete commented code
            if (deliveryTypeInfo.getDeliveryType() == DeliveryType.POST) {
                HTML postDeliveryWarningHtml = new HTML(deliveryConstants.postDeliveryWarningHtml());
                postDeliveryWarningHtml.addStyleName("post-delivery-warning");
                postDeliveryWarningHtml.setVisible(false);
                postDeliveryWarningElements.put(deliveryTypeInfo.getPostHouseType(), postDeliveryWarningHtml);
                deliveryTypeButton.getElement().insertAfter(postDeliveryWarningHtml.getElement(), null);
                if (DeliveryType.POST == selected.getDeliveryType()
                        && deliveryTypeInfo.getPostHouseType().equals(selected.getPostHouseType())) {
                    showPostDeliveryWarning(deliveryTypeInfo.getPostHouseType());
                    deliveryTypeButton.setValue(true);
                }
            } else {
                deliveryTypeButton.setValue(selected.getDeliveryType().equals(deliveryTypeInfo.getDeliveryType()));
            }*/

            deliveryTypeButton.addValueChangeHandler(new DeliveryMethodValueChangeHandler(deliveryTypeInfo));
            deliveryFieldsRadioGroup.add(deliveryTypeButton);

            if (!Strings.isNullOrEmpty(deliveryTypeInfo.getInfo())) {
                String deliveryInfo = deliveryTypeInfo.getInfo();
                HTML deliveryTypeInfoHtml = new HTML(deliveryInfo);
                deliveryTypeInfoHtml.addStyleName("delivery-method-info");
                deliveryFieldsRadioGroup.add(deliveryTypeInfoHtml);
                if (!Strings.isNullOrEmpty(deliveryTypeInfo.getInfoComment())) {
                    HTML deliveryTypeInfoCommentHtml = new HTML(deliveryTypeInfo.getInfoComment());
                    deliveryTypeInfoCommentHtml.addStyleName("delivery-method-info");
                    deliveryFieldsRadioGroup.add(deliveryTypeInfoCommentHtml);
                }
            }
        }
        deliveryMethodsPanel.setVisible(true);
    }

    private void addDeliveryCostInfoToLabel(DeliveryTypeInfo deliveryTypeInfo) {
        int deliveryType = deliveryTypeInfo.getDeliveryType();
        int discountPc = presenter.computeDeliveryDiscountPc(deliveryType);
        if (deliveryType == DeliveryType.POST) {
            int cost = deliveryTypeInfo.getCost();
            if (discountPc > 0) {
                int costByDiscount = CostCalculatorImpl.computeCostByDiscount(cost, discountPc);
                deliveryTypeInfo.setLabel(deliveryTypeInfo.getLabel() + " - " + "<strike>" + cost
                    + " руб.</strike> " + costByDiscount + " руб.");
            } else {
                deliveryTypeInfo.setLabel(deliveryTypeInfo.getLabel() + " - " + cost + " руб.");
            }
        } else if (deliveryType == DeliveryType.EXW || deliveryType == DeliveryType.TRIAL) {
            deliveryTypeInfo.setLabel(deliveryTypeInfo.getLabel() + " - " + deliveryConstants.free());
        }
        if (discountPc > 0 && deliveryType != DeliveryType.POST) {
            String discountInfo = "";
            discountInfo = " (скидка " + discountPc + " %)";
            deliveryTypeInfo.setLabel(deliveryTypeInfo.getLabel() + discountInfo);
        }
    }

    private void showDeliveryFieldsPanel(User user, IsWidget deliveryForm, boolean showAddressList) {
        deliveryFieldsPanel.setVisible(false);
        deliveryFieldsLabel.setVisible(false);
        deliveryAddressPanel.setVisible(false);
        hideFormIncompleteLabel();

        deliveryFormPanel.setWidget(deliveryForm);

        if (showAddressList && !user.getAddresses().isEmpty()) {
            // show only addresses list without address form
            deliveryFieldsLabel.setVisible(true);
            deliveryAddressPanel.setVisible(true);
            deliveryFormPanel.setVisible(false);
            psedoResetAddressRadioButton.setValue(true);
        } else {
            showDeliveryForm();
        }

        deliveryFieldsPanel.setVisible(true);
    }

    @Override
    public void showDeliveryForm() {
        deliveryFormPanel.setVisible(true);
        showPayButtonPanel();
    }

    @Override
    public void showAddressListWithPickupForm(User user) {
        pickupForm = new PickupForm(user);
        showDeliveryFieldsPanel(user, pickupForm, false);
        pickupForm.setDefaultValues();
    }

    @Override
    public void fillPickupFields(Address address) {
        pickupForm.fill(address);
    }

    @Override
    public void fetchPickupFields(Address address) {
        pickupForm.fetch(address);
    }

    @Override
    public boolean pickupFormComplete() {
        return pickupForm.isComplete();
    }

    @Override
    public boolean pickupFormValid() {
        return pickupForm.isValid();
    }

    @Override
    public void highlightIncompletePickupFields() {
        pickupForm.highlightEmptyFields();
    }

    @Override
    public void highlightInvalidPickupFields() {
        pickupForm.highlightInvalidFields();
    }

    @Override
    public void showPickupValidationErrors() {
        showValidationErrors(pickupForm);
    }

    @Override
    public void showAddressListWithPostHouseForm(User user) {
        postHouseForm = new PostHouseForm(user);
        showDeliveryFieldsPanel(user, postHouseForm, true);
        postHouseForm.setDefaultValues();
    }

    @Override
    public void fillPostHouseFields(Address address) {
        postHouseForm.fill(address);
    }

    @Override
    public void fetchPostHouseFields(Address address) {
        postHouseForm.fetch(address);
    }

    @Override
    public void highlightIncompletePostHouseFields() {
        postHouseForm.highlightEmptyFields();
    }

    @Override
    public void highlightInvalidPostHouseFields() {
        postHouseForm.highlightInvalidFields();
    }

    @Override
    public void showPostHouseValidationErrors() {
        showValidationErrors(postHouseForm);
    }

    @Override
    public boolean postHouseFormComplete() {
        return postHouseForm.isComplete();
    }

    @Override
    public boolean postHouseFormValid() {
        return postHouseForm.isValid();
    }

    @Override
    public void showAddressListWithMajorForm(User user) {
        majorForm = new MajorForm(user);
        showDeliveryFieldsPanel(user, majorForm, false);
        majorForm.setDefaultValues();
        presenter.onDeliveryAddressSelected(null);
    }

    @Override
    public void showPickpointWidget(User user) {
        pickpointWidget.showWidget();
    }

    @Override
    public void hidePickpointWidget() {
        pickpointWidget.hideWidget();
    }

    @Override
    public void showPickpointForm(User user) {
        pickpointForm = new PickpointForm(user);
        showDeliveryFieldsPanel(user, pickpointForm, false);
        pickpointForm.setDefaultValues();
        presenter.onDeliveryAddressSelected(null);
    }

    @Override
    public void showPickpointCostAndTime(PickPointData pickPointData) {
        int discountPc = presenter.computeDeliveryDiscountPc(DeliveryType.POSTAMATE);
        pickpointWidget.showCostAndTime(pickPointData, discountPc);
    }

    @Override
    public void hidePickpointCostAndTime() {
        pickpointWidget.hideCostAndTime();
    }

    @Override
    public void fillPickpointFields(Address address) {
        pickpointForm.fill(address);
    }

    @Override
    public void fetchPickpointFields(Address address, Bill bill) {
        pickpointForm.fetch(address);
        pickpointWidget.fetch(bill);
    }



    @Override
    public void fetchPickpointWidgetFields(Bill bill) {
        pickpointWidget.fetch(bill);
    }

    @Override
    public void showSDEKWidget(List<SDEKPackage> sdekPackages) {
        sdekWidget.show(sdekPackages);
    }

    @Override
    public void hideSDEKWidget() {
        sdekWidget.hide();
    }

    @Override
    public void showSDEKForm(SDEKDeliveryType deliveryType, User user) {
        sdekForm = new SDEKForm(user, deliveryType);
        showDeliveryFieldsPanel(user, sdekForm, false);
        sdekForm.setDefaultValues();
        presenter.onDeliveryAddressSelected(null);
    }

    @Override
    public void fillSDEKFields(Address address) {
        sdekForm.fill(address);
    }

    @Override
    public boolean sdekFormComplete() {
        return sdekForm.isComplete();
    }

    @Override
    public boolean sdekFormValid() {
        return sdekForm.isValid();
    }

    @Override
    public void fetchSDEKFields(Address address) {
        sdekForm.fetch(address);
    }

    @Override
    public void highlightIncompleteSDEKFields() {
        sdekForm.highlightEmptyFields();
    }

    @Override
    public void highlightInvalidSDEKFields() {
        sdekForm.highlightInvalidFields();
    }

    @Override
    public void showSDEKValidationErrors() {
        showValidationErrors(sdekForm);
    }

    @Override
    public void showPostDeliveryWarning(PostHouseType postHouseType) {
        hidePostDeliveryWarning();
        if (postDeliveryWarningElements.get(postHouseType) != null) {
            postDeliveryWarningElements.get(postHouseType).setVisible(true);
        }
    }

    @Override
    public void hidePostDeliveryWarning() {
        for (PostHouseType warningKey : postDeliveryWarningElements.keySet()) {
            postDeliveryWarningElements.get(warningKey).setVisible(false);
        }
    }

    @Override
    public boolean pickpointFormComplete() {
        return pickpointForm.isComplete();
    }

    @Override
    public boolean pickpointFormValid() {
        return pickpointForm.isValid();
    }

    @Override
    public void highlightIncompletePickpointFields() {
        pickpointForm.highlightEmptyFields();
    }

    @Override
    public void highlightInvalidPickpointFields() {
        pickpointForm.highlightInvalidFields();
    }

    @Override
    public void showPickpointFormValidationErrors() {
        showValidationErrors(pickpointForm);
    }

    @Override
    public void fillMajorFields(Address address) {
        majorForm.fill(address);
    }

    @Override
    public void fetchMajorFields(Address address) {
        majorForm.fetch(address);
    }

    @Override
    public void highlightIncompleteMajorFields() {
        majorForm.highlightEmptyFields();
    }

    @Override
    public void highlightInvalidMajorFields() {
        majorForm.highlightInvalidFields();
    }

    @Override
    public void showMajorValidationErrors() {
        showValidationErrors(majorForm);
    }

    @Override
    public boolean majorFormComplete() {
        return majorForm.isComplete();
    }

    @Override
    public boolean majorFormValid() {
        return majorForm.isValid();
    }

    @Override
    public void hideFields() {
        deliveryFieldsPanel.setVisible(false);
        hideDeliveryMethodError();
        hideFormIncompleteLabel();
        hideValidationErrors();
        hidePayButtonPanel();
    }

    private void hidePayButtonPanel() {
        payButtonPanel.setVisible(false);
    }

    private void showPayButtonPanel() {
        payButtonPanel.setVisible(true);
    }

    @Override
    public void showMajorCity() {
        majorCityFieldPanel.setVisible(true);
    }

    @Override
    public void hideMajorCity() {
        majorCityFieldPanel.setVisible(false);
        majorCostAndTimeFieldPanel.setVisible(false);
    }

    @Override
    public void resetMajorCity() {
        majorCityField.setValue(null);
        majorCostAndTimeField.setInnerText("");
    }

    @Override
    public void hideMajorConsAndTimePanel() {
        majorCostAndTimeFieldPanel.setVisible(false);
    }

    @Override
    public void showMajorCostAndTime(MajorData majorData) {
        int discountPc = presenter.computeDeliveryDiscountPc(DeliveryType.MAJOR);
        int cost = majorData.getCost();

        if (discountPc > 0){
            int costByDiscount = CostCalculatorImpl.computeCostByDiscount(cost, discountPc);
            majorCostAndTimeField.setInnerHTML(deliveryConstants.majorDiscountedCostAndTimeText(cost, costByDiscount,
                majorData.getTime()));
        } else {
            majorCostAndTimeField.setInnerText(deliveryConstants.majorCostAndTimeText(cost, majorData.getTime()));
        }

        majorCostAndTimeFieldPanel.setVisible(true);
    }

    @UiHandler("majorCityField")
    void onMajorCityFieldKeyUp(KeyUpEvent event) {
        presenter.onMajorCitySelecting();
    }

    @UiHandler("majorCityField")
    void onMajorCityFieldChange(SelectionEvent<SuggestOracle.Suggestion> event) {
        SuggestOracle.Suggestion suggestion = event.getSelectedItem();
        presenter.onMajorCitySelected(suggestion.getReplacementString());
    }

    @Override
    public void alertFormIncomplete() {
        formIncompleteLabel.setVisible(true);
    }

    @UiHandler("payButton")
    void onPayButtonClick(ClickEvent event) {
        presenter.onPayButtonClick();
    }

    @Override
    public void disablePayButton() {
        payButton.setEnabled(false);
    }

    @Override
    public void showProgressIndicator() {
        ajaxLoader.setVisible(true);
    }

    @Override
    public void hideProgressIndicator() {
        ajaxLoader.setVisible(false);
    }

    private void showValidationErrors(Form deliveryForm) {
        formErrorsContainer.setVisible(true);
        formErrorsContainer.clear();
        if (!deliveryForm.getErrors().isEmpty()) {
            StringBuilder errorsHtml = new StringBuilder();
            for (String error : deliveryForm.getErrors()) {
                errorsHtml.append("<div class=\"error\">").append(error).append("</div>");
            }
            HTMLPanel htmlPanel = new HTMLPanel(errorsHtml.toString());
            formErrorsContainer.add(htmlPanel);
        }
    }

    @Override
    public void hideValidationErrors() {
        formErrorsContainer.clear();
        formErrorsContainer.setVisible(false);
    }

    @Override
    public void hideFormIncompleteLabel() {
        formIncompleteLabel.setVisible(false);
    }

    @Override
    public void resetErrorFields(Integer deliveryType) {
        if (deliveryType == DeliveryType.EXW) {
            pickupForm.resetErrorFields();
        } else if (deliveryType == DeliveryType.MAJOR) {
            majorForm.resetErrorFields();
        } else if (deliveryType == DeliveryType.MULTISHIP) {
            throw new UnsupportedOperationException("Multiship is no longer supported");
        } else if (deliveryType == DeliveryType.POSTAMATE) {
            pickpointForm.resetErrorFields();
        } else if (deliveryType == DeliveryType.SDEK) {
            sdekForm.resetErrorFields();
        } else {
            postHouseForm.resetErrorFields();
        }
    }

    @Override
    public void showDeliveryTypeNotSelectedMessage() {
        Notify.error(orderConstants.noDeliveryMethod());
    }

    @Override
    public void showDeliveryMethodError(String deliveryTypeLabel, String errorMsg) {
        deliveryMethodErrorsContainer.setVisible(true);
        deliveryMethodErrorsContainer.clear();
        HTMLPanel htmlPanel = new HTMLPanel(
            "<div class=\"error\">" + deliveryTypeLabel + " - " + errorMsg + "</div>");
        deliveryMethodErrorsContainer.add(htmlPanel);
    }

    @Override
    public void hideDeliveryMethodError() {
        deliveryMethodErrorsContainer.clear();
        deliveryMethodErrorsContainer.setVisible(false);
    }

    @Override
    public void notifyBillPaid() {
        Notify.info(orderConstants.billPaidText());
    }

    private void disableSelectedAddressRadioButton() {
        if (selectedAddressRadioButton != null) {
            selectedAddressRadioButton.setValue(false, true);
        }
    }

    private class DeliveryMethodValueChangeHandler implements ValueChangeHandler<Boolean> {
        private DeliveryTypeInfo deliveryTypeInfo;

        private DeliveryMethodValueChangeHandler(DeliveryTypeInfo deliveryTypeInfo) {
            this.deliveryTypeInfo = deliveryTypeInfo;
        }

        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
            if (event.getValue()) {
                disableSelectedAddressRadioButton();
                presenter.onDeliveryMethodSelected(deliveryTypeInfo);
            }
        }
    }

    private class AddressValueChangeHandler implements ValueChangeHandler<Boolean> {
        private Address address;

        private AddressValueChangeHandler(Address address) {
            this.address = address;
        }

        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
            if (event.getValue()) {
                selectedAddressRadioButton = (RadioButton) event.getSource();
                presenter.onDeliveryAddressSelected(address);
            }
        }
    }

    @Override
    public void showBanner(String bannerText) {
        bannerHtml.setHTML(bannerText);
        bannerPanel.setVisible(true);
    }
}
