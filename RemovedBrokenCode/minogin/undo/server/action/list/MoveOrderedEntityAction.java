package ru.minogin.undo.server.action.list;

import ru.minogin.data.server.repository.OrderedEntityRepository;
import ru.minogin.data.shared.model.OrderedEntity;
import ru.minogin.undo.server.action.UndoableAction;

public class MoveOrderedEntityAction<T extends OrderedEntity> implements
        UndoableAction {
	private final OrderedEntityRepository repository;
	private final Class<T> clazz;
	private final Integer id;
	private final int targetIndex;

	private int index;

	public MoveOrderedEntityAction(OrderedEntityRepository repository,
			Class<T> clazz, Integer id, int targetIndex) {
		this.repository = repository;
		this.clazz = clazz;
		this.id = id;
		this.targetIndex = targetIndex;
	}

	@Override
	public void execute() {
		T entity = (T) repository.get(clazz, id);
		index = entity.getIndex();

		repository.move(clazz, id, targetIndex);
	}

	@Override
	public void undo() {
		repository.move(clazz, id, index);
	}
}
