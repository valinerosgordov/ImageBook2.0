package ru.imagebook.client.admin.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.admin.ProductsResult;

/**
 * Created by rifat on 11.01.17.
 */
@RemoteServiceRelativePath("user.remoteService")
public interface UserRemoteService extends RemoteService {
    ProductsResult loadProducts(int offset, int limit, String query);
}
