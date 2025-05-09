package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class VendorRepositoryImpl extends BaseRepository implements VendorRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Vendor> loadVendors() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Vendor.class);
		criteria.addOrder(Order.asc(Vendor.NAME));
		return criteria.list();
	}

	@Override
	public void saveVendor(Vendor vendor) {
		Session session = getSession();
		session.save(vendor);
	}

	@Override
	public void updateVendor(Vendor vendor) {
		Session session = getSession();
		session.update(vendor);
	}

	@Override
	public Vendor getVendorByCustomerId(String customerId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Vendor.class);
		criteria.add(Restrictions.eq(Vendor.CUSTOMER_ID, customerId));
		return (Vendor) criteria.uniqueResult();
	}

	@Override
	public Vendor getVendorById(Integer id) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Vendor.class);
		criteria.add(Restrictions.eq(Vendor.ID, id));
		return (Vendor) criteria.uniqueResult();
	}

	@Override
	public Vendor getVendorByKey(String key) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Vendor.class);
		criteria.add(Restrictions.eq(Vendor.KEY, key));
		return (Vendor) criteria.uniqueResult();
	}

	@Override
	public Vendor getVendorByType(int type) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Vendor.class);
		criteria.add(Restrictions.eq(Vendor.TYPE, type));
		return (Vendor) criteria.uniqueResult();
	}

	@Override
	public Vendor getVendorBySite(String site) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Vendor.class);
		criteria.add(Restrictions.or(Restrictions.eq(Vendor.SITE, site), Restrictions.eq(Vendor.ENGLISH_DOMAIN, site)));
		return (Vendor) criteria.uniqueResult();
	}
}
