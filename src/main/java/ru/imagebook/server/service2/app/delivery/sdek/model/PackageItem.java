package ru.imagebook.server.service2.app.delivery.sdek.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class PackageItem implements Serializable {
    @XmlAttribute
    private String wareKey;
    @XmlAttribute
    private Integer amount;
    @XmlAttribute
    private String comment;
    @XmlAttribute
    private Float cost;
    @XmlAttribute
    private float payment;
    @XmlAttribute
    private Integer weight;

    public String getWareKey() {
        return wareKey;
    }

    public void setWareKey(String wareKey) {
        this.wareKey = wareKey;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}