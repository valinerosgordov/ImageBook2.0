package ru.imagebook.server.repository;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class PdfRepositoryImpl extends BaseRepository implements PdfRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadPaidOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.ADDRESS, FetchMode.JOIN);
		criteria.setFetchMode(Order.BILL, FetchMode.JOIN);
		criteria.setFetchMode(Order.FLYLEAF, FetchMode.JOIN);
		criteria.setFetchMode(Order.VELLUM, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.STATE, OrderState.PAID));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrders(Collection<Integer> ids) {
		Criteria criteria = getSession().createCriteria(Order.class);
		criteria.add(Restrictions.in(Order.ID, ids));
		return criteria.list();
	}

	@Override
	public Order<?> getOrder(int orderId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.add(Restrictions.idEq(orderId));
		return (Order<?>) criteria.uniqueResult();
	}
	
	@Override
	public List<Page> getPagesFromLayout(int layoutId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Layout.class);
		criteria.setFetchMode(Layout.PAGES, FetchMode.JOIN);
		criteria.add(Restrictions.idEq(layoutId));
		final Layout layout = (Layout) criteria.uniqueResult();
		return layout.getPages();
	}
}
