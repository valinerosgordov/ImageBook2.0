package ru.minogin.core.server.upload;

public interface ProgressCallback {
	void onUploaded(long uploaded, long total);
}
