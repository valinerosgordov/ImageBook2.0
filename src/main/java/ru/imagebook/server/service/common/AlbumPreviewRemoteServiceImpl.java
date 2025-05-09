package ru.imagebook.server.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.imagebook.client.common.service.preview.AlbumPreviewRemoteService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.flash.FlashService;
import ru.imagebook.server.service.pickbook.PickbookClient;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;


@Service
public class AlbumPreviewRemoteServiceImpl implements AlbumPreviewRemoteService {
    @Autowired
    private PickbookClient pickbookClient;

    @Autowired
    private FlashService flashService;

    @Autowired
    private AuthService authService;

    @Override
    public AlbumPreviewParams updatePreview(int importId) {
        return pickbookClient.previewAlbum(importId);
    }

    @Override
    public FlashParams getFlashPreviewParams(int orderId) {
        int userId = authService.getCurrentUserId();
        return flashService.getFlashPreviewParams(orderId, userId);
    }
}
