package ru.imagebook.shared.model.site;

import java.util.SortedSet;

public interface Section extends Page, Comparable<Section> {
	String PARENT = "parent";
	String CHILDREN = "children";
	String LEVEL = "level";
	String NUMBER = "number";
	String HIDDEN = "hidden";

	int getLevel();

	void setLevel(int level);

	Integer getNumber();

	void setNumber(Integer number);

	Section getParent();

	void setParent(Section parent);

	SortedSet<Section> getChildren();

	void setChildren(SortedSet<Section> children);

	boolean isRoot();

	boolean isHidden();

	void setHidden(boolean hidden);
}
