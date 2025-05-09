package ru.minogin.undo.server.action.child;

import ru.minogin.data.server.repository.EntityRepository;
import ru.minogin.data.shared.model.BaseEntity;
import ru.minogin.undo.server.action.UndoableAction;

import java.util.List;

public class InsertOrderedChildAction<P extends BaseEntity, C extends BaseEntity>
		implements UndoableAction {
	private final Integer parentId;
	private C child;
	private final int index;
	private final EntityRepository repository;
	private final Delegate<P, C> delegate;

	public InsertOrderedChildAction(EntityRepository repository,
			Delegate<P, C> delegate, Integer parentId, C child, int index) {
		this.repository = repository;
		this.delegate = delegate;
		this.parentId = parentId;
		this.child = child;
		this.index = index;
	}

	@Override
	public void execute() {
		P parent = repository.get(delegate.getParentClass(), parentId);
		delegate.getChildren(parent).add(index, child);
		repository.save(child);
	}

	@Override
	public void undo() {
		child = repository.get(delegate.getChildClass(), child.getId());
		P parent = delegate.getParent(child);
		List<C> children = delegate.getChildren(parent);
		children.remove(child);
		repository.delete(child);
	}
}
