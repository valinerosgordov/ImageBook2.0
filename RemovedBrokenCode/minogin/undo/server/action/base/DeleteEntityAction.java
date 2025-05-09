package ru.minogin.undo.server.action.base;

import ru.minogin.data.server.repository.EntityRepository;
import ru.minogin.data.shared.model.BaseEntity;
import ru.minogin.undo.server.action.UndoableAction;

public class DeleteEntityAction<T extends BaseEntity> implements UndoableAction {
	private final EntityRepository repository;
	private final Class<T> clazz;
	private final Integer id;
	private T entity;

	public DeleteEntityAction(EntityRepository repository, Class<T> clazz,
			Integer id) {
		this.repository = repository;
		this.clazz = clazz;
		this.id = id;
	}

	@Override
	public void execute() {
		entity = repository.get(clazz, id);
		repository.delete(entity);
	}

	@Override
	public void undo() {
		repository.savePreservingId(entity);
	}
}
