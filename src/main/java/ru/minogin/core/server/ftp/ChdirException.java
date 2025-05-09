package ru.minogin.core.server.ftp;

public class ChdirException extends FtpException {
	private static final long serialVersionUID = 2298844994396734565L;

	public ChdirException(String path) {
		super("Chdir failed: " + path + " not found.");
	}
}
