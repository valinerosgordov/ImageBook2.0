package ru.imagebook.client.common.view.order;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import ru.imagebook.client.common.util.FlashUtils;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gwt.ui.MessageBox;
import ru.minogin.pageflip.client.PageFlip;
import ru.minogin.pageflip.client.PageFlipLoader;


@Deprecated // TODO remove ?
public class AlbumPreviewViewImpl implements AlbumPreviewView {
    private static final int FLASH_PADDING_BOTTOM = 10;

    private final CommonConstants commonConstants = GWT.create(CommonConstants.class);
    private final PreviewConstants previewConstants = GWT.create(PreviewConstants.class);

    @Override
	public void showPreview(final Order<?> order, final Vendor vendor, AlbumPreviewParams previewParams) {
        final PopupPanel popupPanel = createPreviewModal();
        VerticalPanel contentPanel = (VerticalPanel) popupPanel.getWidget();

        PageFlip flip = new PageFlip(previewParams.getPageWidth(), previewParams.getPageHeight(),
            previewParams.getnPages(), previewParams.isHardOrEverflat(), new PageFlipLoader() {
                @Override
                public String getImageUrl(int page) {
                    return "http://" + vendor.getOnlineEditorUrl() + "/app/albumSheetPreviewImage?albumId="
                        + order.getImportId() + "&i=" + page;
                }
            });
        flip.addStyleName("album-preview-content");
        contentPanel.add(flip);

        // show
		popupPanel.center();
	}

	@Override
	public void showFlashPreview(Order<?> order, final Vendor vendor, final FlashParams flashParams) {
        final PopupPanel popupPanel = createPreviewModal();
        VerticalPanel contentPanel = (VerticalPanel) popupPanel.getWidget();

		String flashDiv = previewConstants.flashDiv(flashParams.getFlashWidth(),
            flashParams.getFlashHeight() + FLASH_PADDING_BOTTOM);
		HTMLPanel htmlPanel = new HTMLPanel(flashDiv);
        htmlPanel.setStyleName("album-preview-content");
		contentPanel.add(htmlPanel);

		contentPanel.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
                if (!event.isAttached()) {
                    return;
                }
				String url = "http://flash." + vendor.getEnglishDomain();
				FlashUtils.showFlash(url, flashParams.getFlashContextUrl(), flashParams.getFlashWidth(),
					flashParams.getFlashHeight(), flashParams.getSessionId());
			}
		});

        // show
		popupPanel.center();
	}

    private PopupPanel createPreviewModal() {
        final PopupPanel panel = new PopupPanel(true);
        panel.setGlassEnabled(true);
        panel.removeStyleName("gwt-PopupPanel");
        panel.addStyleName("album-preview");

        VerticalPanel contentPanel = new VerticalPanel();

        SimplePanel closeButtonPanel = new SimplePanel();
        Button closeButton = new Button("Закрыть", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                panel.hide();
            }
        });
        closeButton.setSize("100px", "30px");
        closeButton.setStyleName("album-preview-close-button");
        closeButtonPanel.add(closeButton);
        contentPanel.add(closeButtonPanel);
        contentPanel.setCellHorizontalAlignment(closeButtonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
        contentPanel.setSpacing(10);

        panel.add(contentPanel);
        return panel;
    }

	@Override
	public void showLoader() {
		AjaxLoader.show();
	}

	@Override
	public void hideLoader() {
		AjaxLoader.hide();
	}

    @Override
    public void alertShowPreviewFailed() {
        new MessageBox(commonConstants.error(), previewConstants.showPreviewFailed(), commonConstants).show();
    }
}
