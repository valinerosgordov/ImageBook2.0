package ru.imagebook.client.common.ctl.preview;

import com.google.gwt.core.client.GWT;

import ru.imagebook.client.common.service.preview.AlbumPreviewRemoteService;
import ru.imagebook.client.common.service.preview.AlbumPreviewRemoteServiceAsync;
import ru.imagebook.client.common.view.order.AlbumPreviewView;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.common.preview.FlashParams;
import ru.minogin.core.client.app.failure.XAsyncCallback;


public class AlbumPreviewPresenterImpl implements AlbumPreviewPresenter {
	private AlbumPreviewRemoteServiceAsync service = GWT.create(AlbumPreviewRemoteService.class);
	private AlbumPreviewView view;

	public AlbumPreviewPresenterImpl(AlbumPreviewView view) {
		this.view = view;
	}

    @Override
	public void show(final Order<?> order, final Vendor vendor) {
		view.showLoader();

//		if (order.isExternalOrder()) {
			// TODO remove

//			<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
//			<album_preview>
//			<albumName></albumName>
//			<hardOrEverflat>true</hardOrEverflat>
//			<pageHeight>328</pageHeight>
//			<pageWidth>480</pageWidth>
//			<nPages>28</nPages>
//			</album_preview>

//			AlbumPreviewParams previewParams = new AlbumPreviewParams();
//			previewParams.setPageHeight(328);
//			previewParams.setPageWidth(480);
//			previewParams.setnPages(28);
//			previewParams.setHardOrEverflat(true);
//
//			view.hideLoader();
//			view.showPreview(order, vendor, previewParams);

//			service.updatePreview(order.getImportId(), new XAsyncCallback<AlbumPreviewParams>() {
//				@Override
//				public void onSuccess(AlbumPreviewParams previewParams) {
//					view.hideLoader();
//					view.showPreview(order, vendor, previewParams);
//				}
//
//				@Override
//				public void onFailure(Throwable caught) {
//                    alertShowPreviewFailed();
//				}
//			});
//		} else {
		service.getFlashPreviewParams(order.getId(), new XAsyncCallback<FlashParams>() {
			@Override
			public void onSuccess(FlashParams flashParams) {
				view.hideLoader();
				view.showFlashPreview(order, vendor, flashParams);
			}

			@Override
			public void onFailure(Throwable caught) {
				alertShowPreviewFailed();
			}
		});
//		}
	}

    private void alertShowPreviewFailed() {
        view.hideLoader();
        view.alertShowPreviewFailed();
    }
}
