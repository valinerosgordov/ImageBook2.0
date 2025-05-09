package ru.imagebook.client.admin.view.user;

import ru.imagebook.shared.model.Product;
import ru.minogin.core.client.gxt.form.ObjectFieldCallback;

/**
 * Created by rifat on 11.01.17.
 */
public interface UserPresenter {
    void loadProducts(int offset, int limit, String query, ObjectFieldCallback<Product> productCallback);
}