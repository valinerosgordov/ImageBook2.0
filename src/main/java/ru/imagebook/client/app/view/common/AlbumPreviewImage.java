package ru.imagebook.client.app.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import ru.imagebook.client.common.ctl.preview.AlbumPreviewPresenterImpl;
import ru.imagebook.client.common.view.order.OrderViewer;
import ru.imagebook.server.service.flash.PageType;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;


public class AlbumPreviewImage extends Composite {
    private final Order<?> order;
    private final Vendor vendor;

	public AlbumPreviewImage(Order<?> order, Vendor vendor) {
		this.order = order;
        this.vendor = vendor;

		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		Image image = new Image(getAlbumImageUrl(order));
		image.setStyleName("img-thumbnail");
		image.setTitle("Посмотреть альбом");

        Anchor imageAnchor = new Anchor();
        imageAnchor.getElement().appendChild(image.getElement());
        imageAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPreview();
			}
		});
		panel.add(imageAnchor);

        Anchor anchor = new Anchor("Посмотреть альбом");
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showPreview();
            }
        });
        panel.add(anchor);

		initWidget(panel);
	}

	private void showPreview() {
		new AlbumPreviewPresenterImpl(new AlbumPreviewViewImpl())
            .show(order, vendor);
	}

    private String getAlbumImageUrl(Order<?> order) {
		int pageType = ((Album) order.getProduct()).isSeparateCover() ? PageType.FRONT : PageType.NORMAL;
		return GWT.getHostPageBaseURL() + "orderPreviewImage?orderId=" + order.getId() + "&pageType=" + pageType;
    }
}
