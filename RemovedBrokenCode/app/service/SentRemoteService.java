package ru.imagebook.client.app.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.Order;


@RemoteServiceRelativePath("sent.remoteService")
public interface SentRemoteService extends RemoteService {
    List<Order<?>> loadSentOrders();
}
