package ru.imagebook.client.admin.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductImage;
import ru.minogin.core.client.gxt.grid.LoadResult;

public interface ProductImageRemoteServiceAsync {
    void loadPhotos(int productId, int offset, int limit, AsyncCallback<LoadResult<ProductImage>> callback);

    // TODO move to separate interface
    @Deprecated
    void loadPhotos(int productId, AsyncCallback<List<ProductImage>> async);

    void addPhoto(int productId, ProductImage image, AsyncCallback<Void> async);

    void updateImage(ProductImage image, AsyncCallback<Void> async);

    void getPhotoPath(ProductImage images, AsyncCallback<String> async);

    // TODO move to separate interface
    @Deprecated
    void getProductTypePhotoPath(Integer productType, AsyncCallback<String> async);

    void deletePhotos(List<Integer> ids, AsyncCallback<Void> async);
}
