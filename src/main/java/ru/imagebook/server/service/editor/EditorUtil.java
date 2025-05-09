package ru.imagebook.server.service.editor;

public class EditorUtil {
    public static final String ARCHIVE_IS_NOT_A_ZIP = "java.util.zip.ZipException: archive is not a ZIP archive";
    public static final String UNSUPPORTED_IMAGE_TYPE = "Unsupported Image Type";

	private final EditorConfig config;

	public EditorUtil(EditorConfig config) {
		this.config = config;
	}

	public String getScreenPath(int userId) {
		return getStorageUserPath(userId) + "/screen";
	}

	public String getStorageUserPath(int userId) {
		String storagePath = config.getStoragePath();
		return storagePath + "/" + userId;
	}

	public String getScreenPath(int userId, int imageId) {
		String layoutPath = config.getLayoutPath();
		return layoutPath + "/" + userId + "/" + imageId + "s.jpg";
	}

	public String getPreviewPath(int userId) {
		return getStorageUserPath(userId) + "/preview";
	}

	public String getImagePath(int userId, int imageId) {
		String layoutPath = config.getLayoutPath();
		return layoutPath + "/" + userId + "/" + imageId + ".jpg";
	}
}
