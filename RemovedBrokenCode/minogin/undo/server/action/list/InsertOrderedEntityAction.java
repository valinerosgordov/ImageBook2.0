package ru.minogin.undo.server.action.list;

import ru.minogin.data.server.repository.OrderedEntityRepository;
import ru.minogin.data.shared.model.OrderedEntity;
import ru.minogin.undo.server.action.UndoableAction;

public class InsertOrderedEntityAction<T extends OrderedEntity> implements
        UndoableAction {
	private final OrderedEntityRepository repository;
	private final T entity;
	private final int index;

	public InsertOrderedEntityAction(OrderedEntityRepository repository,
			T entity, int index) {
		this.repository = repository;
		this.entity = entity;
		this.index = index;
	}

	@Override
	public void execute() {
		repository.insert(entity, index);
	}

	@Override
	public void undo() {
		repository.delete(entity.getClass(), entity.getId());
	}
}
