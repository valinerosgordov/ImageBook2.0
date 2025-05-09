package ru.minogin.undo.server.action.list;

import ru.minogin.data.server.repository.OrderedEntityRepository;
import ru.minogin.data.shared.model.OrderedEntity;
import ru.minogin.undo.server.action.UndoableAction;

public class AddOrderedEntityAction<T extends OrderedEntity> implements
        UndoableAction {
	private final OrderedEntityRepository repository;
	private final T entity;

	public AddOrderedEntityAction(OrderedEntityRepository repository, T entity) {
		this.repository = repository;
		this.entity = entity;
	}

	@Override
	public void execute() {
		repository.add(entity);
	}

	@Override
	public void undo() {
		repository.delete(entity.getClass(), entity.getId());
	}
}
