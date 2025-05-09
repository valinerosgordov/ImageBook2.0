package ru.imagebook.client.editor.ctl.file;

import java.util.List;

import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.editor.NotificationType;
import ru.minogin.core.client.file.FileBean;

public interface FileView {
	void showFolders(FileBean root);

	void layout();

	void alertFolderNotSelected();

	void showUploadForm(FileBean folder, String sessionId);

	void showImages(List<String> names, String sessionId, FileBean folder);

	void hideUploadForm();

	void showCreateFolderForm();

	void hideCreateFolderForm();

	void showEditFolderForm(String name);

	void hideEditFolderForm();

	void selectFolder(FileBean baseFolder);

	void updateUploadProgress(double value);

	void hideUploadProgress();

	void alertUploadFailed();

	void alertWrongFileType();

	void showImageMenu(AlbumOrder order, int pageNumber);

	void enableDisposeButton();

	void disableDisposeButton();

	void showUnpackingProgress();

	void hideUnpackingProgress();

    void alertNonZipArchiveFormat();

    void alertUnsupportedImageType(String filename);

	void alertUnpackingFailed();

	void showResizingProgress();

	void hideResizingProgress();

	void alertResizingFailed();

	void updateResizeProgress(double value);

	void showNotificationMessage(Integer type, int imageLayoutType, boolean isShowMessage);

	void disableDisposeFolderImagesMenu();
}
