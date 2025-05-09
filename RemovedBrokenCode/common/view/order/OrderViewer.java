package ru.imagebook.client.common.view.order;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.common.service.order.FlashViewer;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.shared.model.Order;


@Singleton
public class OrderViewer {
    @Inject
    protected VendorService vendorService;

    @Inject
    protected FlashViewer flashViewer;

    public void showOrderPreview(Order<?> order, String sessionId) {
        if (!order.isExternalOrder() && order.getFlash() != null) {
            Window.open(order.getFlash(), "_blank", "");
        } else {
            Window.open(flashViewer.getUrl(order.getId(), sessionId), "_blank", "");
        }
    }
}
