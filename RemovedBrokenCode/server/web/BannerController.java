package ru.imagebook.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.imagebook.server.service.PropertyService;


@Controller
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    protected PropertyService propertyService;

    @RequestMapping("/test")
    public String test() {
        return "admin2/bannerTest";
    }

    @ResponseBody
    @RequestMapping("/initApp")
    public InitAppBean initApp() {
        InitAppBean initAppBean = new InitAppBean();
        initAppBean.setAppBannerText(propertyService.getValue(PropertyService.BANNER_APP_TEXT));
        initAppBean.setAppPaymentDeliveryBannerText(propertyService.getValue(PropertyService.BANNER_APP_PAYMENT_DELIVERY_TEXT));
        initAppBean.setEditorBannerText(propertyService.getValue(PropertyService.BANNER_EDITOR_TEXT));
        return initAppBean;
    }

    protected static class InitAppBean {
        private String appBannerText;
        private String appPaymentDeliveryBannerText;
        private String editorBannerText;

        public String getAppBannerText() {
            return appBannerText;
        }

        public void setAppBannerText(String appBannerText) {
            this.appBannerText = appBannerText;
        }

        public String getAppPaymentDeliveryBannerText() {
            return appPaymentDeliveryBannerText;
        }

        public void setAppPaymentDeliveryBannerText(String appPaymentDeliveryBannerText) {
            this.appPaymentDeliveryBannerText = appPaymentDeliveryBannerText;
        }

        public String getEditorBannerText() {
            return editorBannerText;
        }

        public void setEditorBannerText(String editorBannerText) {
            this.editorBannerText = editorBannerText;
        }
    }
}
