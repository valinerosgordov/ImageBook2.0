package ru.imagebook.server.repository2;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderImpl;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class FlashRepositoryImpl extends BaseRepository implements FlashRepository {
	@Override
	public Order<?> getOrder(int orderId) {
		return (Order<?>) getSession().get(OrderImpl.class, orderId);
	}

	@Override
	public Order<?> getOrder(String orderCode) {
		Criteria criteria = createCriteria(Order.class);
		criteria.add(Restrictions.eq(Order.CODE, orderCode));
		return (Order<?>) criteria.uniqueResult();
	}
}
