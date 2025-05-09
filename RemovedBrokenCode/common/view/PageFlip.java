package ru.imagebook.client.common.view;

/**
 * Created by rifat on 20.02.17.
 */
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.google.gwt.dom.client.Style;
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
        FlowPanel zoomViewport=new FlowPanel();
        zoomViewport.getElement().setId("zoom-viewport");
        imagesPanel.add(zoomViewport);

        FlowPanel container=new FlowPanel();
        container.getElement().addClassName("book-container");
        zoomViewport.add(container);

        final FlowPanel book=new FlowPanel();
        book.getElement().addClassName("pageFlip-imagesPanel");
        container.add(book);

        addButtonDiv(book, "next-button");
        addButtonDiv(book, "previous-button");
        addButtonDiv(book, "top-left-button");
        addButtonDiv(book, "top-right-button");
        addButtonDiv(book, "bottom-left-button");
        addButtonDiv(book, "bottom-right-button");

        FlowPanel thumbnails=new FlowPanel();
        thumbnails.getElement().setId("thumbnailsPage");
        thumbnails.getElement().addClassName("thumbnails");
        thumbnails.getElement().addClassName("thumbnails-lk");

        imagesPanel.add(thumbnails);

        imagesPanel.addAttachHandler(new Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached())
                    run(book);
            }
        });

        return imagesPanel;
    }

    private void addButtonDiv(FlowPanel panel, String className){
        FlowPanel flowPanel=new FlowPanel();
        flowPanel.getElement().addClassName(className);
        flowPanel.getElement().setAttribute("ignore","1");
        panel.add(flowPanel);
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

    private String getLargeImageUrl(int page) {
        return loader.getLargeImageUrl(page);
    }

    private native void runPageFlip(String id, int albumWidth, int albumHeight, int nPages,
                                    boolean hard, int nInitialPages)
	/*-{
        var pageFlip = this;
        $wnd.pageFlip(id, albumWidth, albumHeight, nPages, nInitialPages, hard,
            function (page) {
                return pageFlip.@ru.imagebook.client.common.view.PageFlip::getImageUrl(I)(page);
            },
            function (page) {
                return pageFlip.@ru.imagebook.client.common.view.PageFlip::getLargeImageUrl(I)(page);
            });
    }-*/;

    public void addStyleName(String style) {
        imagesPanel.addStyleName(style);
    }
}
