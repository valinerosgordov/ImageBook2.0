package ru.imagebook.shared.model;

import javax.persistence.Transient;

import ru.minogin.core.client.bean.BaseEntityBean;

public class ProductImage extends BaseEntityBean implements Comparable<ProductImage> {
    public static final String NUMBER = "number";
    public static final String IMAGE = "image";
    public static final String PRODUCT = "product";
    public static final String DESCRIPTION = "description";
    public static final String SOURCE_FILE = "sourceFile";
    public static final String PATH = "path";

    public String getImage() {
        return get(IMAGE);
    }

    public void setImage(String path) {
        set(IMAGE, path);
    }

    public int getNumber() {
        return get(NUMBER);
    }

    public void setNumber(int number) {
        set(NUMBER, number);
    }

    public Product getProduct() {
        return get(PRODUCT);
    }

    public void setProduct(Product product) {
        set(PRODUCT, product);
    }

    public String getDescription() {
        return get(DESCRIPTION);
    }

    public void setDescription(String description) {
        set(DESCRIPTION, description);
    }

    public String getSourceFile() {
        return get(SOURCE_FILE);
    }

    public void setSourceFile(String sourceFile) {
        set(SOURCE_FILE, sourceFile);
    }

    @Transient
    public String getPath() {
        return get(PATH);
    }

    public void setPath(String path) {
        set(PATH, path);
    }

    @Override
    public int compareTo(ProductImage o) {
        return new Integer(this.getNumber()).compareTo(o.getNumber());
    }
}
