package ru.imagebook.client.app.view.personal.phone;

import com.google.gwt.event.shared.GwtEvent;


public class DeletePhoneEvent extends GwtEvent<DeletePhoneEventHandler> {
    public static Type<DeletePhoneEventHandler> TYPE = new Type<DeletePhoneEventHandler>();

    private final Integer phoneId;

    public DeletePhoneEvent(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    @Override
    public Type<DeletePhoneEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DeletePhoneEventHandler handler) {
        handler.onDeletePhone(this);
    }
}
