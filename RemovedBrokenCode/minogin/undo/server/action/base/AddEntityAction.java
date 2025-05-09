package ru.minogin.undo.server.action.base;

import ru.minogin.data.server.repository.EntityRepository;
import ru.minogin.data.shared.model.BaseEntity;
import ru.minogin.undo.server.action.UndoableAction;

public class AddEntityAction<T extends BaseEntity> implements UndoableAction {
	private final EntityRepository repository;
	private final T entity;

	public AddEntityAction(EntityRepository repository, T entity) {
		this.repository = repository;
		this.entity = entity;
	}

	@Override
	public void execute() {
		repository.save(entity);
	}

	@Override
	public void undo() {
		repository.delete(entity.getClass(), entity.getId());
	}
}
