package ru.imagebook.server.service2.flash;

public class FlashNotPublishedException extends RuntimeException {
	private static final long serialVersionUID = -671397033645308850L;

	public FlashNotPublishedException(String orderNumber) {
		super(orderNumber);
	}
}
