package ru.imagebook.shared.model.editor;

import ru.minogin.core.client.bean.EntityBean;

public interface Component extends EntityBean, Comparable<Component> {
	String INDEX = "index";
	String BLOCKED = "blocked";
	String COMPONENT_ID = "componentId";

	Integer getIndex();

	void setIndex(Integer index);

	boolean isBlocked();

	void setBlocked(boolean blocked);
	
	Integer getComponentId();
	
	void setComponentId(Integer componentId);
	
	Component copy();

	void accept(ComponentVisitor visitor);
}
