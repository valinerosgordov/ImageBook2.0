package ru.imagebook.server.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class LoadRepositoryImpl extends BaseRepository implements
		LoadRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<String> loadExistingNumbers(Collection<String> numbers) {
		if (numbers.isEmpty())
			return new ArrayList<String>();

		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setProjection(Projections.property(Order.NUMBER));
		criteria.add(Restrictions.in(Order.NUMBER, numbers));
		return criteria.list();
	}
}
