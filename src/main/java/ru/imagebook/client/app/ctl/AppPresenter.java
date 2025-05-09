package ru.imagebook.client.app.ctl;


public interface AppPresenter {
    void onOrderAnchorClicked();

    void onPaymentAnchorClicked();

    void onProcessOrdersAnchorClicked();

    void onSentOrdersAnchorClicked();

    void onProfileAnchorClicked();

    void onEditorAnchorClicked();

    void onSupportAnchorClicked();

    void onBonusStatusAnchorClicked();

    void createBonusStatusRequest(String requestValue);
}
