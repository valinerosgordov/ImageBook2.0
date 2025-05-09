package ru.imagebook.server.service2.app.delivery.sdek.model.calculation.response;

import java.util.List;

public class ResultDto {
    private String price;
    private int deliveryPeriodMin;
    private int deliveryPeriodMax;
    private String deliveryDateMin;
    private String deliveryDateMax;
    private int tariffId;
    private double priceByCurrency;
    private String currency;
    private int percentVAT;
    private List<String> services;

    public ResultDto(String price, int deliveryPeriodMin, int deliveryPeriodMax, String deliveryDateMin, String deliveryDateMax, int tariffId, double priceByCurrency, String currency, int percentVAT, List<String> services) {
        this.price = price;
        this.deliveryPeriodMin = deliveryPeriodMin;
        this.deliveryPeriodMax = deliveryPeriodMax;
        this.deliveryDateMin = deliveryDateMin;
        this.deliveryDateMax = deliveryDateMax;
        this.tariffId = tariffId;
        this.priceByCurrency = priceByCurrency;
        this.currency = currency;
        this.percentVAT = percentVAT;
        this.services = services;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getDeliveryPeriodMin() {
        return deliveryPeriodMin;
    }

    public void setDeliveryPeriodMin(int deliveryPeriodMin) {
        this.deliveryPeriodMin = deliveryPeriodMin;
    }

    public int getDeliveryPeriodMax() {
        return deliveryPeriodMax;
    }

    public void setDeliveryPeriodMax(int deliveryPeriodMax) {
        this.deliveryPeriodMax = deliveryPeriodMax;
    }

    public String getDeliveryDateMin() {
        return deliveryDateMin;
    }

    public void setDeliveryDateMin(String deliveryDateMin) {
        this.deliveryDateMin = deliveryDateMin;
    }

    public String getDeliveryDateMax() {
        return deliveryDateMax;
    }

    public void setDeliveryDateMax(String deliveryDateMax) {
        this.deliveryDateMax = deliveryDateMax;
    }

    public int getTariffId() {
        return tariffId;
    }

    public void setTariffId(int tariffId) {
        this.tariffId = tariffId;
    }

    public double getPriceByCurrency() {
        return priceByCurrency;
    }

    public void setPriceByCurrency(double priceByCurrency) {
        this.priceByCurrency = priceByCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPercentVAT() {
        return percentVAT;
    }

    public void setPercentVAT(int percentVAT) {
        this.percentVAT = percentVAT;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }
}
