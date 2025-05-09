package ru.imagebook.client.admin.view.product;

/**
 * Created by ksf on 13.01.17.
 */
public interface AlbumPresenter {
    void loadUsers(int offset, int limit, String query);

    void photosButtonClicked();
}
