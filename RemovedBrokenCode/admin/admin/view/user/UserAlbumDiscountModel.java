package ru.imagebook.client.admin.view.user;

import com.extjs.gxt.ui.client.data.BaseModel;

import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.UserAlbumDiscount;
import ru.minogin.core.client.gxt.form.ObjectModel;

/**
 * Created by alex on 29.03.2017.
 */
public class UserAlbumDiscountModel extends BaseModel {
    private final UserAlbumDiscount albumDiscount;

    public UserAlbumDiscountModel(UserAlbumDiscount albumDiscount, final String locale) {
        this.albumDiscount = albumDiscount;
        setProduct(albumDiscount.getProduct(), locale);
        set(UserAlbumDiscount.DISCOUNT_PC, albumDiscount.getDiscountPc());
    }

    public Product getProduct() {
        ObjectModel<Product> productObjectModel = get(UserAlbumDiscount.PRODUCT);
        return productObjectModel != null ? productObjectModel.getObject() : null;
    }

    public void setProduct(Product product, final String locale) {
        set(UserAlbumDiscount.PRODUCT,
            product != null ? new ObjectModel<Product>(product, product.getName().getNonEmptyValue(locale)) : null);
    }

    public int getDiscountPc() {
        return (Integer) get(UserAlbumDiscount.DISCOUNT_PC);
    }

    public UserAlbumDiscount getAlbumDiscount() {
        return albumDiscount;
    }
}
