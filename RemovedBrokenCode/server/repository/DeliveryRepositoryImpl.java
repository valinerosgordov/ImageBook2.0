package ru.imagebook.server.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.User;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class DeliveryRepositoryImpl extends BaseRepository implements
		DeliveryRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadPrintedOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.BILL, FetchMode.JOIN);
		criteria.setFetchMode(Order.BILL + "." + Bill.ORDERS, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.STATE, OrderState.PRINTED));
		criteria.createAlias(Order.ADDRESS, Order.ADDRESS, Criteria.LEFT_JOIN);
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.BILL + "."
				+ Bill.ID));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadDeliveryOrders(Integer deliveryType) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.BILL, FetchMode.JOIN);
		criteria.setFetchMode(Order.BILL + "." + Bill.ORDERS, FetchMode.JOIN);
		criteria.setFetchMode(Order.USER, FetchMode.JOIN);
		criteria.setFetchMode(Order.USER + "." + User.VENDOR, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.STATE, OrderState.DELIVERY));
		if (deliveryType != null)
			criteria.add(Restrictions.eq(Order.DELIVERY_TYPE, deliveryType));
		else
			criteria.add(Restrictions.isNull(Order.DELIVERY_TYPE));
		criteria.createAlias(Order.ADDRESS, Order.ADDRESS, Criteria.LEFT_JOIN);
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.BILL + "."
				+ Bill.ID));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Order<?> findOrder(String number) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.eq(Order.NUMBER, number));
		return (Order<?>) criteria.uniqueResult();
	}

	@Override
	public Order<?> findOrder(int orderId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.idEq(orderId));
		return (Order<?>) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrdersFromBuffer(List<Integer> orderIds) {
		if (orderIds.isEmpty())
			return new ArrayList<Order<?>>();

		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.in(Order.ID, orderIds));
		criteria.add(Restrictions.eq(Order.STATE, OrderState.DELIVERY));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Integer> loadPostBillIds() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.BILL, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.STATE, OrderState.DELIVERY));
		criteria.add(Restrictions.eq(Order.DELIVERY_TYPE, DeliveryType.POST));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.BILL + "." + Bill.ID));
		List<Order<?>> orders = criteria.list();

		Set<Integer> billIds = new LinkedHashSet<Integer>();
		for (Order<?> order : orders) {
			Bill bill = order.getBill();
			if (bill != null)
				billIds.add(bill.getId());
		}
		return billIds;
	}
}
