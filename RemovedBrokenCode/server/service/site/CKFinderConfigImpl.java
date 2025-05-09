package ru.imagebook.server.service.site;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import ru.minogin.ui.server.ckfinder.CKFinderConfig;

public class CKFinderConfigImpl implements CKFinderConfig {
	@Autowired
	private SiteConfig config;

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		return true;
	}

	@Override
	public String getBaseDir() {
		return config.getFilesPath();
	}

	@Override
	public String getBaseURL() {
		return config.getFilesUrl();
	}
}
