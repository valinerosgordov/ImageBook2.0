package ru.imagebook.client.admin.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.admin.UsersResult;

/**
 * Created by ksf on 13.01.17.
 */
public interface AlbumRemoteServiceAsync {
    void loadUsers(int offset, int limit, String query, AsyncCallback<UsersResult> callback);
}
