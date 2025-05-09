package ru.imagebook.client.app.view.payment;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by alex on 27.08.2015.
 */
public class PostamateSelectionEvent extends GwtEvent<PostamateSelectionEventHandler> {
    public static Type<PostamateSelectionEventHandler> TYPE = new Type<PostamateSelectionEventHandler>();

    public PostamateSelectionEvent() {
    }

    @Override
    public Type<PostamateSelectionEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PostamateSelectionEventHandler handler) {
        handler.onPostamateSelected(this);
    }
}
