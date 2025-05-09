package ru.imagebook.server.repository2;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderImpl;
import ru.imagebook.shared.model.OrderState;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class PdfTransferRepositoryImpl extends BaseRepository implements PdfTransferRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrdersToTransfer() {
		Criteria criteria = createCriteria(Order.class);
		criteria.add(Restrictions.eq(Order.STATE, OrderState.READY_TO_TRANSFER_PDF));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.ID));
		return criteria.list();
	}

	@Override
	public Order<?> getOrder(int orderId) {
		Session session = getSession();
		return (Order<?>) session.get(OrderImpl.class, orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrdersInProgress() {
		Criteria criteria = createCriteria(Order.class);
		criteria.add(Restrictions.eq(Order.STATE, OrderState.PDF_TRANSFER_IN_PROGRESS));
		return criteria.list();
	}

	@Override
	public int getPackageOrdersQuantityCount(String rootOrderName) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.ilike(Order.NUMBER, rootOrderName, MatchMode.END));
		criteria.setProjection(Projections.rowCount());
		return (int) criteria.uniqueResult();
	}
}
