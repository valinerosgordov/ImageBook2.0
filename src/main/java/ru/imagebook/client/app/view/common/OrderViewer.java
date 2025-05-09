package ru.imagebook.client.app.view.common;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;


@Singleton
public class OrderViewer extends ru.imagebook.client.common.view.order.OrderViewer {
    public Widget createOrderPreviewWidget(Order<?> order) {
        VerticalPanel panel = new VerticalPanel();
        panel.addStyleName("no-border");

        Vendor vendor = vendorService.getVendor();
        AlbumPreviewImage albumPreviewImage = new AlbumPreviewImage(order, vendor);
        panel.add(albumPreviewImage);
        panel.setCellHorizontalAlignment(albumPreviewImage, HasHorizontalAlignment.ALIGN_CENTER);

        return panel;
    }
}
