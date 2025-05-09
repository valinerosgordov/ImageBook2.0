package ru.minogin.undo.server.action.base;

import ru.minogin.data.server.repository.EntityRepository;
import ru.minogin.data.shared.model.BaseEntity;
import ru.minogin.undo.server.action.UndoableAction;

public abstract class UpdateEntityAction<T extends BaseEntity> implements
        UndoableAction {
	private final EntityRepository repository;
	private final T modified;
	private T copy;

	public UpdateEntityAction(EntityRepository repository, T modified) {
		this.repository = repository;
		this.modified = modified;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		copy = (T) repository.get(modified.getClass(), modified.getId());
		repository.detach(copy);

		T entity = (T) repository.get(modified.getClass(), modified.getId());
		update(entity, modified);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void undo() {
		T entity = (T) repository.get(copy.getClass(), copy.getId());
		update(entity, copy);
	}

	protected abstract void update(T entity, T modified);
}
