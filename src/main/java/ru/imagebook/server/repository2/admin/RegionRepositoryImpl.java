package ru.imagebook.server.repository2.admin;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class RegionRepositoryImpl extends BaseRepository implements RegionRepository {

	@Override
	public void saveRegion(Region region) {
		Session session = getSession();
		session.save(region);
	}

	@Override
	public Region getRegion(int id) {
		Session session = getSession();
		return (Region) session.get(Region.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deleteRegions(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Region.class);
		criteria.add(Restrictions.in(Region.ID, ids));
		List<Region> regions = criteria.list();
		for (Region region : regions) {
			session.delete(region);
		}
	}

	@Override
	public long getRegionsCount() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Region.class);
		criteria.addOrder(Order.asc(Region.ID));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Region> loadRegions() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Region.class);
		criteria.addOrder(Order.asc(Region.NAME));
		return (List<Region>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Region> loadRegionsForCountry(Country country) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Region.class);
		criteria.add(Restrictions.eq(Region.COUNTRY, country));
		criteria.addOrder(Order.asc(Region.NAME));
		return (List<Region>) criteria.list();
	}
}
