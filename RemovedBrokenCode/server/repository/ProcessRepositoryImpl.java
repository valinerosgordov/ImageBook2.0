package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.User;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class ProcessRepositoryImpl extends BaseRepository implements ProcessRepository {
    private static final Object[] PROCESSING_ORDER_STATES = new Object[]{
        OrderState.PAID, OrderState.READY_TO_TRANSFER_PDF, OrderState.PDF_TRANSFER_IN_PROGRESS,
        OrderState.PRINTING, OrderState.FINISHING, OrderState.PRINTED, OrderState.DELIVERY
    };

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrders(int userId) {
		Criteria criteria = getSession().createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.BILL, FetchMode.JOIN);
		criteria.createAlias(Order.ADDRESS, Order.ADDRESS, JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq(Order.USER + "." + User.ID, userId));
		criteria.add(Restrictions.in(Order.STATE, PROCESSING_ORDER_STATES));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.DATE));
		return criteria.list();
	}
}
