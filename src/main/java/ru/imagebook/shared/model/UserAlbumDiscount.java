package ru.imagebook.shared.model;

import ru.minogin.core.client.bean.BaseEntityBean;

/**
 * Created by user on 29.03.2017.
 */
public class UserAlbumDiscount extends BaseEntityBean {
    public static final String USER = "user";
    public static final String PRODUCT = "product";
    public static final String DISCOUNT_PC = "discountPc";

    public UserAlbumDiscount() {
    }

    public UserAlbumDiscount(Product product, int discountPc) {
        setProduct(product);
        setDiscountPc(discountPc);
    }

    public User getUser() {
        return get(USER);
    }

    public void setUser(User user) {
        set(USER, user);
    }

    public Product getProduct() {
        return get(PRODUCT);
    }

    public void setProduct(Product product) {
        set(PRODUCT, product);
    }

    public Integer getDiscountPc() {
        return get(DISCOUNT_PC);
    }

    public void setDiscountPc(int discountPc) {
        set(DISCOUNT_PC, discountPc);
    }
}
