package ru.minogin.upload.client;

import com.google.gwt.xhr.client.XMLHttpRequest;

public interface UploadCallback {
	void onProgress(double value);

	void onUploaded(XMLHttpRequest request);

	void onError();
}