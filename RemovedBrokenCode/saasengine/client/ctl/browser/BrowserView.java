package ru.saasengine.client.ctl.browser;

public interface BrowserView {
	void suppressEsc();
	
	void setCloseConfirmation(String message);
}
