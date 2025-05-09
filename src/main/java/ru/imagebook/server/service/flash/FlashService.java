package ru.imagebook.server.service.flash;

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.common.preview.FlashParams;

public interface FlashService {
	void startAsync();

	FlashParams getFlashPreviewParams(int orderId, int userId);

	FlashParams getFlashPreviewParams(Order<?> order, String logo);

	void showFlashXml(String sessionId, Writer writer);

	void showFlashImage(String sessionId, int type, int size, int page, HttpServletResponse response);

	boolean generate(Order<?> order);

	void publishFlash(int orderId, int userId);

	Order<?> loadOrder(int orderId, int userId);

	void unpublishFlash(int orderId, int userId);

	String showWebFlash(String orderNumber);

	@Deprecated
	String showWebFlash(String orderNumber, String name, String author, boolean small);

	@Deprecated
	void showWebFlashXml(String sessionId, Writer writer);

	void showWebFlashImage(String sessionId, int type, int size, int page, HttpServletResponse response);

	@Deprecated
	void generateWebImages(Order<?> order, boolean small);

	void generateWebImages(Order<?> order);
}
