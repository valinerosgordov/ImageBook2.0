package ru.imagebook.client.admin.view.product;

import java.util.List;

import ru.imagebook.shared.model.ProductImage;

public interface ProductImageView {
    void show(String productName);
    void setPresenter(ProductImagePresenter presenter);
    void showPhotos(List<ProductImage> photos, int offset, long total);
    void setFormValues(ProductImage photo);
    ProductImage getSelectedPhoto();
    List<ProductImage> getSelectedPhotos();
    void reload();
    void fetch(ProductImage image);
    void hide();
    void confirmDelete();
    void clearFields();
    void updatePhoto(String path);
    void emptySelection();
}
