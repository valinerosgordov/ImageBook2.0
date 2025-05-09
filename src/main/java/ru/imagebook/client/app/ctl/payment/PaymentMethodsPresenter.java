package ru.imagebook.client.app.ctl.payment;


public interface PaymentMethodsPresenter {
    void onRoboMethodAnchorClicked();

    void onYandexKassaMethodAnchorClicked();

    void generateReceiptPdf(String name, String address);

    void generateReceiptMSWord(String name, String address);
}
