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
@XmlRootElement
public class DeliveryRequest implements Serializable {
    @XmlAttribute
    private String account;
    @XmlAttribute
    private String date;
    @XmlAttribute
    private String number;
    @XmlAttribute
    private Integer orderCount;
    @XmlAttribute
    private String secure;
    @XmlElement(name = "order")
    private List<DeliveryOrder> orders;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(String secure) {
        this.secure = secure;
    }

    public List<DeliveryOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DeliveryOrder> orders) {
        this.orders = orders;
    }

    public void addOrder(DeliveryOrder deliveryOrder) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        this.orders.add(deliveryOrder);
    }
}