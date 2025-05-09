package ru.minogin.undo.server.action.child;

import ru.minogin.data.server.repository.EntityRepository;
import ru.minogin.data.shared.model.BaseEntity;
import ru.minogin.undo.server.action.UndoableAction;

import java.util.List;

public class MoveOrderedChildAction<P extends BaseEntity, C extends BaseEntity>
		implements UndoableAction {
	private final Integer id;
	private final int targetIndex;
	private final EntityRepository repository;
	private int index;
	private final Delegate<P, C> delegate;

	public MoveOrderedChildAction(EntityRepository repository,
			Delegate<P, C> delegate, Integer id, int targetIndex) {
		this.repository = repository;
		this.delegate = delegate;
		this.id = id;
		this.targetIndex = targetIndex;
	}

	@Override
	public void execute() {
		C child = repository.get(delegate.getChildClass(), id);
		P parent = delegate.getParent(child);
		List<C> children = delegate.getChildren(parent);
		index = children.indexOf(child);
		children.remove(child);
		children.add(targetIndex, child);
	}

	@Override
	public void undo() {
		C child = repository.get(delegate.getChildClass(), id);
		P parent = delegate.getParent(child);
		List<C> children = delegate.getChildren(parent);
		children.remove(child);
		children.add(index, child);
	}
}
