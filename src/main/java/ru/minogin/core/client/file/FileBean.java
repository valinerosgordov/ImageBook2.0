package ru.minogin.core.client.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.text.StringUtil;

public class FileBean extends BaseBean {
	private static final long serialVersionUID = 6457194216356774257L;

	public static final String NAME = "name";
	public static final String PARENT = "parent";
	public static final String CHILDREN = "children";

	public FileBean() {
		setChildren(new ArrayList<FileBean>());
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public FileBean getParent() {
		return get(PARENT);
	}

	public void setParent(FileBean parent) {
		set(PARENT, parent);
	}

	public List<FileBean> getChildren() {
		return get(CHILDREN);
	}

	public void setChildren(List<FileBean> children) {
		set(CHILDREN, children);
	}

	public String getPath() {
		List<String> names = new ArrayList<String>();
		FileBean parent = this;
		while (parent != null) {
			names.add(parent.getName());
			parent = parent.getParent();
		}
		Collections.reverse(names);
		return StringUtil.implode("/", names);
	}
}
