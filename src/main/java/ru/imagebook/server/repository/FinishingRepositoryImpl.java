package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class FinishingRepositoryImpl extends BaseRepository implements FinishingRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.COLOR, FetchMode.JOIN);
		criteria.setFetchMode(Order.FLYLEAF, FetchMode.JOIN);
		criteria.setFetchMode(Order.VELLUM, FetchMode.JOIN);
		criteria.add(Restrictions.or(Restrictions.eq(Order.STATE, OrderState.PRINTING),
				Restrictions.eq(Order.STATE, OrderState.FINISHING)));
		criteria.createAlias(Order.PRODUCT, Order.PRODUCT);
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.PRINT_DATE));
		return criteria.list();
	}

	@Override
	public Order<?> findOrder(int orderId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.idEq(orderId));
		criteria.add(Restrictions.or(Restrictions.eq(Order.STATE, OrderState.PRINTING),
				Restrictions.eq(Order.STATE, OrderState.FINISHING)));
		return (Order<?>) criteria.uniqueResult();
	}

	@Override
	public Order<?> findPrintingOrder(int orderId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.idEq(orderId));
		criteria.add(Restrictions.eq(Order.STATE, OrderState.PRINTING));
		return (Order<?>) criteria.uniqueResult();
	}
}
