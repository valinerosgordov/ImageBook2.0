package ru.imagebook.server.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.imagebook.server.service.request.RequestConfig;
import ru.imagebook.server.tools.DateUtil;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.Request;
import ru.imagebook.shared.model.User;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class RequestRepositoryImpl extends BaseRepository implements RequestRepository {
    @Autowired
    private RequestConfig requestConfig;
    
	@SuppressWarnings("unchecked")
	@Override
	public List<Request> loadRequests(int offset, int limit) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Request.class);
		criteria.setProjection(Projections.id());
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.addOrder(org.hibernate.criterion.Order.desc(Request.NUMBER));
		List<Integer> ids = criteria.list();

		if (ids.isEmpty())
			return new ArrayList<Request>();

		criteria = session.createCriteria(Request.class);
		criteria.setFetchMode(Request.ORDERS, FetchMode.JOIN);
		criteria.add(Restrictions.in(Request.ID, ids));
		criteria.addOrder(org.hibernate.criterion.Order.desc(Request.DATE));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public long countRequests() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Request.class);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadNonBasketOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.createAlias(Order.USER, "userAlias");
		criteria.createAlias("userAlias." + User.VENDOR, "vendorAlias");
		criteria.add(Restrictions.eq("vendorAlias.printer", false));
		criteria.add(Restrictions.eq(Order.STATE, OrderState.PRINTING));
		criteria.add(Restrictions.eq(Order.IN_REQUEST_BASKET, false));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.NUMBER));
		return criteria.list();
	}

	@Override
	public long countNonBasketOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setProjection(Projections.rowCount());
		criteria.createAlias(Order.USER, "userAlias");
		criteria.createAlias("userAlias." + User.VENDOR, "vendorAlias");
		criteria.add(Restrictions.eq("vendorAlias.printer", false));
		criteria.add(Restrictions.eq(Order.STATE, OrderState.PRINTING));
		criteria.add(Restrictions.eq(Order.IN_REQUEST_BASKET, false));
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadBasketOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.createAlias(Order.USER, "userAlias");
		criteria.createAlias("userAlias." + User.VENDOR, "vendorAlias");
		criteria.add(Restrictions.eq("vendorAlias.printer", false));
		criteria.add(Restrictions.eq(Order.IN_REQUEST_BASKET, true));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.NUMBER));
		return criteria.list();
	}

	@Override
	public long countBasketOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setProjection(Projections.rowCount());
		criteria.createAlias(Order.USER, "userAlias");
		criteria.createAlias("userAlias." + User.VENDOR, "vendorAlias");
		criteria.add(Restrictions.eq("vendorAlias.printer", false));
		criteria.add(Restrictions.eq(Order.IN_REQUEST_BASKET, true));
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrders(List<Integer> orderIds) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.createAlias(Order.USER, "userAlias");
		criteria.createAlias("userAlias." + User.VENDOR, "vendorAlias");
		criteria.add(Restrictions.eq("vendorAlias.printer", false));
		criteria.add(Restrictions.in(Order.ID, orderIds));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadAllBasketOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.eq(Order.IN_REQUEST_BASKET, true));
		criteria.createAlias(Order.USER, "userAlias");
		criteria.createAlias("userAlias." + User.VENDOR, "vendorAlias");
		criteria.add(Restrictions.eq("vendorAlias.printer", false));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.NUMBER));
		return criteria.list();
	}

	@Override
	public void saveRequest(Request request) {
		Session session = getSession();
		session.save(request);
	}

	@Override
	public int getMaxRequestNumber() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Request.class);
		criteria.setProjection(Projections.max(Request.NUMBER));
		Integer max = (Integer) criteria.uniqueResult();
		return max != null ? max : 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteRequests(List<Integer> requestIds) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Request.class);
		criteria.add(Restrictions.in(Request.ID, requestIds));
		List<Request> requests = criteria.list();
		for (Request request : requests) {
			for (Order<?> order : request.getOrders()) {
				order.setRequest(null);
				order.setState(OrderState.PRINTING);
			}

			session.delete(request);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Request> loadRequests(List<Integer> requestIds) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Request.class);
		criteria.setFetchMode(Request.ORDERS, FetchMode.JOIN);
		criteria.add(Restrictions.in(Request.ID, requestIds));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Request getRequest(int requestId) {
		Session session = getSession();
		return (Request) session.get(Request.class, requestId);
		// Criteria criteria = session.createCriteria(Request.class);
		// criteria.setFetchMode(Request.ORDERS, FetchMode.JOIN);
		// criteria.setFetchMode(Request.ORDERS + "." + Order.PRODUCT,
		// FetchMode.JOIN);
		// criteria.add(Restrictions.idEq(requestId));
		// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		// return (Request) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadTodaysPrintingOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.createAlias(Order.USER, "userAlias");
		criteria.createAlias("userAlias." + User.VENDOR, "vendorAlias");
		criteria.add(Restrictions.eq("vendorAlias.printer", false));
		criteria.add(Restrictions.in(Order.STATE,
			new Integer[] {
				OrderState.PAID,
				OrderState.READY_TO_TRANSFER_PDF,
				OrderState.PDF_TRANSFER_IN_PROGRESS,
				OrderState.PRINTING
			}
		));
		criteria.add(Restrictions.isNull(Order.REQUEST));
		criteria.add(Restrictions.le(Order.PRINT_DATE, new Date()));
		return criteria.list();
	}
	
    @SuppressWarnings("unchecked")
    @Override
    public List<Order<?>> loadLastRequestOrders() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.setFetchMode(Order.REQUEST, FetchMode.JOIN);
        criteria.createAlias(Order.REQUEST, Order.REQUEST);

        TreeSet<Date> dates = new TreeSet<Date>();        
        Calendar calendar = Calendar.getInstance();
        dates.add(DateUtil.setHour(calendar.getTime(), requestConfig.getDeliveryHour1()));
        dates.add(DateUtil.setHour(calendar.getTime(), requestConfig.getDeliveryHour2()));        

        Date nearestRequestDeliveryDate = dates.floor(calendar.getTime());        
        criteria.add(Restrictions.eq(Order.REQUEST + "." + Request.DATE, nearestRequestDeliveryDate));
        return criteria.list();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadLastWeekOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.REQUEST, FetchMode.JOIN);
		criteria.createAlias(Order.REQUEST, Order.REQUEST);
		
		Calendar cal = Calendar.getInstance();
		Date lastDayOfLastWeek = DateUtil.firstDayOfLastWeek(cal);
		
		Date nowTruncated = DateUtils.truncate(cal.getTime(), Calendar.DATE);
		
		criteria.add(Restrictions.ge(Order.REQUEST + "." + Request.DATE, lastDayOfLastWeek));
		criteria.add(Restrictions.lt(Order.REQUEST + "." + Request.DATE, nowTruncated));
		return criteria.list();
	}
}
