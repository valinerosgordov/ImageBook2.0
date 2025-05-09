package ru.minogin.data.shared.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class TreeEntity<T extends TreeEntity<T>> extends BaseEntity {
	public static final String INDEX = "index";
	public static final String PARENT = "parent";
	public static final String CHILDREN = "children";

	private int index;
	private T parent;
	private List<T> children = new ArrayList<T>();

	public TreeEntity() {
	}

	@Column(name = "index_")
	@NotNull
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@ManyToOne
	@JoinColumn(name = "parent_")
	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = PARENT, cascade = CascadeType.ALL)
	@OrderBy(INDEX)
	public List<T> getChildren() {
		return children;
	}

	public void setChildren(List<T> children) {
		this.children = children;
	}

	@SuppressWarnings("unchecked")
	public void add(T entity) {
		entity.setIndex(children.size());
		entity.setParent((T) this);
		children.add(entity);
	}
	
	@Transient
	public boolean isRoot() {
		return getParent() == null;
	}
	
	@Transient
	public int getLevel() {
		int level = 0;
		T parent = getParent();
		while (parent != null) {
			parent = parent.getParent();
			level++;
		}
		return level;
	}
}
