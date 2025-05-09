package ru.minogin.core.server.ftp;

public class FtpException extends RuntimeException {
	private static final long serialVersionUID = 2366490129450574390L;

	public FtpException() {}

	public FtpException(String message) {
		super(message);
	}
}
