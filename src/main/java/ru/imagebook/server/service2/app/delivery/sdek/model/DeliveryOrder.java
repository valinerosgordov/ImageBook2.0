package ru.imagebook.server.service2.app.delivery.sdek.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "order")
public class DeliveryOrder implements Serializable {
    @XmlAttribute
    private String number;
    @XmlAttribute
    private String clientSide = "SENDER";
    @XmlAttribute
    private Integer sendCityCode;
    @XmlAttribute
    private String sellerName;
    @XmlAttribute
    private Integer recCityCode;
    @XmlAttribute
    private String recipientName;
    @XmlAttribute
    private String phone;
    @XmlAttribute
    private Integer tariffTypeCode;

    @XmlElement
    private DeliveryAddress address;
    @XmlElement(name = "package")
    private List<DeliveryPackage> deliveryPackages;
    @XmlElement
    private DeliveryRecipientCostAdv deliveryRecipientCostAdv;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClientSide() {
        return clientSide;
    }

    public void setClientSide(String clientSide) {
        this.clientSide = clientSide;
    }

    public Integer getSendCityCode() {
        return sendCityCode;
    }

    public void setSendCityCode(Integer sendCityCode) {
        this.sendCityCode = sendCityCode;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getRecCityCode() {
        return recCityCode;
    }

    public void setRecCityCode(Integer recCityCode) {
        this.recCityCode = recCityCode;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTariffTypeCode() {
        return tariffTypeCode;
    }

    public void setTariffTypeCode(Integer tariffTypeCode) {
        this.tariffTypeCode = tariffTypeCode;
    }

    public DeliveryAddress getAddress() {
        return address;
    }

    public void setAddress(DeliveryAddress address) {
        this.address = address;
    }

    public List<DeliveryPackage> getDeliveryPackages() {
        return deliveryPackages;
    }

    public void setDeliveryPackages(List<DeliveryPackage> deliveryPackages) {
        this.deliveryPackages = deliveryPackages;
    }

    public void addDeliveryPackage(DeliveryPackage deliveryPackage) {
        if (this.deliveryPackages == null) {
            this.deliveryPackages = new ArrayList<>();
        }
        this.deliveryPackages.add(deliveryPackage);
    }

    public DeliveryRecipientCostAdv getDeliveryRecipientCostAdv() {
        return deliveryRecipientCostAdv;
    }

    public void setDeliveryRecipientCostAdv(DeliveryRecipientCostAdv deliveryRecipientCostAdv) {
        this.deliveryRecipientCostAdv = deliveryRecipientCostAdv;
    }
}