package ru.imagebook.server.repository;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.*;
import ru.minogin.core.server.hibernate.BaseRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hibernate.criterion.MatchMode.ANYWHERE;

@Repository
public class OrderRepositoryImpl extends BaseRepository implements OrderRepository {
    private static final Object[] INCOMING_ORDER_STATES = new Object[]{
            OrderState.OLD_VERSION, OrderState.FLASH_GENERATION, OrderState.FLASH_GENERATED, OrderState.REJECTED,
            OrderState.JPEG_GENERATION, OrderState.JPEG_ONLINE_GENERATION, OrderState.JPEG_BOOK_GENERATION, OrderState.JPEG_GENERATION_ERROR,
            OrderState.FLASH_REGENERATION_ERROR
    };

    @SuppressWarnings("unchecked")
    @Override
    public List<Order<?>> loadIncomingOrders(int userId) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Order.class);
        criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
        criteria.add(Restrictions.eq(Order.USER + "." + User.ID, userId));

        Criterion incomingOrders = Restrictions.in(Order.STATE, INCOMING_ORDER_STATES);
        Criterion newEditorOrders = Restrictions.and(
                Restrictions.eq(Order.STATE, OrderState.NEW),
                // Including external orders from Online Editor
                Restrictions.in(Order.TYPE, new Object[]{OrderType.EDITOR, OrderType.EXTERNAL, OrderType.BOOK})
        );
        criteria.add(Restrictions.or(incomingOrders, newEditorOrders));

        criteria.addOrder(org.hibernate.criterion.Order.asc(Order.ID));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Order<?>> loadBasketOrders(int userId) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Order.class);
        criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
        criteria.setFetchMode(Order.PRODUCT + "." + Product.COLOR_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Order.PRODUCT + "." + Product.COVER_LAM_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Order.PRODUCT + "." + Product.PAGE_LAM_RANGE, FetchMode.JOIN);

        criteria.setFetchMode(Order.FLYLEAF, FetchMode.JOIN);
        criteria.setFetchMode(Order.VELLUM, FetchMode.JOIN);

        criteria.createAlias(Order.PRODUCT, Order.PRODUCT);
        criteria.createAlias(Order.PRODUCT + "." + Product.ALBUM_DISCOUNTS, Product.ALBUM_DISCOUNTS,
                JoinType.LEFT_OUTER_JOIN, Restrictions.eq(UserAlbumDiscount.USER + "." + User.ID, userId));

        criteria.setFetchMode(Order.COLOR, FetchMode.JOIN);
        criteria.setFetchMode(Order.BONUS_CODE, FetchMode.JOIN);
        criteria.add(Restrictions.eq(Order.USER + "." + User.ID, userId));
        criteria.add(Restrictions.eq(Order.STATE, OrderState.BASKET));
        criteria.addOrder(org.hibernate.criterion.Order.asc(Order.ID));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public Order<?> getOrder(int id) {
        Criteria criteria = createCriteria(Order.class);
        criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
        criteria.add(Restrictions.idEq(id));
        return (Order<?>) criteria.uniqueResult();
    }

    @Override
    public Order<?> getOrderByImportId(Integer importId) {
        Criteria criteria = createCriteria(Order.class);
        criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
        criteria.add(Restrictions.eq(Order.IMPORT_ID, importId));
        return (Order<?>) criteria.uniqueResult();
    }

    @Override
    public void save(Bill bill) {
        getSession().save(bill);
    }

    @Override
    public void save(Order<?> order) {
        getSession().save(order);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Bill> loadBills(int userId) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Bill.class);
        criteria.setFetchMode(Bill.ORDERS, FetchMode.JOIN);
        criteria.setFetchMode(Bill.ORDERS + "." + Order.PRODUCT, FetchMode.JOIN);
        criteria.add(Restrictions.eq(Bill.USER + "." + User.ID, userId));
        criteria.add(Restrictions.eq(Bill.STATE, BillState.NEW));
        criteria.addOrder(org.hibernate.criterion.Order.asc(Bill.ID));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Bill> loadBillsWithoutPickupDeliveryNotification() {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Bill.class);
        //TODO criteria.add(Restrictions.eq(Bill.STATE, BillState.NEW));
        criteria.addOrder(org.hibernate.criterion.Order.asc(Bill.ID));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setFetchMode(Bill.ORDERS, FetchMode.JOIN);
        Conjunction conjunction = Restrictions.conjunction();
        conjunction.add(Restrictions.isNotNull(Bill.PICKUP_SEND_STATE_DATE));
        conjunction.add(Restrictions.eq(Bill.NOTIFY_PICKUP, false/*is not notify pickup*/));
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        conjunction.add(Restrictions.le(Bill.PICKUP_SEND_STATE_DATE, calendar.getTime()));
        criteria.add(conjunction);
        criteria.setMaxResults(500/*TODO уточнить размер*/);
        return criteria.list();
    }

    @Override
    public Bill getBill(int id) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Bill.class);
        criteria.add(Restrictions.idEq(id));
        criteria.setFetchMode(Bill.ORDERS, FetchMode.JOIN);
        criteria.setFetchMode(Bill.ORDERS + "." + Order.PRODUCT, FetchMode.JOIN);
        criteria.setFetchMode(Bill.USER, FetchMode.JOIN);
        criteria.setFetchMode(Bill.USER + "." + User.VENDOR, FetchMode.JOIN);
        criteria.setFetchMode(Bill.USER + "." + User.EMAILS, FetchMode.JOIN);
        criteria.setFetchMode(Bill.USER + "." + User.PHONES, FetchMode.JOIN);
        return (Bill) criteria.uniqueResult();
    }

    @Override
    public void deleteBill(Bill bill) {
        getSession().delete(bill);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Order<?>> loadOrders(int offset, int limit, OrderFilter filter, String query) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Order.class);
        criteria.setProjection(Projections.id());
        addRestrictions(criteria, filter, query);
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        criteria.addOrder(org.hibernate.criterion.Order.desc(Order.ID));
        List<Integer> ids = criteria.list();
        if (ids.isEmpty()) {
            return new ArrayList<Order<?>>();
        }

        criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.in(Order.ID, ids));
        criteria.setFetchMode(Order.USER, FetchMode.JOIN);
        criteria.setFetchMode(Order.USER + "." + User.VENDOR, FetchMode.JOIN);
        criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
        criteria.setFetchMode(Order.PRODUCT + "." + Product.COLOR_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Order.PRODUCT + "." + Product.COVER_LAM_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Order.PRODUCT + "." + Product.PAGE_LAM_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Order.COLOR, FetchMode.JOIN);
        criteria.setFetchMode(Order.FLYLEAF, FetchMode.JOIN);
        criteria.setFetchMode(Order.VELLUM, FetchMode.JOIN);
        criteria.setFetchMode(Order.ADDRESS, FetchMode.JOIN);
        criteria.setFetchMode(Order.BONUS_CODE, FetchMode.JOIN);
        criteria.setFetchMode(Order.BONUS_CODE + "." + BonusCode.ACTION, FetchMode.JOIN);
        criteria.setFetchMode(Order.REQUEST, FetchMode.JOIN);
        criteria.setFetchMode(Order.BILL, FetchMode.JOIN);
        criteria.addOrder(org.hibernate.criterion.Order.desc(Order.ID));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public long countOrders(OrderFilter filter, String query) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.setProjection(Projections.rowCount());
        addRestrictions(criteria, filter, query);
        return (Long) criteria.uniqueResult();
    }

    private void addRestrictions(Criteria criteria, OrderFilter filter, String query) {
        criteria.createAlias(Order.USER, Order.USER);
        criteria.createAlias(Order.ADDRESS, Order.ADDRESS, JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias(Order.BILL, Order.BILL, JoinType.LEFT_OUTER_JOIN);

        if (query != null) {
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.ilike(Order.NUMBER, query, ANYWHERE));
            try {
                int findInt = Integer.parseInt(query);
                disjunction.add(Restrictions.eq(Order.BILL + "." + Bill.ID, findInt));
            } catch (NumberFormatException ignored) {
            }
            disjunction.add(Restrictions.ilike(Order.DELIVERY_CODE, query));
            disjunction.add(Restrictions.ilike(Order.ADDRESS + "." + Address.NAME, query, ANYWHERE));
            disjunction.add(Restrictions.ilike(Order.ADDRESS + "." + Address.LAST_NAME, query, ANYWHERE));
            disjunction.add(Restrictions.ilike(Order.ADDRESS + "." + Address.SURNAME, query, ANYWHERE));
            disjunction.add(Restrictions.ilike(Order.ADDRESS + "." + Address.PHONE, query));
            disjunction.add(Restrictions.ilike(Order.USER + "." + User.USER_NAME, query, ANYWHERE));
            disjunction.add(Restrictions.ilike(Order.USER + "." + User.LAST_NAME, query, ANYWHERE));
            disjunction.add(Restrictions.ilike(Order.USER + "." + User.NAME, query, ANYWHERE));
            criteria.add(disjunction);
        }

        if (!filter.getStates().isEmpty())
            criteria.add(Restrictions.in(Order.STATE, filter.getStates()));

        Vendor vendor = filter.getVendor();
        if (vendor != null)
            criteria.add(Restrictions.eq(Order.USER + "." + User.VENDOR, vendor));

        if (!StringUtils.isEmpty(filter.getBonusCode())) {
            criteria.createAlias(Order.BONUS_CODE, Order.BONUS_CODE);
            criteria.add(Restrictions.ilike(Order.BONUS_CODE + "." + BonusCode.CODE, filter.getBonusCode()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> loadProducts() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Product.class);
        criteria.setFetchMode(Product.COLOR_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Product.PAGE_LAM_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Product.COVER_LAM_RANGE, FetchMode.JOIN);
        criteria.addOrder(org.hibernate.criterion.Order.asc(Product.TYPE));
        criteria.addOrder(org.hibernate.criterion.Order.asc(Product.NUMBER));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> loadUsers(int offset, int limit, String query) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.setFetchMode(User.ADDRESSES, FetchMode.JOIN);
        Criterion like1 = Restrictions.ilike(User.LAST_NAME, query, ANYWHERE);
        Criterion like2 = Restrictions.ilike(User.NAME, query, ANYWHERE);
        Criterion like3 = Restrictions.ilike(User.USER_NAME, query, ANYWHERE);
        LogicalExpression or12 = Restrictions.or(like1, like2);
        criteria.add(Restrictions.or(or12, like3));
        criteria.addOrder(org.hibernate.criterion.Order.asc(User.USER_NAME));
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public long countUsers(String query) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.setProjection(Projections.rowCount());
        Criterion like1 = Restrictions.ilike(User.LAST_NAME, query, ANYWHERE);
        Criterion like2 = Restrictions.ilike(User.NAME, query, ANYWHERE);
        Criterion like3 = Restrictions.ilike(User.USER_NAME, query, ANYWHERE);
        LogicalExpression or12 = Restrictions.or(like1, like2);
        criteria.add(Restrictions.or(or12, like3));
        return (Long) criteria.uniqueResult();
    }

    @Override
    public void saveOrder(Order<?> order) {
        Session session = getSession();
        session.save(order);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteOrders(List<Integer> orderIds) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.in(Order.ID, orderIds));
        List<Order<?>> orders = criteria.list();
        for (Order<?> order : orders) {
            session.delete(order);
        }
    }

    @Override
    public void updateOrder(Order<?> order) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.idEq(order.getId()));
        Order<?> sessionOrder = (Order<?>) criteria.uniqueResult();
        session.evict(sessionOrder);

        session.update(order);
    }

    @Override
    public Color getColor(int colorId) {
        Session session = getSession();
        return (Color) session.get(Color.class, colorId);
    }

    @Override
    public BonusCode findCode(String code, Vendor vendor) {
        Session session = getSession();
        Query query = session
                .createQuery("from BonusCode code where (code.action.vendor = :vendor and code.action.codeLength is NULL and code.code = :code)"
                        + " or (code.action.vendor = :vendor and code.action.codeLength is not NULL and code.code = SUBSTRING(:code, 1, code.action.codeLength))");
        query.setParameter("code", code);
        query.setParameter("vendor", vendor);
        return (BonusCode) query.uniqueResult();
    }

    @Override
    public BonusCode getFirstCodeFromLastAction(Vendor vendor) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(BonusAction.class);
        criteria.add(Restrictions.eq(BonusAction.VENDOR, vendor));
        criteria.addOrder(org.hibernate.criterion.Order.desc(BonusAction.DATE_START));
        criteria.setMaxResults(1);
        BonusAction lastAction = (BonusAction) criteria.uniqueResult();

        criteria = session.createCriteria(BonusCode.class);
        criteria.add(Restrictions.eq(BonusCode.ACTION, lastAction));
        criteria.addOrder(org.hibernate.criterion.Order.asc(BonusCode.NUMBER));
        criteria.setMaxResults(1);
        return (BonusCode) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Order<?>> getCodeOrders(BonusCode bonusCode) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq(Order.BONUS_CODE, bonusCode));
        criteria.setFetchMode(Order.USER, FetchMode.JOIN);
        return criteria.list();
    }

    @Override
    public long getTrialOrdersCount(User user) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(Order.USER, user));
        criteria.add(Restrictions.eq(Order.TRIAL, true));
        return (Long) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Order<?>> loadOrders(OrderFilter filter) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
        criteria.setFetchMode(Order.BONUS_CODE, FetchMode.JOIN);
        criteria.setFetchMode(Order.BONUS_CODE + "." + BonusCode.ACTION, FetchMode.JOIN);
        criteria.setFetchMode(Order.BILL, FetchMode.JOIN);
        criteria.setFetchMode(Order.ADDRESS, FetchMode.JOIN);
        criteria.setFetchMode(Order.COLOR, FetchMode.JOIN);
        criteria.setFetchMode(Order.USER, FetchMode.JOIN);
        criteria.setFetchMode(Order.USER + "." + User.VENDOR, FetchMode.JOIN);
        addRestrictions(criteria, filter, null);
        criteria.addOrder(org.hibernate.criterion.Order.desc(Order.ID));
        return criteria.list();
    }

    @Override
    public String getLastOrderNumberByType(int orderType) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.setProjection(Projections.max(Order.NUMBER));
        criteria.add(Restrictions.eq(Order.TYPE, orderType));
        return (String) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vendor> loadVendors() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Vendor.class);
        criteria.addOrder(org.hibernate.criterion.Order.asc(Vendor.NAME));
        return criteria.list();
    }

    @Override
    public void flush() {
        getSession().flush();
    }

    @Override
    public Order<?> getOrderByPublishCode(int publishCode) {
        Criteria criteria = createCriteria(Order.class);
        criteria.add(Restrictions.eq(Order.PUBLISH_CODE, publishCode));
        return (Order<?>) criteria.uniqueResult();
    }

    @Override
    public List<Order<?>> findOrdersByState(Integer state) {
        Criteria criteria = createCriteria(Order.class);
        criteria.add(Restrictions.eq(Order.STATE, state));
        return criteria.list();
    }

    @Override
    public AlbumOrder findOrderbyAlbumId(String albumId) {
        Criteria criteria = createCriteria(Order.class);
        criteria.add(Restrictions.eq(Order.ALBUM_ID, albumId));
        return (AlbumOrder) criteria.uniqueResult();
    }
}
