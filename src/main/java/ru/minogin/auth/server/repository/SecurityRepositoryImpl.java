package ru.minogin.auth.server.repository;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.minogin.auth.shared.model.base.BaseUser;
import ru.minogin.auth.shared.model.base.Role;

import javax.persistence.EntityManagerFactory;

@Repository
public class SecurityRepositoryImpl<U extends BaseUser> implements SecurityRepository<U> {
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	private final Class<? extends BaseUser> clazz;

	public SecurityRepositoryImpl(Class<? extends BaseUser> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public U getUser(String username) {
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(clazz);
		criteria.setFetchMode(BaseUser.ROLES, FetchMode.JOIN);
		criteria.setFetchMode(BaseUser.ROLES + "." + Role.PERMISSIONS, FetchMode.JOIN);
		criteria.setFetchMode(BaseUser.PERMISSIONS, FetchMode.JOIN);
		criteria.add(Restrictions.eq(BaseUser.USERNAME, username));
		return (U) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public U getUser(int userId) {
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(clazz);
		criteria.setFetchMode(BaseUser.ROLES, FetchMode.JOIN);
		criteria.setFetchMode(BaseUser.ROLES + "." + Role.PERMISSIONS, FetchMode.JOIN);
		criteria.setFetchMode(BaseUser.PERMISSIONS, FetchMode.JOIN);
		criteria.add(Restrictions.idEq(userId));
		return (U) criteria.uniqueResult();
	}

	@Override
	public Role getRole(String key) {
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Role.class);
		criteria.add(Restrictions.eq(Role.KEY, key));
		return (Role) criteria.uniqueResult();
	}
}
