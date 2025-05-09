package ru.imagebook.shared.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ru.minogin.core.shared.model.BaseEntityImpl;

/**
 * Created by rifat on 19.01.17.
 */
@Entity
@Table(name = "delivery_discount", uniqueConstraints = {@UniqueConstraint(columnNames = {"deliveryType", "sum"})})
public class DeliveryDiscount extends BaseEntityImpl {
    public static final String DELIVERY_TYPE = "deliveryType";
    public static final String SUM = "sum";
    public static final String DISCOUNT_PC = "discountPc";

    private Integer deliveryType;
    private Integer sum;
    private Integer discountPc;

    public DeliveryDiscount() {
    }

    public DeliveryDiscount(Integer deliveryType, Integer sum, Integer discountPc) {
        this.deliveryType = deliveryType;
        this.sum = sum;
        this.discountPc = discountPc;
    }

    @Column(nullable = false)
    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Column(nullable = false)
    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    @Column(nullable = false)
    public Integer getDiscountPc() {
        return discountPc;
    }

    public void setDiscountPc(Integer discountPc) {
        this.discountPc = discountPc;
    }
}
