package ru.imagebook.shared.model.site;

import java.util.SortedSet;
import java.util.TreeSet;

public class SectionImpl extends PageImpl implements Section {
	private static final long serialVersionUID = 6557815943269441433L;

	public SectionImpl() {
		setChildren(new TreeSet<Section>());
		setLevel(0);
		setNumber(0);
		setHidden(false);
	}

	@Override
	public int getLevel() {
		return (Integer) get(LEVEL);
	}

	@Override
	public void setLevel(int level) {
		set(LEVEL, level);
	}

	@Override
	public Integer getNumber() {
		return get(NUMBER);
	}

	@Override
	public void setNumber(Integer number) {
		set(NUMBER, number);
	}

	@Override
	public Section getParent() {
		return get(PARENT);
	}

	@Override
	public void setParent(Section parent) {
		set(PARENT, parent);
	}

	@Override
	public SortedSet<Section> getChildren() {
		return get(CHILDREN);
	}

	@Override
	public void setChildren(SortedSet<Section> children) {
		set(CHILDREN, children);
	}

	@Override
	public boolean isHidden() {
		return (Boolean) get(HIDDEN);
	}

	@Override
	public void setHidden(boolean hidden) {
		set(HIDDEN, hidden);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Section))
			return false;

		Section section = (Section) obj;
		if (section.getId() == null || getId() == null)
			return false;

		return section.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}

	@Override
	public int compareTo(Section section) {
		return getNumber().compareTo(section.getNumber());
	}

	@Override
	public boolean isRoot() {
		return getLevel() == 0;
	}
}
