package ru.imagebook.client.common.ctl.preview;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;

public interface AlbumPreviewPresenter {
    void show(Order<?> order, Vendor vendor);
}
