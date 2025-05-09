package ru.imagebook.client.app.view.personal.email;

import com.google.gwt.event.shared.GwtEvent;


public class AddEmailEvent extends GwtEvent<AddEmailEventHandler> {
    public static Type<AddEmailEventHandler> TYPE = new Type<AddEmailEventHandler>();

    private final String email;

    public AddEmailEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Type<AddEmailEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddEmailEventHandler handler) {
        handler.onAddEmail(this);
    }
}
