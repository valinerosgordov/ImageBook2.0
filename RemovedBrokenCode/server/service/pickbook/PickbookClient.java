package ru.imagebook.server.service.pickbook;

import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;

/**

 * @since 03.12.2014
 */
public interface PickbookClient {
    void editAlbum(int albumId);

    void copyAlbum(int albumId);

    void processAlbum(int albumId);

    AlbumPreviewParams previewAlbum(int albumId);

    void publishAlbum(int albumId, int code);

    void cleanAlbum(int albumId);

    void createUser(User user);

    void updateUsername(User user, String anonymousUserName);

    void moveAnonymousAlbums(User user, String anonymousUserName);
}
