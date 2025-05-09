package ru.minogin.data.server.repository;

import ru.minogin.data.shared.model.BaseEntity;

public interface EntityRepository {
	<T extends BaseEntity> T get(Class<T> clazz, Integer id);

	void detach(BaseEntity entity);

	void save(BaseEntity object);

	void savePreservingId(BaseEntity object);

	void delete(BaseEntity entity);

	<T extends BaseEntity> void delete(Class<T> clazz, Integer id);
}
