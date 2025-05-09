package ru.imagebook.server.service;

public class MainConfig {
	private String appCode;
	private boolean rpcProduction;
	private String adminEmail;
	private String techEmail;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public boolean isRpcProduction() {
		return rpcProduction;
	}

	public void setRpcProduction(boolean rpcProduction) {
		this.rpcProduction = rpcProduction;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getTechEmail() {
		return techEmail;
	}

	public void setTechEmail(String techEmail) {
		this.techEmail = techEmail;
	}
}
