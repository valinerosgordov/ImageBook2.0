package ru.imagebook.server.repository;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class SecurityRepositoryImpl extends BaseRepository implements SecurityRepository {
	@Override
	public void enableVendorFilters(Vendor vendor) {
		Session session = getSession();
		session.enableFilter("vendorOrders").setParameter("vendorId", vendor.getId());
		session.enableFilter("vendorBills").setParameter("vendorId", vendor.getId());
		session.enableFilter("vendorUsers").setParameter("vendorId", vendor.getId());
		session.enableFilter("vendorActions").setParameter("vendorId", vendor.getId());
		session.enableFilter("vendorMailing").setParameter("vendorId", vendor.getId());
	}
}
