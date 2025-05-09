package ru.imagebook.client.flash.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.flash.Flash;
import ru.imagebook.shared.service.admin.flash.FlashExistsException;
import ru.imagebook.shared.service.admin.flash.TooManyFlashesException;

@RemoteServiceRelativePath("flash.remoteService")
public interface FlashService extends RemoteService {
	FlashData loadData(int code);

    @Deprecated
	void unpublish(int orderId);

    @Deprecated
	void publish(int orderId);

	void generateFlash(int orderId, int flashWidth) throws FlashExistsException,
			TooManyFlashesException;

	List<Flash> loadFlashes(int orderId);

	void deleteFlash(int orderId, int width);
}
