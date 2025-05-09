package ru.imagebook.server.service2.app.delivery.sdek.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class DeliveryRecipientCostAdv implements Serializable {
    @XmlAttribute
    private Integer threshold = Integer.MAX_VALUE;
    @XmlAttribute
    private Float sum = 100f;

    public DeliveryRecipientCostAdv() {
    }

    public DeliveryRecipientCostAdv(Integer threshold, Float sum) {
        this.threshold = threshold;
        this.sum = sum;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public Float getSum() {
        return sum;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }
}
