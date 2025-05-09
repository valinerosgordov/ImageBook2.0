package ru.imagebook.client.admin.view.site;

import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class DirModel extends BaseTreeModel {
	private static final long serialVersionUID = -5578272873925555036L;

	public static final String NAME = "name";
	public static final String SECTION1 = "section1";
	public static final String SECTION2 = "section2";

	public void setSection1(DirSection1 section1) {
		set(SECTION1, section1);
		set(NAME, section1.getName());
	}

	public void setSection2(DirSection2 section2) {
		set(SECTION2, section2);
		set(NAME, section2.getName());
	}

	public DirSection1 getSection1() {
		return get(SECTION1);
	}

	public DirSection2 getSection2() {
		return get(SECTION2);
	}
}
