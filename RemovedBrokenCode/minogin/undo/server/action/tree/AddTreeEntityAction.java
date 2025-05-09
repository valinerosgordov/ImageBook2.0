package ru.minogin.undo.server.action.tree;

import ru.minogin.data.server.repository.TreeEntityRepository;
import ru.minogin.data.shared.model.TreeEntity;
import ru.minogin.undo.server.action.UndoableAction;

public class AddTreeEntityAction<T extends TreeEntity<T>> implements
        UndoableAction {
	private final TreeEntityRepository repository;
	private final Integer parentId;
	private final T entity;

	public AddTreeEntityAction(TreeEntityRepository repository,
			Integer parentId, T entity) {
		this.repository = repository;
		this.parentId = parentId;
		this.entity = entity;
	}

	@Override
	public void execute() {
		repository.add(parentId, entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void undo() {
		repository.delete(entity.getClass(), entity.getId());
	}
}
