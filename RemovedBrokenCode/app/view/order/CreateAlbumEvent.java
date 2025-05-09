package ru.imagebook.client.app.view.order;

import com.google.gwt.event.shared.GwtEvent;

public class CreateAlbumEvent extends GwtEvent<CreateAlbumEventHandler> {
    public static Type<CreateAlbumEventHandler> TYPE = new Type<CreateAlbumEventHandler>();

    private final Integer productId;
    private final Integer pageCount;

    public CreateAlbumEvent(Integer productId, Integer pageCount) {
        this.productId = productId;
        this.pageCount = pageCount;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    @Override
    public Type<CreateAlbumEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CreateAlbumEventHandler handler) {
        handler.onCreateAlbum(this);
    }
}
