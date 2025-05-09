package ru.imagebook.server.service2.app.delivery.sdek.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class DeliveryResponse implements Serializable {
    @XmlElement(name = "Order")
    private List<ResponseOrder> orders;

    public List<ResponseOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ResponseOrder> orders) {
        this.orders = orders;
    }
}