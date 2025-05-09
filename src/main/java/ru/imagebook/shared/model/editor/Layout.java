package ru.imagebook.shared.model.editor;

import java.util.ArrayList;
import java.util.List;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Layout extends BaseEntityBean {
	private static final long serialVersionUID = 6302821487688037727L;

	public static final String PAGES = "pages";

	public Layout() {
		setPages(new ArrayList<Page>());
	}

	public Layout(Layout prototype) {
		this();

		for (Page page : prototype.getPages()) {
			getPages().add(page.copy());
		}
	}

	public List<Page> getPages() {
		return get(PAGES);
	}

	public void setPages(List<Page> pages) {
		set(PAGES, pages);
	}

	// Copies layout without barcode.
	public Layout copy() {
		return new Layout(this);
	}
}
