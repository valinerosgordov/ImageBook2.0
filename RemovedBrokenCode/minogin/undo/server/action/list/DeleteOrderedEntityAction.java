package ru.minogin.undo.server.action.list;

import ru.minogin.data.server.repository.OrderedEntityRepository;
import ru.minogin.data.shared.model.OrderedEntity;
import ru.minogin.undo.server.action.UndoableAction;

public class DeleteOrderedEntityAction<T extends OrderedEntity> implements
        UndoableAction {
	private final OrderedEntityRepository repository;
	private final Class<T> clazz;
	private final Integer id;
	
	private T entity;

	public DeleteOrderedEntityAction(OrderedEntityRepository repository,
			Class<T> clazz, Integer id) {
		this.repository = repository;
		this.clazz = clazz;
		this.id = id;
	}

	@Override
	public void execute() {
		entity = repository.get(clazz, id);
		repository.delete(clazz, id);
	}

	@Override
	public void undo() {
		int index = entity.getIndex();
		repository.insertPreservingId(entity, index);
	}
}
