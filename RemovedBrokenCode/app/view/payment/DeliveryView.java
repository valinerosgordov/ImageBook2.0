package ru.imagebook.client.app.view.payment;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SuggestOracle;

import ru.imagebook.client.app.ctl.payment.DeliveryPresenter;
import ru.imagebook.client.common.service.delivery.PostHouseType;
import ru.imagebook.client.common.service.delivery.SDEKDeliveryType;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DeliveryTypeInfo;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.app.MajorData;
import ru.imagebook.shared.model.app.PickPointData;
import ru.imagebook.shared.model.app.SDEKPackage;


public interface DeliveryView extends IsWidget {
    void setPresenter(DeliveryPresenter presenter);

    void setMajorCityFieldOracle(SuggestOracle majorCityFieldOracle);

    void initDeliveryFieldsPanel(User user);

    void showDeliveryMethods(List<DeliveryTypeInfo> deliveryTypeInfos);

    void showDeliveryForm();

    void showAddressListWithPickupForm(User user);

    void fillPickupFields(Address address);

    void fetchPickupFields(Address address);

    boolean pickupFormComplete();

    boolean pickupFormValid();

    void highlightIncompletePickupFields();

    void highlightInvalidPickupFields();

    void showPickupValidationErrors();

    void showAddressListWithPostHouseForm(User user);

    void fillPostHouseFields(Address address);

    void fetchPostHouseFields(Address address);

    boolean postHouseFormComplete();

    boolean postHouseFormValid();

    void highlightIncompletePostHouseFields();

    void highlightInvalidPostHouseFields();

    void showPostHouseValidationErrors();

    void showMajorCity();

    void alertFormIncomplete();

    void hideMajorCity();

    void resetMajorCity();

    void hideMajorConsAndTimePanel();

    void showMajorCostAndTime(MajorData majorData);

    void hideFields();

    void showAddressListWithMajorForm(User user);

    void fillMajorFields(Address address);

    void fetchMajorFields(Address address);

    void highlightIncompleteMajorFields();

    void highlightInvalidMajorFields();

    void showMajorValidationErrors();

    boolean majorFormComplete();

    boolean majorFormValid();

    void disablePayButton();

    void showProgressIndicator();

    void hideProgressIndicator();

    void hideValidationErrors();

    void hideFormIncompleteLabel();

    void resetErrorFields(Integer deliveryType);

    void showDeliveryTypeNotSelectedMessage();

    void showDeliveryMethodError(String deliveryTypeLabel, String errorMsg);

    void hideDeliveryMethodError();

    void hidePickpointCostAndTime();

    void fillPickpointFields(Address address);

    boolean pickpointFormComplete();

    boolean pickpointFormValid();

    void fetchPickpointFields(Address address, Bill bill);

    void highlightIncompletePickpointFields();

    void highlightInvalidPickpointFields();

    void showPickpointFormValidationErrors();

    void showPickpointWidget(User user);

    void hidePickpointWidget();

    void showPickpointForm(User user);

    void showPickpointCostAndTime(PickPointData pickPointData);

    void fetchPickpointWidgetFields(Bill bill);

    void showSDEKWidget(List<SDEKPackage> sdekPackages);

    void hideSDEKWidget();

    void showSDEKForm(SDEKDeliveryType deliveryType, User user);

    void fillSDEKFields(Address selectedAddress);

    boolean sdekFormComplete();

    boolean sdekFormValid();

    void fetchSDEKFields(Address address);

    void highlightIncompleteSDEKFields();

    void highlightInvalidSDEKFields();

    void showSDEKValidationErrors();

    void showPostDeliveryWarning(PostHouseType postHouseType);

    void hidePostDeliveryWarning();

    void notifyBillPaid();

    void showBanner(String bannerText);
}
