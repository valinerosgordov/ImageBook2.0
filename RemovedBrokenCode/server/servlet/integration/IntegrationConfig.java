package ru.imagebook.server.servlet.integration;

import java.util.Map;


public class IntegrationConfig {
	
	private Map<String, IntegrationAccount> accounts;

	public Map<String, IntegrationAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(Map<String, IntegrationAccount> accounts) {
		this.accounts = accounts;
	}
	
}
