package ru.imagebook.server.service;

import javax.servlet.http.HttpServletResponse;

public interface OrderService2 {
	void requestRegenerateOrder(int orderId);

	void regenerateOrder(int orderId);

	@Deprecated
	void publishWebFlash(int orderId, boolean small);

	void publishWebFlash(int orderId);

	void showPreview(int orderId, int pageType, HttpServletResponse response);

	void showFlyleafAppImage(Integer flyleafId, HttpServletResponse response);

	void showVellumAppImage(Integer vellumId, HttpServletResponse response);
}
