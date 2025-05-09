package ru.imagebook.shared.model.admin;

import java.io.Serializable;
import java.util.List;

import ru.imagebook.shared.model.Product;

/**
 * Created by rifat on 12.01.17.
 */
public class ProductsResult implements Serializable {
    private List<Product> products;
    private long total;

    public ProductsResult() {
    }

    public ProductsResult(List<Product> products, long total) {
        this.products = products;
        this.total = total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public long getTotal() {
        return total;
    }
}
