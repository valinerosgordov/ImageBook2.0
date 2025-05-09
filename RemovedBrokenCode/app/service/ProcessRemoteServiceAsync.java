package ru.imagebook.client.app.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.Order;

/**

 * @since 03.12.2014
 */
public interface ProcessRemoteServiceAsync {
    void loadProcessingOrders(AsyncCallback<List<Order<?>>> callback);
}