package ru.imagebook.client.admin.view.site;

import ru.imagebook.shared.model.site.Section;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class SectionModel extends BaseTreeModel {
	private static final long serialVersionUID = -6776380624683880127L;

	private final Section section;

	public SectionModel(Section section) {
		this.section = section;
	}

	public Section getSection() {
		return section;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(String property) {
		return (X) section.get(property);
	}

	@Override
	public <X extends Object> X set(String name, X value) {
		return section.set(name, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SectionModel))
			return false;

		SectionModel model = (SectionModel) obj;
		return section.equals(model.getSection());
	}

	@Override
	public int hashCode() {
		return section.hashCode();
	}
}
