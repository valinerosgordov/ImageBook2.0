package ru.imagebook.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface BannerRemoteServiceAsync {
    void getAppBannerText(AsyncCallback<String> callback);

    void getAppPaymentDeliveryText(AsyncCallback<String> callback);

    void getEditorBannerText(AsyncCallback<String> callback);
}