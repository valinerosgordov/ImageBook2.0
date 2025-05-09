package ru.minogin.data.server.repository;

import ru.minogin.data.shared.model.TreeEntity;

public interface TreeEntityRepository {
	<T extends TreeEntity<T>> T get(Class<T> clazz, Integer id);

	<T extends TreeEntity<T>> void saveTree(T root);

	<T extends TreeEntity<T>> T loadTree(Class<T> clazz);

	<T extends TreeEntity<T>> void add(Integer parentId, T entity);

	<T extends TreeEntity<T>> void insert(Integer parentId, T entity, int index);

	/**
	 * Delete item with specified id.
	 * 
	 * The delete(id) semantics is used instead of delete(object) because this
	 * method does not work with detached instances instead of Hibernate's
	 * delete(object).
	 */
	<T extends TreeEntity<T>> void delete(Class<T> clazz, Integer id);

	<T extends TreeEntity<T>> void move(Class<T> clazz, Integer id,
            Integer targetId, int index);

	<T extends TreeEntity<T>> void fetchChildrenTree(T entity);
}
