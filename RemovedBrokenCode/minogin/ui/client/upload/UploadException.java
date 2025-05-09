package ru.minogin.ui.client.upload;

public class UploadException extends RuntimeException {
	private static final long serialVersionUID = -2544614781678974282L;

	private int statusCode;
	private String response;

	public UploadException(int statusCode, String response) {
		this.statusCode = statusCode;
		this.response = response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getResponse() {
		return response;
	}
}
