package ru.imagebook.client.app.view.personal.email;

import com.google.gwt.event.shared.GwtEvent;


public class DeleteEmailEvent extends GwtEvent<DeleteEmailEventHandler> {
    public static Type<DeleteEmailEventHandler> TYPE = new Type<DeleteEmailEventHandler>();

    private final Integer emailId;

    public DeleteEmailEvent(Integer emailId) {
        this.emailId = emailId;
    }

    public Integer getEmailId() {
        return emailId;
    }

    @Override
    public Type<DeleteEmailEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DeleteEmailEventHandler handler) {
        handler.onDeleteEmail(this);
    }
}
