package ru.imagebook.client.app.view.personal.user;

import com.google.gwt.event.shared.GwtEvent;


public class UserNameSaveEvent extends GwtEvent<UserNameSaveEventHandler> {
    public static Type<UserNameSaveEventHandler> TYPE = new Type<UserNameSaveEventHandler>();

    private final String lastName;
    private final String name;
    private final String surname;

    public UserNameSaveEvent(String lastName, String name, String surname) {
        this.lastName = lastName;
        this.name = name;
        this.surname = surname;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public Type<UserNameSaveEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserNameSaveEventHandler handler) {
        handler.onUserNameSave(this);
    }
}
