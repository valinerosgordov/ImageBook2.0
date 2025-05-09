package ru.imagebook.client.admin.view.product;

import com.extjs.gxt.ui.client.data.BaseModel;

import ru.imagebook.shared.model.ProductImage;

public class ProductImageModel extends BaseModel {
    private final ProductImage entity;

    public ProductImageModel(ProductImage entity) {
        this.entity = entity;
        set(ProductImage.NUMBER, entity.getNumber());
        set(ProductImage.SOURCE_FILE, entity.getSourceFile());
        set(ProductImage.PATH, entity.getPath());
    }

    public ProductImage getEntity() {
        return this.entity;
    }
}
