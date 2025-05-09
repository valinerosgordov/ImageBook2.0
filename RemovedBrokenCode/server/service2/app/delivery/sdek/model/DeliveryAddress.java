package ru.imagebook.server.service2.app.delivery.sdek.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "address")
public class DeliveryAddress implements Serializable {
    @XmlAttribute
    private String street;
    @XmlAttribute
    private String house;
    @XmlAttribute
    private String flat;
    @XmlAttribute
    private String pvzCode;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getPvzCode() {
        return pvzCode;
    }

    public void setPvzCode(String pvzCode) {
        this.pvzCode = pvzCode;
    }
}