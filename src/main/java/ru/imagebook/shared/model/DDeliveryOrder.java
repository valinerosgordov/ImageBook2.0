package ru.imagebook.shared.model;

import ru.minogin.core.shared.model.BaseEntityImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Sergey Boykov
 */
@Deprecated
@Table(name = "ddelivery_module_order")
@Entity
public class DDeliveryOrder extends BaseEntityImpl {

    private Integer imagebookBillId;
    private Integer imagebookOrderId;
    private Integer widthCm;
    private Integer heightCm;
    private Integer lengthCm;
    private BigDecimal weightKg;
    private BigDecimal assessedPrice;
    private Integer quantity;
    private String description;

    public Integer getImagebookBillId() {
        return imagebookBillId;
    }

    public void setImagebookBillId(Integer imagebookBillId) {
        this.imagebookBillId = imagebookBillId;
    }

    public Integer getImagebookOrderId() {
        return imagebookOrderId;
    }

    public void setImagebookOrderId(Integer imagebookOrderId) {
        this.imagebookOrderId = imagebookOrderId;
    }

    public Integer getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(Integer widthCm) {
        this.widthCm = widthCm;
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public Integer getLengthCm() {
        return lengthCm;
    }

    public void setLengthCm(Integer lengthCm) {
        this.lengthCm = lengthCm;
    }

    @Column(columnDefinition = "decimal(19,3)")
    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getAssessedPrice() {
        return assessedPrice;
    }

    public void setAssessedPrice(BigDecimal assessedPrice) {
        this.assessedPrice = assessedPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
