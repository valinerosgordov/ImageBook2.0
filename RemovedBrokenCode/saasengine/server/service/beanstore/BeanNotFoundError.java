package ru.saasengine.server.service.beanstore;


public class BeanNotFoundError extends RuntimeException {
	private static final long serialVersionUID = -8192775368036668969L;

	public BeanNotFoundError(String id) {
		super("No bean in bean store: " + id);
	}
}
