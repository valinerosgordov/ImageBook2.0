package ru.imagebook.client.editor.view.file;

import ru.minogin.core.client.file.FileBean;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class FileModel extends BaseTreeModel {
	private static final long serialVersionUID = 8750651951002743313L;

	public static final String FILE = "file";
	public static final String NAME = "name";

	public FileModel(FileBean file) {
		set(FILE, file);
		set(NAME, file.getName());
	}

	public FileBean getFile() {
		return get(FILE);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FileModel))
			return false;

		FileModel model = (FileModel) obj;
		return getFile().equals(model.getFile());
	}

	@Override
	public int hashCode() {
		return getFile().hashCode();
	}
}
