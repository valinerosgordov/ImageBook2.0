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
@XmlRootElement(name = "package")
public class DeliveryPackage implements Serializable {
    @XmlAttribute
    private String number;
    @XmlAttribute
    private String barcode;
    @XmlAttribute
    private int weight;
    /**
     * Габариты упаковки. Длина (в сантиметрах)
     */
    @XmlAttribute
    private Integer sizeA;
    /**
     * Габариты упаковки. Ширина (в сантиметрах)
     */
    @XmlAttribute
    private Integer sizeB;
    /**
     * Габариты упаковки. Высота (в сантиметрах)
     */
    @XmlAttribute
    private Integer sizeC;
    @XmlAttribute
    private String comment;

    @XmlElement(name = "item")
    private List<PackageItem> items;

    public DeliveryPackage() {
    }

    public DeliveryPackage(String number) {
        this.number = number;
        this.barcode = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Integer getSizeA() {
        return sizeA;
    }

    public void setSizeA(Integer sizeA) {
        this.sizeA = sizeA;
    }

    public Integer getSizeB() {
        return sizeB;
    }

    public void setSizeB(Integer sizeB) {
        this.sizeB = sizeB;
    }

    public Integer getSizeC() {
        return sizeC;
    }

    public void setSizeC(Integer sizeC) {
        this.sizeC = sizeC;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<PackageItem> getItems() {
        return items;
    }

    public void setItems(List<PackageItem> items) {
        this.items = items;
    }

    public void addItem(PackageItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }
}