package ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request;

import java.time.LocalDate;
import java.util.List;

public class CalculatePriceRequest {
    private String version = "1.0";
    private String dateExecute = LocalDate.now().toString();
    private int senderCityId;
    private int receiverCityId;
    private int widget = 1;
    private List<TariffDto> tariffList;
    private String authLogin;
    private String secure;
    private List<GoodDto> goods;

    public CalculatePriceRequest() {
    }

    public CalculatePriceRequest(String version, String dateExecute, int senderCityId, int receiverCityId, int widget, List<TariffDto> tariffList, String authLogin, String secure, List<GoodDto> goods) {
        this.version = version;
        this.dateExecute = dateExecute;
        this.senderCityId = senderCityId;
        this.receiverCityId = receiverCityId;
        this.widget = widget;
        this.tariffList = tariffList;
        this.authLogin = authLogin;
        this.secure = secure;
        this.goods = goods;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDateExecute() {
        return dateExecute;
    }

    public void setDateExecute(String dateExecute) {
        this.dateExecute = dateExecute;
    }

    public int getSenderCityId() {
        return senderCityId;
    }

    public void setSenderCityId(int senderCityId) {
        this.senderCityId = senderCityId;
    }

    public int getReceiverCityId() {
        return receiverCityId;
    }

    public void setReceiverCityId(int receiverCityId) {
        this.receiverCityId = receiverCityId;
    }

    public int getWidget() {
        return widget;
    }

    public void setWidget(int widget) {
        this.widget = widget;
    }

    public List<TariffDto> getTariffList() {
        return tariffList;
    }

    public void setTariffList(List<TariffDto> tariffList) {
        this.tariffList = tariffList;
    }

    public String getAuthLogin() {
        return authLogin;
    }

    public void setAuthLogin(String authLogin) {
        this.authLogin = authLogin;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(String secure) {
        this.secure = secure;
    }

    public List<GoodDto> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodDto> goods) {
        this.goods = goods;
    }
}
