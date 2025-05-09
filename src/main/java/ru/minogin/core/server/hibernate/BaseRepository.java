package ru.minogin.core.server.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseRepository {
	@PersistenceContext
	protected EntityManager entityManager;

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	protected Criteria createCriteria(Class<?> xClass) {
		return getSession().createCriteria(xClass);
	}

	protected Query createQuery(String queryString) {
		return getSession().createQuery(queryString);
	}
}
