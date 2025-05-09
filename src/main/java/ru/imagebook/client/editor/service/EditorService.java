package ru.imagebook.client.editor.service;

import ru.imagebook.shared.model.AlbumOrder;
import ru.minogin.core.client.file.FileBean;

import com.google.inject.Singleton;

@Singleton
public class EditorService {
	private AlbumOrder order;
	private FileBean folder;
	private int pageNumber;
	private String imagePath;
	private int variant;

	public AlbumOrder getOrder() {
		return order;
	}

	public void setOrder(AlbumOrder order) {
		this.order = order;
	}

	public FileBean getFolder() {
		return folder;
	}

	public void setFolder(FileBean folder) {
		this.folder = folder;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getVariant() { return variant; }

	public void setVariant(int variant) { this.variant = variant; }
}
