package ru.imagebook.shared.model.admin;

import java.io.Serializable;
import java.util.List;

import ru.imagebook.shared.model.User;

/**
 * Created by ksf on 13.01.17.
 */
public class UsersResult implements Serializable {
    private List<User> users;
    private long total;

    public UsersResult() {
    }

    public UsersResult(List<User> users, long total) {
        this.users = users;
        this.total = total;
    }

    public List<User> getUsers() {
        return users;
    }

    public long getTotal() {
        return total;
    }
}