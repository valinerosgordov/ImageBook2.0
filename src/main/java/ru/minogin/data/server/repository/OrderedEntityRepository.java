package ru.minogin.data.server.repository;

import ru.minogin.data.shared.model.OrderedEntity;

import java.util.List;

public interface OrderedEntityRepository {
	<T extends OrderedEntity> T get(Class<T> clazz, Integer id);

	/**
	 * Loads all entities in their order.
	 */
	<T extends OrderedEntity> List<T> load(Class<T> clazz);

	/**
	 * Add an entity to the end of the list.
	 */
	void add(OrderedEntity entity);

	/**
	 * Inserts an entity at the specified index.
	 */
	void insert(OrderedEntity entity, int index);

	/**
	 * Same as insert but preserving entity id. Useful for deletion undo. In order
	 * this method to work you should use IdPreservingReplicateEventListener.
	 */
	void insertPreservingId(OrderedEntity entity, int index);

	/**
	 * Delete item with specified id.<br/>
	 * <br/>
	 * The delete(id) semantics is used instead of delete(object) because this
	 * method does not work with detached instances instead of Hibernate's
	 * delete(object).
	 */
	<T extends OrderedEntity> void delete(Class<T> clazz, Integer id);

	/**
	 * Move item to the specified position shifting other items.
	 */
	<T extends OrderedEntity> void move(Class<T> clazz, Integer id,
            int targetIndex);

	/**
	 * Get item at the specified index.
	 */
	<T extends OrderedEntity> T getAt(Class<T> clazz, int index);

	/**
	 * Return total number of items.
	 */
	<T extends OrderedEntity> long count(Class<T> clazz);
}
