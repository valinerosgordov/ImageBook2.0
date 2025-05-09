package ru.imagebook.server.service.pickbook;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ru.imagebook.server.config.PickbookClientConfig;
import ru.imagebook.server.model.importing.XUser;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.minogin.core.client.exception.Exceptions;

/**

 * @since 03.12.2014
 */
@Component
public class PickbookClientImpl implements PickbookClient {
    private static final Logger LOG = Logger.getLogger(PickbookClientImpl.class);

    @Autowired
    private PickbookClientConfig config;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void editAlbum(int albumId) {
        makePost(config.getEditAlbumUrl(), null, String.class, albumId);
    }

    @Override
    public void copyAlbum(int albumId) {
        makePost(config.getCopyAlbumUrl(), null, String.class, albumId);
    }

    @Override
    public void processAlbum(int albumId) {
        makePost(config.getProcessAlbumUrl(), null, String.class, albumId);
    }

    @Override
    public AlbumPreviewParams previewAlbum(int albumId) {
        return makePost(config.getPreviewAlbumUrl(), null, AlbumPreviewParams.class, albumId);
    }

    @Override
    public void publishAlbum(int albumId, int code) {
        makePost(config.getPublishAlbumUrl(), null, String.class, albumId, code);
    }

    @Override
    public void cleanAlbum(int albumId) {
        makePost(config.getCleanAlbumUrl(), null, String.class, albumId);
    }

    @Override
    public void createUser(User user) {
        XUser xUser = XUser.create(user);
        xUser.setWholesaler(user.isPhotographer() && user.getLevel() == 9);
        makePost(config.getCreateUserUrl(), xUser, XUser.class);
    }

    @Override
    public void updateUsername(User user, String anonymousUserName) {
        XUser xUser = XUser.create(user);
        xUser.setPrevUsername(anonymousUserName);
        makePost(config.getAttachUserEmailUrl(), xUser, XUser.class);
    }

    @Override
    public void moveAnonymousAlbums(User user, String anonymousUserName) {
        XUser xUser = XUser.create(user);
        xUser.setPrevUsername(anonymousUserName);
        makePost(config.getMoveUserAlbumsUrl(), xUser, XUser.class);
    }

    private <T> T makePost(String url, T request, Class<T> responseType, Object... uriVariables) {
        try {
            ResponseEntity<T> response = restTemplate.postForEntity(url, request, responseType, uriVariables);
            if (LOG.isDebugEnabled()) {
                LOG.debug("response:\n" + response);
            }
            return response.getBody();
        } catch (Exception e) {
            LOG.error("Request to Pickbook failed: " + e.getMessage());
            Exceptions.rethrow(e);
            return null;
        }
    }
}
