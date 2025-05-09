package ru.minogin.ui.server.ckfinder;

import javax.servlet.http.HttpServletRequest;

public interface CKFinderConfig {
	boolean checkAuthentication(HttpServletRequest request);

	String getBaseDir();

	String getBaseURL();
}
