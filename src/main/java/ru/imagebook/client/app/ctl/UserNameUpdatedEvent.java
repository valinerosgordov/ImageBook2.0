package ru.imagebook.client.app.ctl;

import com.google.web.bindery.event.shared.binder.GenericEvent;

import ru.imagebook.shared.model.User;


public class UserNameUpdatedEvent extends GenericEvent {
    private final User user;

    public UserNameUpdatedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
