package ru.minogin.pageflip.client;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class PageFlip implements IsWidget {
	public static final int N_INITIAL_PAGES = 3;

	private static int count = 0;

	private final int pageWidth;
	private final int pageHeight;
	private int nPages;
	private boolean hard;
	private PageFlipLoader loader;
	private FlowPanel imagesPanel;

	public PageFlip(int pageWidth, int pageHeight, int nPages, boolean hard, PageFlipLoader loader) {
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		this.nPages = nPages;
		this.hard = hard;
		this.loader = loader;

		imagesPanel = new FlowPanel();
	}

	@Override
	public Widget asWidget() {
		imagesPanel.addStyleName("pageFlip-imagesPanel");

		imagesPanel.addAttachHandler(new Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if (event.isAttached())
					run(imagesPanel);
			}
		});
		return imagesPanel;
	}

	private void run(FlowPanel imagesPanel) {
		String id = "pageflip_" + count;
		imagesPanel.getElement().setId(id);
		count++;

		runPageFlip(id, pageWidth * 2, pageHeight, nPages, hard, N_INITIAL_PAGES);
	}

	private String getImageUrl(int page) {
		return loader.getImageUrl(page);
	}

	private native void runPageFlip(String id, int albumWidth, int albumHeight, int nPages,
			boolean hard, int nInitialPages)
	/*-{
	  var pageFlip = this;
		$wnd.pageFlip(id, albumWidth, albumHeight, nPages, nInitialPages, hard, function(page) {
			return pageFlip.@ru.minogin.pageflip.client.PageFlip::getImageUrl(I)(page); 
		});
	}-*/;

	public void addStyleName(String style) {
		imagesPanel.addStyleName(style);
	}
}
