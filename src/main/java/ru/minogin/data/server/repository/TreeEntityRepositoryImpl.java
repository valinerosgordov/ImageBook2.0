package ru.minogin.data.server.repository;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.minogin.data.shared.model.TreeEntity;

import java.util.List;

@Repository
public class TreeEntityRepositoryImpl extends HibernateRepository implements TreeEntityRepository {
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends TreeEntity<T>> T get(Class<T> clazz, Integer id) {
		return (T) session().get(clazz, id);
	}

	@Override
	public <T extends TreeEntity<T>> void saveTree(T root) {
		root.setParent(null);
		session().save(root);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends TreeEntity<T>> T loadTree(Class<T> clazz) {
		Criteria criteria = criteria(clazz);
		criteria.setFetchMode(TreeEntity.CHILDREN, FetchMode.JOIN);
		criteria.add(Restrictions.isNull(TreeEntity.PARENT));
		T root = (T) criteria.uniqueResult();

		fetchChildrenTree(root);

		return root;
	}

	@Override
	public <T extends TreeEntity<T>> void fetchChildrenTree(T entity) {
		if (entity != null) {
			for (T child : entity.getChildren()) {
				fetchChildrenTree(child);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends TreeEntity<T>> void add(Integer parentId, T entity) {
		T parent = (T) session().get(entity.getClass(), parentId);
		parent.add(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends TreeEntity<T>> void insert(Integer parentId, T entity,
			int index) {
		T parent = (T) session().get(entity.getClass(), parentId);
		List<T> children = parent.getChildren();
		children.add(index, entity);
		entity.setParent(parent);
		for (int i = index; i < children.size(); i++) {
			T t = children.get(i);
			t.setIndex(i);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends TreeEntity<T>> void delete(Class<T> clazz, Integer id) {
		Session session = session();
		T entity = (T) session.get(clazz, id);
		session.delete(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends TreeEntity<T>> void move(Class<T> clazz, Integer id,
			Integer targetId, int index) {
		Session session = session();
		T entity = (T) session.get(clazz, id);
		T sourceParent = entity.getParent();
		List<T> sourceChildren = sourceParent.getChildren();
		int sourceIndex = sourceChildren.indexOf(entity);
		sourceChildren.remove(entity);
		for (int i = sourceIndex; i < sourceChildren.size(); i++) {
			T t = sourceChildren.get(i);
			t.setIndex(i);
		}

		T target = (T) session.get(clazz, targetId);
		List<T> targetChildren = target.getChildren();
		targetChildren.add(index, entity);
		entity.setParent(target);
		for (int i = index; i < targetChildren.size(); i++) {
			T t = targetChildren.get(i);
			t.setIndex(i);
		}
	}
}