package ru.minogin.data.server.repository;

import org.hibernate.Criteria;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.minogin.data.shared.model.OrderedEntity;

import java.util.List;

@Repository
public class OrderedEntityRepositoryImpl extends HibernateRepository implements OrderedEntityRepository {
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends OrderedEntity> T get(Class<T> clazz, Integer id) {
		return (T) session().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends OrderedEntity> List<T> load(Class<T> clazz) {
		Criteria criteria = criteria(clazz);
		criteria.addOrder(Order.asc(OrderedEntity.INDEX));
		return criteria.list();
	}

	@Override
	public void add(OrderedEntity entity) {
		// TODO lock
		Criteria criteria = criteria(entity.getClass());
		criteria.setProjection(Projections.max(OrderedEntity.INDEX));
		Integer maxIndex = (Integer) criteria.uniqueResult();
		if (maxIndex == null)
			entity.setIndex(0);
		else
			entity.setIndex(maxIndex + 1);
		session().save(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends OrderedEntity> void delete(Class<T> clazz, Integer id) {
		Session session = session();
		OrderedEntity entity = (OrderedEntity) session.get(clazz, id);
		int index = entity.getIndex();
		session.delete(entity);

		Criteria criteria = criteria(clazz);
		criteria.add(Restrictions.gt(OrderedEntity.INDEX, index));
		List<T> entities = criteria.list();
		for (T e : entities) {
			e.setIndex(e.getIndex() - 1);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends OrderedEntity> void move(Class<T> clazz, Integer id,
			int targetIndex) {
		if (targetIndex < 0)
			throw new IndexOutOfBoundsException();

		long count = count(clazz);
		if (targetIndex >= count)
			throw new IndexOutOfBoundsException();
		
		Session session = session();
		T entity = (T) session.get(clazz, id);
		int index = entity.getIndex();

		if (index < targetIndex) {
			Criteria criteria = session.createCriteria(clazz);
			criteria.add(Restrictions.gt(OrderedEntity.INDEX, index));
			criteria.add(Restrictions.le(OrderedEntity.INDEX, targetIndex));
			List<T> entities = criteria.list();
			for (T e : entities) {
				e.setIndex(e.getIndex() - 1);
			}
		} else {
			Criteria criteria = session.createCriteria(clazz);
			criteria.add(Restrictions.ge(OrderedEntity.INDEX, targetIndex));
			criteria.add(Restrictions.lt(OrderedEntity.INDEX, index));
			List<T> entities = criteria.list();
			for (T e : entities) {
				e.setIndex(e.getIndex() + 1);
			}
		}

		entity.setIndex(targetIndex);
	}

	@Override
	public void insert(OrderedEntity entity, int index) {
		insert(entity, index, false);
	}

	@Override
	public void insertPreservingId(OrderedEntity entity, int index) {
		insert(entity, index, true);
	}

	@SuppressWarnings("unchecked")
	private void insert(OrderedEntity entity, int index, boolean preserveId) {
		if (index < 0)
			throw new IndexOutOfBoundsException();

		if (index > count(entity.getClass()))
			throw new IndexOutOfBoundsException();

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(entity.getClass());
		criteria.add(Restrictions.ge(OrderedEntity.INDEX, index));
		List<OrderedEntity> entities = criteria.list();
		for (OrderedEntity e : entities) {
			e.setIndex(e.getIndex() + 1);
		}

		entity.setIndex(index);
		if (preserveId)
			session.replicate(entity, ReplicationMode.EXCEPTION);
		else
			session.save(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends OrderedEntity> T getAt(Class<T> clazz, int index) {
		Criteria criteria = criteria(clazz);
		criteria.add(Restrictions.eq(OrderedEntity.INDEX, index));
		return (T) criteria.uniqueResult();
	}

	@Override
	public <T extends OrderedEntity> long count(Class<T> clazz) {
		Criteria criteria = criteria(clazz);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
}
