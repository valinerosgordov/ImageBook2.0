package ru.minogin.undo.server.action.tree;

import ru.minogin.data.server.repository.TreeEntityRepository;
import ru.minogin.data.shared.model.TreeEntity;
import ru.minogin.undo.server.action.UndoableAction;

import java.util.List;

public class DeleteTreeEntityAction<T extends TreeEntity<T>> implements
        UndoableAction {
	private final TreeEntityRepository repository;
	private final Class<T> clazz;
	private final Integer id;
	
	private T entity;
	private int index;

	public DeleteTreeEntityAction(TreeEntityRepository repository, Class<T> clazz, Integer id) {
		this.repository = repository;
		this.clazz = clazz;
		this.id = id;
	}

	@Override
	public void execute() {
		entity = repository.get(clazz, id);
		T parent = entity.getParent();
		List<T> children = parent.getChildren();
		index = children.indexOf(entity);
		clearVersion(entity);
		children.remove(entity);
		repository.delete(clazz, entity.getId());
	}

	private void clearVersion(T entity) {
		entity.setVersion(null);
		for (T child : entity.getChildren()) {
			clearVersion(child);
		}
	}

	@Override
	public void undo() {
		repository.insert(entity.getParent().getId(), entity, index);
	}
}
