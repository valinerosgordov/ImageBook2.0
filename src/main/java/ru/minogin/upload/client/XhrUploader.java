package ru.minogin.upload.client;

import com.google.gwt.core.client.JavaScriptObject;

public class XhrUploader {
	public native void upload(JavaScriptObject file, String url, UploadCallback callback)
	/*-{
		xhr = new XMLHttpRequest();
		xhr.open('POST', url, true);
		xhr.onload = function(e) {
			if (this.status == 200) {
				callback.@ru.minogin.upload.client.UploadCallback::onUploaded(Lcom/google/gwt/xhr/client/XMLHttpRequest;)(this);
			}
			else {
				callback.@ru.minogin.upload.client.UploadCallback::onError()();
			}
		};
		xhr.upload.onprogress = function(e) {
			if (e.lengthComputable) {
				value = (e.loaded / e.total) * 100;
				callback.@ru.minogin.upload.client.UploadCallback::onProgress(D)(value);
			}
		};
		xhr.setRequestHeader("X-File-Name", encodeURI(file.name));
		xhr.send(file);
	}-*/;
	
	public static native boolean isSupported()
	/*-{
	  if (window.File && window.FileReader && window.FileList && window.Blob)
	  	return true;
	  else
	  	return false; 
	}-*/;
}
