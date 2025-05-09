package ru.imagebook.client.app.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.common.view.PageFlip;
import ru.imagebook.client.common.view.PageFlipLoader;
import ru.imagebook.client.common.view.order.AjaxLoader;
import ru.imagebook.client.common.view.order.AlbumPreviewView;
import ru.imagebook.client.common.view.order.PreviewConstants;
import ru.imagebook.server.service.flash.PageSize;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;
import ru.imagebook.shared.model.common.preview.PreviewUtils;



public class AlbumPreviewViewImpl implements AlbumPreviewView {
    private static final String ALBUM_PREVIEW_MODAL_ID = "albumPreview";
    private static final int BORDER_SIZE = 1;

    private final PreviewConstants previewConstants = GWT.create(PreviewConstants.class);

    @Override
	public void showPreview(final Order<?> order, final Vendor vendor, AlbumPreviewParams previewParams) {
        PageFlip flip = new PageFlip(previewParams.getPageWidth(), previewParams.getPageHeight(),
            previewParams.getnPages(), previewParams.isHardOrEverflat(), new PageFlipLoader() {
                @Override
                public String getImageUrl(int page) {
                    return "http://online.imagebook.ru/app/albumPageImage?code=1&i=" + page;
//                    return "http://" + vendor.getOnlineEditorUrl() + "/app/albumSheetPreviewImage?albumId="
//                        + order.getImportId() + "&i=" + page;
                }

                @Override
                public String getLargeImageUrl(int page) {
                    return null;
                }
            });

        int width = (previewParams.getPageWidth() + BORDER_SIZE) * 2 + 50;
        int height = previewParams.getPageHeight() + 110;

        PreviewModalDialog previewModal = createPreviewModal(order.getNumber(), width, height, flip.asWidget());
        showPreviewModal(previewModal);
	}

    @Override
    public void showFlashPreview(final Order<?> order, final Vendor vendor, final FlashParams flashParams) {
        Album album = (Album) order.getProduct();
        final boolean isSeparateCover = ((Album) order.getProduct()).isSeparateCover();

        PageFlip flip = new PageFlip(flashParams.getFlashWidth()/2-20, flashParams.getFlashHeight()-79,
                flashParams.getnPages(), album.isHardOrEverflat(), new PageFlipLoader() {
            @Override
            public String getImageUrl(int page) {
                //return getImageUrl(page, PageSize.NORMAL);
                return PreviewUtils.getImageUrl(page, PageSize.NORMAL, flashParams.getnPages(), isSeparateCover,
                    flashParams.getSessionId(), vendor);
            }

            @Override
            public String getLargeImageUrl(int page) {
//                return getImageUrl(page, PageSize.LARGE);
                return PreviewUtils.getImageUrl(page, PageSize.LARGE, flashParams.getnPages(), isSeparateCover,
                    flashParams.getSessionId(), vendor);
            }

//            private String getImageUrl(int page, int pageSize) {
//                String a = flashParams.getSessionId();
//                String b= String.valueOf(PageType.NORMAL);
//                String c=String.valueOf(pageSize);
//                String d=String.valueOf(page);
//                boolean isSeparateCover=((Album) order.getProduct()).isSeparateCover();
//                int nPages=flashParams.getnPages();
//                if(isSeparateCover) {
//                    if (page == 1 ||page==2) {
//                        b = String.valueOf(PageType.FRONT);
//                    } else if(page==(nPages-1)||page==nPages){
//                        b = String.valueOf(PageType.BACK);
//                        if(page==nPages){
//                            d="2";
//                        } else{
//                            d="1";
//                        }
//                    } else {
//                        d=String.valueOf(page-2);
//                    }
//                }
//                String url = "http://127.0.0.1:8888/flash/image?a=" + a + "&b=" + b + "&c=" + c + "&d=" + d;
//                return url;
//            }
        });

        int width = flashParams.getFlashWidth() + BORDER_SIZE+45;
        int height = flashParams.getFlashHeight()+31;

        PreviewModalDialog previewModal = createPreviewModal(order.getNumber(), width, height, flip.asWidget());
        showPreviewModal(previewModal);
    }

    private PreviewModalDialog createPreviewModal(String orderNumber, int width, int height, Widget bodyWidget) {
        final PreviewModalDialog previewModal = new PreviewModalDialog(ALBUM_PREVIEW_MODAL_ID);
        previewModal.addHeader(new HTML(previewConstants.previewTitle(orderNumber)));

        if (width > 0) {
            previewModal.setModalWidth(width + "px");
        }
        if (height > 0) {
            previewModal.setModalHeight(height + "px");
        }

        previewModal.addBody(bodyWidget);

        previewModal.setCancelButtonTitle(previewConstants.close());
        previewModal.setCancelButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                previewModal.hide();
            }
        });

        // FIXME hack
        previewModal.setCancelButtonStyle("position: relative");

        return previewModal;
    }

    private void showPreviewModal(PreviewModalDialog previewModal) {
        replacePreviewModal(previewModal);
        previewModal.show();
    }

    private void replacePreviewModal(PreviewModalDialog previewModal) {
        Element previewEl = DOM.getElementById(ALBUM_PREVIEW_MODAL_ID);
        if (previewEl != null) {
            previewEl.getParentElement().removeChild(previewEl);
        }
        RootPanel.get().add(previewModal);
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
        Notify.error(previewConstants.showPreviewFailed());
    }
}
