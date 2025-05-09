package ru.imagebook.server.service.preview;

import java.io.Writer;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;


public interface PreviewService {
    String renderOrderFlipper(Order<?> order, Vendor vendor, AlbumPreviewParams previewParams);

    String renderOrderFlipperByPublishedCode(int publishCode, Vendor vendor, AlbumPreviewParams previewParams);

    String renderOrderFlashFlipper(Order<?> order, Vendor vendor, FlashParams flashParams);

    void showOrderPreview(int orderId, int userId, Writer writer);

    void showOrderPreviewExt(String number, Writer writer);

    void showPageNotFound(Writer writer);
}
