package ru.imagebook.client.admin.view.site;

import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.Folder;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class DocumentModel extends BaseTreeModel {
	private static final long serialVersionUID = -5578272873925555036L;

	public static final String NAME = "name";
	public static final String FOLDER = "folder";
	public static final String DOCUMENT = "document";

	public void setFolder(Folder folder) {
		set(FOLDER, folder);
		set(NAME, folder.getName());
	}

	public Folder getFolder() {
		return get(FOLDER);
	}

	public void setDocument(Document document) {
		set(DOCUMENT, document);
		set(NAME, document.getName());
	}

	public Document getDocument() {
		return get(DOCUMENT);
	}
}
