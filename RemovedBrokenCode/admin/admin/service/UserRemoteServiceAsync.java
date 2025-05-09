package ru.imagebook.client.admin.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.admin.ProductsResult;

/**
 * Created by rifat on 11.01.17.
 */
public interface UserRemoteServiceAsync {
    void loadProducts(int offset, int limit, String query, AsyncCallback<ProductsResult> callback);
}
