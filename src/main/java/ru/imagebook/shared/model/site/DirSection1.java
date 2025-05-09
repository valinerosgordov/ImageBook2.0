package ru.imagebook.shared.model.site;

import java.util.Set;
import java.util.TreeSet;

import ru.minogin.core.client.bean.BaseEntityBean;

public class DirSection1 extends BaseEntityBean implements
		Comparable<DirSection1> {
	private static final long serialVersionUID = 369851637507043225L;

	public static final String INDEX = "index";
	public static final String KEY = "key";
	public static final String NAME = "name";
	public static final String SECTIONS = "sections";

	public DirSection1() {
		setSections(new TreeSet<DirSection2>());
	}

	public Integer getIndex() {
		return get(INDEX);
	}

	public void setIndex(Integer index) {
		set(INDEX, index);
	}

	public String getKey() {
		return get(KEY);
	}

	public void setKey(String key) {
		set(KEY, key);
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public Set<DirSection2> getSections() {
		return get(SECTIONS);
	}

	public void setSections(Set<DirSection2> sections) {
		set(SECTIONS, sections);
	}

	@Override
	public int compareTo(DirSection1 section) {
		if (getIndex() != null && section.getIndex() != null)
			return getIndex().compareTo(section.getIndex());
		else
			return getId().compareTo(section.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof DirSection1))
			return false;

		DirSection1 section = (DirSection1) obj;
		if (section.getId() == null || getId() == null)
			return false;

		return section.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
