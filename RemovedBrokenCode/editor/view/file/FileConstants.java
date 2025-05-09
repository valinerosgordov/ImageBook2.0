package ru.imagebook.client.editor.view.file;

import com.google.gwt.i18n.client.Messages;

public interface FileConstants extends Messages {
	String nameColumn();

	String uploadButton();

	String folderNotSelected();

	String addLeftMenuItem();

	String addRightMenuItem();

	String addSpreadMenuItem();

	String createFolderItem();

	String folderNameField();

	String createButton();

	String createFolderWindow();

	String uploadField();

	String uploadFormButton();

	String deleteItem();

	String confirmDeleteFolder();

	String uploadFieldHint();

	String renameFolderItem();

	String editFolderWindow();

	String folderActionsButton();

	String imageActionsButton();

	String deleteImageMenuItem();

	String addMenuItem();

	String confirmDeleteImage();

	String uploadProgressTitle();

	String uploadProgressMessage();

	String uploadFailed();

	String wrongFileType();

	String unpackingProgressTitle();

	String unpackingProgressMessage();

    String nonZipArchiveFormat();

    String unsupportedImageType(String filename);

	String unpackingFailed();

	String resizingProgressTitle();

	String resizingProgressMessage();

	String resizingFailed();

	String showNotificationWindow();

	String cancelShowMessage();
}
