package ru.imagebook.server.model.calc;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class CalcProductRequest implements Serializable {
    @NotNull
    private Integer productId;
    @NotNull
    private Integer pages;
    @NotNull
    private Integer coverLam;
    @NotNull
    private Integer pageLam;
    @NotNull
    private Integer quantity;
    @NotNull
    private Integer bonusLevel;

    public CalcProductRequest() {
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getCoverLam() {
        return coverLam;
    }

    public Integer getPageLam() {
        return pageLam;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getBonusLevel() {
        return bonusLevel;
    }
}
