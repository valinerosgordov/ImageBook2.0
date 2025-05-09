package ru.imagebook.client.admin.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.admin.UsersResult;

/**
 * Created by ksf on 13.01.17.
 */
@RemoteServiceRelativePath("album.remoteService")
public interface AlbumRemoteService extends RemoteService {
    UsersResult loadUsers(int offset, int limit, String query);
}