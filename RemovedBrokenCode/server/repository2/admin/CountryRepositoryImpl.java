package ru.imagebook.server.repository2.admin;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Country;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class CountryRepositoryImpl extends BaseRepository implements CountryRepository{
	@Override
	public void saveCountry(Country country) {
		Session session = getSession();
		session.save(country);
	}

	@Override
	public Country getCountry(int id) {
		Session session = getSession();
		return (Country) session.get(Country.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteCountries(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Country.class);
		criteria.add(Restrictions.in(Country.ID, ids));
		List<Country> countries = criteria.list();
		for (Country country : countries) {
			session.delete(country);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Country> loadCountries(int offset, int limit) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Country.class);
		criteria.addOrder(Order.asc(Country.NAME));
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Country>) criteria.list();
	}

	@Override
	public long getCountriesCount() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Country.class);
		criteria.addOrder(Order.asc(Country.ID));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Country> loadCountries() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Country.class);
		criteria.addOrder(Order.asc(Country.NAME));
		return (List<Country>) criteria.list();
	}

	@Override
	public Country getCountryByName(String name) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Country.class);
		criteria.add(Restrictions.eq(Country.NAME, name));
		return (Country) criteria.uniqueResult();
	}
}
