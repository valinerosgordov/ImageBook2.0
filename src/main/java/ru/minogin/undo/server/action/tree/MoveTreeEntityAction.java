package ru.minogin.undo.server.action.tree;

import ru.minogin.data.server.repository.TreeEntityRepository;
import ru.minogin.data.shared.model.TreeEntity;
import ru.minogin.undo.server.action.UndoableAction;

public class MoveTreeEntityAction<T extends TreeEntity<T>> implements
        UndoableAction {
	private final TreeEntityRepository repository;
	private final Class<T> clazz;
	private final Integer id;
	private final Integer targetParentId;
	private final int targetIndex;
	private int index;
	private Integer parentId;

	public MoveTreeEntityAction(TreeEntityRepository repository, Class<T> clazz,
			Integer id, Integer targetParentId, int targetIndex) {
		this.repository = repository;
		this.clazz = clazz;
		this.id = id;
		this.targetParentId = targetParentId;
		this.targetIndex = targetIndex;
	}

	@Override
	public void execute() {
		T entity = repository.get(clazz, id);
		index = entity.getIndex();
		parentId = entity.getParent().getId();
		repository.move(clazz, id, targetParentId, targetIndex);
	}

	@Override
	public void undo() {
		repository.move(clazz, id, parentId, index);
	}
}
