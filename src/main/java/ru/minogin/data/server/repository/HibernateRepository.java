package ru.minogin.data.server.repository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateRepository {
	@Autowired
	SessionFactory sessionFactory;

	protected Session session() {
		return sessionFactory.getCurrentSession();
	}

	protected Criteria criteria(Class<?> clazz) {
		return session().createCriteria(clazz);
	}
}
