package ru.minogin.core.server.hibernate.updater;

import java.util.Collection;

import ru.minogin.core.client.collections.CollectionUtil;
import ru.minogin.core.shared.model.BaseEntity;

public abstract class CollectionUpdater2<T extends BaseEntity> {
	protected abstract void add(T entity);

	protected abstract void update(T entity, T modified);

	protected abstract void remove(T entity);

	public void update(Collection<T> stored, Collection<T> modified) {
		for (T storedEntity : stored) {
			T modifiedEntity = CollectionUtil.find(modified, storedEntity);
			if (modifiedEntity != null)
				update(storedEntity, modifiedEntity);
			else
				remove(storedEntity);
		}

		for (T modifiedEntity : modified) {
			if (!stored.contains(modifiedEntity))
				add(modifiedEntity);
		}
	}
}
