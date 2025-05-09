package ru.imagebook.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("banner.remoteService")
public interface BannerRemoteService extends RemoteService {
    String getAppBannerText();

    String getAppPaymentDeliveryText();

    String getEditorBannerText();
}