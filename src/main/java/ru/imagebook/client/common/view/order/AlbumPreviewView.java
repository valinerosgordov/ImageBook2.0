package ru.imagebook.client.common.view.order;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;


public interface AlbumPreviewView {
	// TODO remove
	@Deprecated
	void showPreview(Order<?> order, Vendor vendor, AlbumPreviewParams previewParams);

	void showFlashPreview(Order<?> order, Vendor vendor, FlashParams flashParams);

	void showLoader();

	void hideLoader();

    void alertShowPreviewFailed();
}