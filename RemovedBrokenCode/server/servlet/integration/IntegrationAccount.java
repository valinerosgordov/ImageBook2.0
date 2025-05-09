package ru.imagebook.server.servlet.integration;

import java.util.List;

/**
 * 
 * @author Svyatoslav Gulin
 * @version 30.10.2011 
 */
public class IntegrationAccount {
	
	private String accountCode;
	
	private List<String> excludeAlbumMasks;

	public List<String> getExcludeAlbumMasks() {
		return excludeAlbumMasks;
	}

	public void setExcludeAlbumMasks(List<String> excludeAlbumMasks) {
		this.excludeAlbumMasks = excludeAlbumMasks;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
}
