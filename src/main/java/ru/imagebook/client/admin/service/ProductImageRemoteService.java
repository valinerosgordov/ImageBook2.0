package ru.imagebook.client.admin.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductImage;
import ru.minogin.core.client.gxt.grid.LoadResult;

@RemoteServiceRelativePath("productImage.remoteService")
public interface ProductImageRemoteService extends RemoteService {
    LoadResult<ProductImage> loadPhotos(int productId, int offset, int limit);

    List<ProductImage> loadPhotos(int productId);

    void addPhoto(int productId, ProductImage image);

    void updateImage(ProductImage image);

    String getPhotoPath(ProductImage image);

    String getProductTypePhotoPath(Integer productType);

    void deletePhotos(List<Integer> productIds);
}
