package ru.imagebook.server.model.export.request;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 
 */
@XmlRootElement(name = "orders")
public class OrderExpCollection {
    private List<OrderExpItem> orders;

    @XmlElement(name = "order")
    public List<OrderExpItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderExpItem> orders) {
        this.orders = orders;
    }
}