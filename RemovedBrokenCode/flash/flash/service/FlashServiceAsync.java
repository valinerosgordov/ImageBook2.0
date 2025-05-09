package ru.imagebook.client.flash.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.flash.Flash;

public interface FlashServiceAsync {
	void loadData(int code, AsyncCallback<FlashData> callback);

	@Deprecated
	void unpublish(int orderId, AsyncCallback<Void> callback);

	@Deprecated
	void publish(int orderId, AsyncCallback<Void> callback);

	void generateFlash(int orderId, int flashWidth, AsyncCallback<Void> callback);

	void loadFlashes(int orderId, AsyncCallback<List<Flash>> callback);

	void deleteFlash(int orderId, int width, AsyncCallback<Void> callback);
}
