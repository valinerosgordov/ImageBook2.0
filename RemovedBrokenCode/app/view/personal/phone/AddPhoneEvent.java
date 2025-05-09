package ru.imagebook.client.app.view.personal.phone;

import com.google.gwt.event.shared.GwtEvent;


public class AddPhoneEvent extends GwtEvent<AddPhoneEventHandler> {
    public static Type<AddPhoneEventHandler> TYPE = new Type<AddPhoneEventHandler>();

    private final String phone;

    public AddPhoneEvent(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public Type<AddPhoneEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddPhoneEventHandler handler) {
        handler.onAddPhone(this);
    }
}
