package ru.imagebook.server.service.flash;

import ru.imagebook.shared.model.Order;

public class FlashPath {
	private final FlashConfig config;

	public FlashPath(FlashConfig config) {
		this.config = config;
	}

	public String getFlashDir(int orderId) {
		return config.getFlashPath() + "/" + orderId;
	}

	public String getImagePath(int orderId, int type, int size, int page) {
		String path = getFlashDir(orderId) + "/";
		if (type == PageType.NORMAL)
			path += page;
		else if (type == PageType.FRONT)
			path += "f" + page;
		else if (type == PageType.BACK)
			path += "b" + page;

		if (size == PageSize.SMALL)
			path += "_s";
		else if (size == PageSize.LARGE)
			path += "_l";

		return path + ".jpg";
	}

	public String getWebFlashDir(int orderId, boolean small) {
		String path = config.getWebFlashPath();
		if (small)
			path += "_small";
		return path + "/" + orderId;
	}

	public String getWebImagePath(int orderId, int type, int size, int page) {
		// small = 'true' - for backward compatibility
		String path = getWebFlashDir(orderId, true) + "/";
		if (type == PageType.NORMAL) {
			path += page;
		} else if (type == PageType.FRONT) {
			path += "f" + page;
		} else if (type == PageType.BACK) {
			path += "b" + page;
		}

		if (size == PageSize.LARGE) {
			path += "_l";
		}

		return path + ".jpg";
	}

	public String getJpegDir(Order<?> order) {
		return config.getJpegPath() + "/" + order.getId();
	}

	public String getPublishPath(String orderCode, int flashWidth) {
		return config.getPublishPath() + "/" + orderCode + "/" + flashWidth;
	}

	public String getPublishPath(String orderCode) {
		return config.getPublishPath() + "/" + orderCode;
	}
}
