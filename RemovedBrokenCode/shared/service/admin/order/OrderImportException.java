package ru.imagebook.shared.service.admin.order;

public class OrderImportException extends RuntimeException {
	private static final long serialVersionUID = -4161997974508624090L;
	
	private int errorCode;
	private String errorData;
	
	public OrderImportException() {
		super();				
	}
	
	public OrderImportException(int errorCode, String errorData) {
		super();
		this.errorCode = errorCode;
		this.errorData = errorData;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorData() {
		return errorData;
	}
}
