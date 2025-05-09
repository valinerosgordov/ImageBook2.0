package ru.imagebook.client.admin.view.product;

public interface ProductImagePresenter {
    void photosLoaded(int offset, int limit);

    void setProductId(Integer productId);

    void addEditFormSave();

    void photoSelected();

    void show(String productName);

    void onHide();

    void deleteButtonClicked();

    void deletionConfirmed();
}
