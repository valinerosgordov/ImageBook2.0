package ru.minogin.ui.client.upload;

import com.google.gwt.http.client.*;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;
import ru.minogin.ui.client.field.upload.ActiveFileUpload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An asynchronous file uploader for the drag and drop file upload field.
 * 
 * @author Andrey Minogin
 * 
 * @see ActiveFileUpload */
public class ClientFileUploader {
	private FileReader reader = new FileReader();
	private List<File> queue = new ArrayList<File>();
	private ClientFileUploaderCallback callback;
	private Map<String, String> params = new HashMap<String, String>();

	public interface ClientFileUploaderCallback {
		void onFileUploaded(File file, Response response);

		void onAllFilesUploaded();

		void onFailure(Throwable t);
	}

	/** @param uploadUrl - URL to send files to. */
	public ClientFileUploader(final String uploadUrl) {
		reader.addLoadEndHandler(new LoadEndHandler() {
			@Override
			public void onLoadEnd(LoadEndEvent event) {
				final File file = queue.get(0);
				final RequestBuilder requestBuilder = new RequestBuilder(
						RequestBuilder.POST, uploadUrl);
				requestBuilder.setHeader("filename", file.getName());
				requestBuilder.setHeader("Content-Type", file.getType()
						+ ";charset=ISO-8859-1");
				for (String key : params.keySet()) {
					requestBuilder.setHeader(key, params.get(key));
				}
				try {
					String result = reader.getStringResult();
					String result64 = base64encode(result);
					requestBuilder.sendRequest(result64, new RequestCallback() {
						@Override
						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() == Response.SC_OK) {
								if (callback != null)
									callback.onFileUploaded(file, response);
								queue.remove(0);
								readNext();
							}
							else {
								callback.onFailure(new UploadException(response.getStatusCode(), response.getText()));
							}
						}

						@Override
						public void onError(Request request, Throwable t) {
							if (callback != null)
								callback.onFailure(t);
						}
					});
				}
				catch (RequestException e) {
					if (callback != null)
						callback.onFailure(e);
				}
			}
		});
	}

	public void upload(FileList files) {
		upload(files, null);
	}

	public void upload(FileList files, ClientFileUploaderCallback callback) {
		this.callback = callback;
		for (File file : files) {
			queue.add(file);
		}
		readNext();
	}

	private void readNext() {
		if (queue.size() > 0) {
			File file = queue.get(0);
			reader.readAsBinaryString(file);
		}
		else {
			if (callback != null)
				callback.onAllFilesUploaded();
		}
	}

	private static native String base64encode(String str) /*-{
		return $wnd.btoa(str);
	}-*/;

	public void setParam(String name, String value) {
		params.put(name, value);
	}
}
