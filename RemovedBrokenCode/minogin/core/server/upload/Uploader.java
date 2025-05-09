package ru.minogin.core.server.upload;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import ru.minogin.core.client.exception.Exceptions;

public class Uploader {
	private static final int KB = 1024;
	private static final int MB = KB * KB;
	private static final int GB = KB * KB * KB;
	private static final int MAX_SIZE_IN_MEMORY = 10 * MB;
	private static final int MAX_FILE_SIZE = 1 * GB;
	private static final int CHUNK_SIZE = 10 * MB;

	private final HttpServletRequest request;
	private final String uploadPath;
	private Map<String, String> parameters = new HashMap<String, String>();
	private List<FileItem> items;
	private ProgressCallback progressCallback;
	private Map<String, FileItem> fieldItemMap = new HashMap<String, FileItem>();

	public Uploader(HttpServletRequest request, String uploadPath) {
		this.request = request;
		this.uploadPath = uploadPath;
	}

	// TODO don't know wheter this really works
	public void parseParameters() {
		try {
			if (!ServletFileUpload.isMultipartContent(request))
				throw new RuntimeException("Wrong content type.");

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setFileCleaningTracker(null);
			factory.setSizeThreshold(MAX_SIZE_IN_MEMORY);
			factory.setRepository(new File(uploadPath));
			ServletFileUpload upload = new ServletFileUpload(factory);

			FileItemIterator iterator = upload.getItemIterator(request);

			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				String name = item.getFieldName();
				InputStream is = item.openStream();

				if (item.isFormField()) {
					String value = IOUtils.toString(is, "UTF-8");
					is.close();

					parameters.put(name, value);
				}
			}
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<FileItem> upload() {
		if (!ServletFileUpload.isMultipartContent(request))
			throw new RuntimeException("Wrong content type.");

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setFileCleaningTracker(null);
		factory.setSizeThreshold(MAX_SIZE_IN_MEMORY);
		factory.setRepository(new File(uploadPath));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		upload.setSizeMax(MAX_FILE_SIZE);

		ProgressListener progressListener = new ProgressListener() {
			private int currentChunks = -1;

			@Override
			public void update(long bytesRead, long contentLength, int items) {
				int chunks = (int) (bytesRead / CHUNK_SIZE);
				if (currentChunks == chunks)
					return;
				currentChunks = chunks;

				if (progressCallback != null)
					progressCallback.onUploaded(bytesRead, contentLength);

				// TODO
				// authManager.sendToUser(session.getWorkspaceId(), session.getUserId(),
				// new UploadProgressEvent(uploadId, bytesRead, contentLength));
			}
		};
		upload.setProgressListener(progressListener);

		try {
			items = upload.parseRequest(request);
			Iterator<FileItem> iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = iterator.next();
				if (item.isFormField()) {
					parameters.put(item.getFieldName(), item.getString());
					iterator.remove();
				}
				else if (item.getName().isEmpty())
					iterator.remove();
				else
					fieldItemMap.put(item.getFieldName(), item);
			}
			return items;
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}

	public Map<String, FileItem> getFieldItemMap() {
		return fieldItemMap;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}

	public List<File> writeAll(String path, UploadFilter filter) {
		try {
			List<File> files = new ArrayList<File>();

			for (FileItem item : items) {
				String fileName = URLDecoder.decode(item.getName(), "UTF-8");
				File file = new File(path, fileName);

				File parentFile = file.getParentFile();
				if (parentFile != null)
					parentFile.mkdirs();

				if (filter == null || file.isDirectory() || filter.isAllowed(file)) {
					item.write(file);
					if (!file.isDirectory())
						files.add(file);
				}
			}

			return files;
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}

	public void setProgressCallback(ProgressCallback progressCallback) {
		this.progressCallback = progressCallback;
	}
}
