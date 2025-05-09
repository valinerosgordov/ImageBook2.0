package ru.imagebook.server.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.AdminSettings;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.OrderFilter;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAlbumDiscount;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class
UserRepositoryImpl extends BaseRepository implements UserRepository {

    private static final int USER_EMAILS_FETCH_SIZE = 1000;

	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadUsers(int offset, int limit, String query) {
		Session session = getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.setProjection(Projections.id());
		criteria.createAlias(User.PHONES, User.PHONES, Criteria.LEFT_JOIN);
		criteria.createAlias(User.ROLES, User.ROLES);
		criteria.add(Restrictions.eq(User.ROLES + "." + Role.TYPE, Role.USER));
		if (query != null)
			criteria.add(createQueryCriterion(query));
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.addOrder(Order.asc(User.ID));
		List<Integer> ids = criteria.list();
		if (ids.isEmpty())
			return new ArrayList<User>();

		criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.in(User.ID, ids));
		criteria.setFetchMode(User.EMAILS, FetchMode.JOIN);
		criteria.createAlias(User.PHONES, User.PHONES, Criteria.LEFT_JOIN);
		criteria.createAlias(User.ACCESSED_PRODUCTS, User.ACCESSED_PRODUCTS, Criteria.LEFT_JOIN);
		criteria.setFetchMode(User.ALBUM_DISCOUNTS, FetchMode.JOIN);
        criteria.setFetchMode(User.ALBUM_DISCOUNTS + "." + UserAlbumDiscount.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(User.ADDRESSES, FetchMode.JOIN);
		criteria.setFetchMode(User.ORDERS, FetchMode.JOIN);
		criteria.setFetchMode(User.ORDERS + "." + ru.imagebook.shared.model.Order.BONUS_CODE, FetchMode.JOIN);
		criteria.setFetchMode(User.ORDERS + "." + ru.imagebook.shared.model.Order.BONUS_CODE + "." + BonusCode.ACTION,
            FetchMode.JOIN);
		criteria.setFetchMode(User.VENDOR, FetchMode.JOIN);
		criteria.addOrder(Order.asc(User.ID));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	private Disjunction createQueryCriterion(String query) {
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.ilike(User.USER_NAME, query, MatchMode.ANYWHERE));
		disjunction.add(Restrictions.ilike(User.LAST_NAME, query, MatchMode.ANYWHERE));
		disjunction.add(Restrictions.ilike(User.NAME, query, MatchMode.ANYWHERE));
		disjunction.add(Restrictions.ilike(User.SURNAME, query, MatchMode.ANYWHERE));
		disjunction.add(Restrictions.ilike(User.PHONES + "." + Phone.PHONE, query, MatchMode.ANYWHERE));
		return disjunction;
	}

	@Override
	public long countUsers(String query) {
		Criteria criteria = createCriteria(User.class);
		criteria.createAlias(User.ROLES, User.ROLES);
		criteria.createAlias(User.PHONES, User.PHONES, Criteria.LEFT_JOIN);
		criteria.add(Restrictions.eq(User.ROLES + "." + Role.TYPE, Role.USER));
		if (query != null)
			criteria.add(createQueryCriterion(query));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	@Override
	public Integer addUser(User user) {
        return (Integer) getSession().save(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteUsers(List<Integer> userIds) {
		Session session = getSession();
		Criteria criteria = createCriteria(User.class);
		criteria.add(Restrictions.in(User.ID, userIds));
		List<User> users = criteria.list();
		for (User user : users) {
			session.delete(user);
		}
	}

	@Override
	public User getUserLite(int userId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.setFetchMode(User.ROLES, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS + "." + AdminSettings.ORDER_FILTER, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS + "." + AdminSettings.ORDER_FILTER + "." + OrderFilter.STATES, FetchMode.JOIN);
		criteria.setFetchMode(User.VENDOR, FetchMode.JOIN);
		criteria.add(Restrictions.idEq(userId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		User user = (User) criteria.uniqueResult();
		session.evict(user.getVendor());
		return user;
	}

	@Override
	public User getUser(int userId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.setFetchMode(User.ROLES, FetchMode.JOIN);
		criteria.setFetchMode(User.EMAILS, FetchMode.JOIN);
		criteria.setFetchMode(User.PHONES, FetchMode.JOIN);
		criteria.setFetchMode(User.ADDRESSES, FetchMode.JOIN);
		criteria.setFetchMode(User.ORDERS, FetchMode.JOIN);
		criteria.setFetchMode(User.ORDERS, FetchMode.JOIN);
		criteria.setFetchMode(User.ORDERS + "." + ru.imagebook.shared.model.Order.BONUS_CODE, FetchMode.JOIN);
		criteria.setFetchMode(User.ORDERS + "." + ru.imagebook.shared.model.Order.BONUS_CODE + "." + BonusCode.ACTION,
			FetchMode.JOIN);
		criteria.setFetchMode(User.ORDERS + "." + ru.imagebook.shared.model.Order.COLOR, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS + "." + AdminSettings.ORDER_FILTER, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS + "." + AdminSettings.ORDER_FILTER + "." + OrderFilter.STATES, FetchMode.JOIN);
		criteria.setFetchMode(User.VENDOR, FetchMode.JOIN);
		criteria.add(Restrictions.idEq(userId));
		criteria.createAlias(User.ACCESSED_PRODUCTS, User.ACCESSED_PRODUCTS, Criteria.LEFT_JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		User user = (User) criteria.uniqueResult();
		session.evict(user.getVendor());
		return user;
	}

    @Override
    public User getUser(String username) {
        Criteria criteria = createCriteria(User.class);
        criteria.setFetchMode(User.ROLES, FetchMode.JOIN);
        criteria.setFetchMode(User.ORDERS, FetchMode.JOIN);
        criteria.setFetchMode(User.VENDOR, FetchMode.JOIN);
        criteria.add(Restrictions.eq(User.USER_NAME, username));
        return (User) criteria.uniqueResult();
    }

    @Override
	public User loadUser(int userId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.setFetchMode(User.ROLES, FetchMode.JOIN);
		criteria.setFetchMode(User.EMAILS, FetchMode.JOIN);
		criteria.setFetchMode(User.PHONES, FetchMode.JOIN);
		criteria.setFetchMode(User.ADDRESSES, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS + "." + AdminSettings.ORDER_FILTER, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS + "." + AdminSettings.ORDER_FILTER + "." + OrderFilter.STATES, FetchMode.JOIN);
		criteria.setFetchMode(User.SETTINGS + "." + AdminSettings.ORDER_FILTER + "." + OrderFilter.VENDOR, FetchMode.JOIN);
		criteria.setFetchMode(User.VENDOR, FetchMode.JOIN);
		criteria.add(Restrictions.idEq(userId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		User user = (User) criteria.uniqueResult();
		session.evict(user.getVendor());
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadUsers(List<Integer> userIds) {
		Criteria criteria = createCriteria(User.class);
		criteria.add(Restrictions.in(User.ID, userIds));
		return criteria.list();
	}

    @SuppressWarnings("unchecked")
    @Override
    public ScrollableResults loadActiveUserEmails(Vendor vendor, boolean commonUsers, boolean photographers) {
        String hql = "from User u inner join fetch u.account as a inner join fetch u.emails as e "
            + "where a.active = true and e.active = true and u.registered = true and u.vendor = :vendor "
            + "and u.skipMailing = false";

        if (commonUsers && !photographers) { // no photographers
            hql += " and u.photographer = false";
        } else if (!commonUsers && photographers) { // only photographers
            hql += " and u.photographer = true";
        }

        return createQuery(hql)
            .setParameter("vendor", vendor)
            .setReadOnly(true)
            .setCacheMode(CacheMode.IGNORE)
            .setFetchSize(USER_EMAILS_FETCH_SIZE)
            .scroll(ScrollMode.FORWARD_ONLY);
    }

    @Override
	public Email getEmail(int emailId) {
		Session session = getSession();
		return (Email) session.get(Email.class, emailId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersByEmail(String email, Vendor vendor) {
		Criteria criteria = createCriteria(User.class);
		criteria.add(Restrictions.eq(User.VENDOR, vendor));
		Criteria emailsCriteria = criteria.createCriteria(User.EMAILS);
		emailsCriteria.add(Restrictions.eq(Email.EMAIL, email));
		emailsCriteria.add(Restrictions.eq(Email.ACTIVE, true));
		return criteria.list();
	}

	@Override
	public User getUser(String userName, Vendor vendor) {
		Criteria criteria = createCriteria(User.class);
		criteria.add(Restrictions.eq(User.USER_NAME, userName));
		criteria.add(Restrictions.eq(User.VENDOR, vendor));
		return (User) criteria.uniqueResult();
	}

    @Override
    public Address getAddress(int addressId) {
        Session session = getSession();
        return (Address) session.get(Address.class, addressId);
    }

	@Override
	public void saveUser(User user) {
		Session session = getSession();
		session.merge(user);
	}

	@Override
	public void flush() {
        getSession().flush();
	}

    @Override
    public void detach(User user) {
        getSession().evict(user);
    }
}
