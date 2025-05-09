package ru.imagebook.client.common.service.preview;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;


@RemoteServiceRelativePath("gwt.remoteService")
public interface AlbumPreviewRemoteService extends RemoteService {
	AlbumPreviewParams updatePreview(int importId);

	FlashParams getFlashPreviewParams(int orderId);
}
