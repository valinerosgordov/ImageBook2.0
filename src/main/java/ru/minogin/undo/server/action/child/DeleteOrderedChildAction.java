package ru.minogin.undo.server.action.child;

import ru.minogin.data.server.repository.EntityRepository;
import ru.minogin.data.shared.model.BaseEntity;
import ru.minogin.undo.server.action.UndoableAction;

import java.util.List;

public class DeleteOrderedChildAction<P extends BaseEntity, C extends BaseEntity>
		implements UndoableAction {
	private final EntityRepository repository;
	private final Integer id;
	private final Delegate<P, C> delegate;
	private Integer parentId;
	private C child;
	private int index;

	public DeleteOrderedChildAction(EntityRepository repository,
			Delegate<P, C> delegate, Integer id) {
		this.repository = repository;
		this.delegate = delegate;
		this.id = id;
	}

	@Override
	public void execute() {
		child = repository.get(delegate.getChildClass(), id);
		P parent = delegate.getParent(child);
		parentId = parent.getId();
		List<C> children = delegate.getChildren(parent);
		index = children.indexOf(child);
		children.remove(child);
		repository.delete(child);
	}

	@Override
	public void undo() {
		P parent = repository.get(delegate.getParentClass(), parentId);
		List<C> children = delegate.getChildren(parent);
		children.add(index, child);
		repository.save(child);
	}
}
