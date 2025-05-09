package ru.imagebook.client.app.view.personal.password;

import com.google.gwt.event.shared.GwtEvent;


public class PasswordChangeEvent extends GwtEvent<PasswordChangeEventHandler> {
    public static Type<PasswordChangeEventHandler> TYPE = new Type<PasswordChangeEventHandler>();

    private final String password;

    public PasswordChangeEvent(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Type<PasswordChangeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PasswordChangeEventHandler handler) {
        handler.onPasswordChange(this);
    }
}
