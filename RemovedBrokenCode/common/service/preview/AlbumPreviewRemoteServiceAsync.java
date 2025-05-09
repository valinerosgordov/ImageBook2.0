package ru.imagebook.client.common.service.preview;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;


public interface AlbumPreviewRemoteServiceAsync {
	void updatePreview(int importId, AsyncCallback<AlbumPreviewParams> callback);

	void getFlashPreviewParams(int orderId, AsyncCallback<FlashParams> callback);
}
