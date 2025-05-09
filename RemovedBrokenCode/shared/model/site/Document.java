package ru.imagebook.shared.model.site;

public interface Document extends Page, Comparable<Document> {
	String FOLDER = "folder";

	Folder getFolder();

	void setFolder(Folder folder);
}
