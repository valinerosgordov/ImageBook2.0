package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.User;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class FlashRepositoryImpl extends BaseRepository implements
		FlashRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.USER, FetchMode.JOIN);
		criteria.setFetchMode(Order.USER + "." + User.VENDOR, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.STATE, OrderState.FLASH_GENERATION));
		return criteria.list();
	}

	@Override
	public Order<?> getOrder(int orderId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.USER, FetchMode.JOIN);
		criteria.setFetchMode(Order.USER + "." + User.VENDOR, FetchMode.JOIN);
		criteria.add(Restrictions.idEq(orderId));
		return (Order<?>) criteria.uniqueResult();
	}

	@Override
	public Order<?> findOrder(String number) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.NUMBER, number));
		return (Order<?>) criteria.uniqueResult();
	}
}
