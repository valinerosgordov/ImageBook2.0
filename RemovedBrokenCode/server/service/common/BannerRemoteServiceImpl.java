package ru.imagebook.server.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.imagebook.client.common.service.BannerRemoteService;
import ru.imagebook.server.service.PropertyService;
import ru.minogin.core.client.text.StringUtil;


@Service
public class BannerRemoteServiceImpl implements BannerRemoteService {

    @Autowired
    private PropertyService propertyService;

    @Override
    public String getAppBannerText() {
        return getBannerText(PropertyService.BANNER_APP_TEXT);
    }

    @Override
    public String getAppPaymentDeliveryText() {
        return getBannerText(PropertyService.BANNER_APP_PAYMENT_DELIVERY_TEXT);
    }

    @Override
    public String getEditorBannerText() {
        return getBannerText(PropertyService.BANNER_EDITOR_TEXT);
    }

    private String getBannerText(String propertyKey) {
        String bannerText = propertyService.getValue(propertyKey);
        return StringUtil.nlToBr(bannerText);
    }
}
