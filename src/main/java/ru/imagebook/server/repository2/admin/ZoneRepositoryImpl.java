package ru.imagebook.server.repository2.admin;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Zone;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class ZoneRepositoryImpl extends BaseRepository implements ZoneRepository{
	@Override
	public void saveZone(Zone zone) {
		Session session = getSession();
		session.save(zone);
	}

	@Override
	public Zone getZone(int id) {
		Session session = getSession();
		return (Zone) session.get(Zone.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteZones(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Zone.class);
		criteria.add(Restrictions.in(Zone.ID, ids));
		List<Zone> zones = criteria.list();
		for (Zone zone : zones) {
			session.delete(zone);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Zone> loadZones(int offset, int limit) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Zone.class);
		criteria.addOrder(Order.asc(Zone.ID));
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Zone>) criteria.list();
	}

	@Override
	public long getZonesCount() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Zone.class);
		criteria.addOrder(Order.asc(Zone.ID));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteAllZones() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Zone.class);
		List<Zone> zones = criteria.list();
		for (Zone zone : zones) {
			session.delete(zone);
		}
	}

}
