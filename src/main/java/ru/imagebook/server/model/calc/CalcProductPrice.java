package ru.imagebook.server.model.calc;

import java.io.Serializable;

public class CalcProductPrice implements Serializable {
    private final Integer price;
    private final Integer cost;
    private final Integer discount;
    private final Integer total;
    private final Integer totalPrice;

    public CalcProductPrice(Integer price, Integer cost, Integer discount, Integer total, Integer totalPrice) {
        this.price = price;
        this.cost = cost;
        this.discount = discount;
        this.total = total;
        this.totalPrice = totalPrice;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getCost() {
        return cost;
    }

    public Integer getDiscount() {
        return discount;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
