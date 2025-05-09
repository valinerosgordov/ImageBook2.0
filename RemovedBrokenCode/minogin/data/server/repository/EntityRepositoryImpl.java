package ru.minogin.data.server.repository;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.minogin.data.shared.model.BaseEntity;

@Repository
public class EntityRepositoryImpl extends HibernateRepository implements EntityRepository {
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> T get(Class<T> clazz, Integer id) {
		return (T) session().get(clazz, id);
	}

	@Override
	public void detach(BaseEntity entity) {
		session().evict(entity);
	}

	@Override
	public void save(BaseEntity entity) {
		session().save(entity);
	}

	@Override
	public void delete(BaseEntity entity) {
		session().delete(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> void delete(Class<T> clazz, Integer id) {
		Session session = session();
		T entity = ((T) session.get(clazz, id));
		session.delete(entity);
	}

	@Override
	public void savePreservingId(BaseEntity entity) {
		session().replicate(entity, ReplicationMode.EXCEPTION);
	}
}
