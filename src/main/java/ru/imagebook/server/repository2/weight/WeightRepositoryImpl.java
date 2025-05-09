package ru.imagebook.server.repository2.weight;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class WeightRepositoryImpl extends BaseRepository implements WeightRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadYearOrders() {
		Criteria criteria = createCriteria(Order.class);
		criteria.add(Restrictions.eq(Order.STATE, OrderState.SENT));
		criteria.add(Restrictions.isNotNull(Order.QUANTITY));
		DateTime minDate = new DateTime(2010, 3, 1, 0, 0, 0, 0);
		criteria.add(Restrictions.ge(Order.DATE, minDate.toDate()));
		DateTime maxDate = new DateTime(2011, 3, 1, 0, 0, 0, 0);
		criteria.add(Restrictions.le(Order.DATE, maxDate.toDate()));
		criteria.addOrder(org.hibernate.criterion.Order.desc(Order.DATE));
		return criteria.list();
	}
}
