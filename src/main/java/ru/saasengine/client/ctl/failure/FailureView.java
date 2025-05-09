package ru.saasengine.client.ctl.failure;

public interface FailureView {
	void onUnknownError();
	
	void onDisconnect();
	
	void onIncompatibleVersion();
}
