package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Availability;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.editor.*;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class EditorRepositoryImpl extends BaseRepository implements
		EditorRepository {
	@Override
	public Product getProduct(int productId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.idEq(productId));
		return (Product) criteria.uniqueResult();
	}

	@Override
	public void saveOrder(AlbumOrder order) {
		Session session = getSession();
		session.save(order);
	}


	@Override
	public void deleteOrder(final Order<?> order) {
		Session session = getSession();
		session.delete(order);
	}

	@Override
	public void saveLayout(final Layout layout, final int orderId) {
		Session session = getSession();
		final Query query = session.createQuery(" UPDATE OrderImpl SET layout.id = :layoutId WHERE id = :orderId");
		query.setParameter("layoutId", layout.getId());
		query.setParameter("orderId", orderId);
		query.executeUpdate();
	}

	@Override
	public Color getColor(int number) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Color.class);
		criteria.add(Restrictions.eq(Color.NUMBER, number));
		return (Color) criteria.uniqueResult();
	}

	@Transactional
	@Override
	public int nextCounter() {
		Session session = getSession();
		int number = 17000000;
		EditorCounter counter = (EditorCounter) session.get(EditorCounter.class, 1);
		if (counter == null) {
			counter = new EditorCounter();
			counter.setNumber(number);
			session.save(counter);
		}
		else {
			number = counter.getNumber() + 1;
			counter.setNumber(number);
		}

		return number;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadOrders(User user) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT + "." + Order.LAYOUTS, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.USER, user));
		criteria.add(Restrictions.eq(Order.TYPE, OrderType.EDITOR));
		criteria.add(Restrictions.in(Order.STATE, new Object[]{OrderState.NEW, OrderState.FLASH_GENERATED}));
		criteria.addOrder(org.hibernate.criterion.Order.desc(Order.DATE));
		return criteria.list();
	}

	@Override
	public boolean requiredShowNotificationMessage(final Integer userId, final int notificationTypeId) {
		final Session session = getSession();
		final Query query = session.createQuery(" select count (en.id) from EditorNotification en where en.user = :userId and en.type = :typeId");
		query.setParameter("userId", userId);
		query.setParameter("typeId", notificationTypeId);
		return (Long)query.uniqueResult() > 0 ? false : true;
	}

	@Override
	public void cancelFromNotification(final Integer userId, final int notificationTypeId) {
		final Session session = getSession();
		session.save(new EditorNotification(userId, notificationTypeId));
		/*final Query query = session.createSQLQuery(" insert into EditorNotification(user_id, type) values(:userId, :typeId");
		query.setParameter("userId", userId);
		query.setParameter("typeId", notificationTypeId);
		query.executeUpdate();*/
	}

	@Override
	public Order<?> getOrder(int orderId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.idEq(orderId));
		return (Order<?>) criteria.uniqueResult();
	}

	@Override
	public Order<?> loadOrderWithLayout(int orderId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT + "." + Order.LAYOUTS, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT + "." + Layout.PAGES, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT + "." + Layout.PAGES + "."
				+ Page.COMPONENTS, FetchMode.JOIN);
		criteria.add(Restrictions.idEq(orderId));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Order.LAYOUTS));
		return (Order<?>) criteria.uniqueResult();
	}

	@Override
	public Image getImage(int id) {
		Session session = getSession();
		return (Image) session.get(ImageImpl.class, id);
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> loadProducts() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.setFetchMode(Product.COLOR_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Product.PAGE_LAM_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Product.COVER_LAM_RANGE, FetchMode.JOIN);
		// criteria.add(Restrictions
		// .not(Restrictions.eq(Product.TYPE, ProductType.EVERFLAT_WHITE_MARGINS)));
		// criteria.add(Restrictions.not(Restrictions.eq(Product.TYPE,
		// ProductType.HARD_COVER_WHITE_MARGINS)));
		criteria.add(Restrictions.eq(Product.AVAILABILITY, Availability.PRESENT));
		criteria.add(Restrictions.eq(Product.NON_EDITOR, false));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Product.TYPE));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Product.NUMBER));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadProcessingOrders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.USER, FetchMode.JOIN);
		criteria.setFetchMode(Order.USER + "." + User.VENDOR, FetchMode.JOIN);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT + "." + Layout.PAGES, FetchMode.JOIN);
		criteria.setFetchMode(Order.LAYOUT + "." + Layout.PAGES + "."
				+ Page.COMPONENTS, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.TYPE, OrderType.EDITOR));
		criteria.add(Restrictions.eq(Order.STATE, OrderState.JPEG_GENERATION));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadAllOrders(User user) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFetchMode(Order.PRODUCT, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.USER, user));
		criteria.add(Restrictions.eq(Order.TYPE, OrderType.EDITOR));
		criteria.addOrder(org.hibernate.criterion.Order.desc(Order.DATE));
		return criteria.list();
	}

	@Override
	public Component getComponent(int componentId) {
		Session session = getSession();
		return (Component) session.get(ComponentImpl.class, componentId);
	}

	@Override
	public Album getAlbum(int type, int number) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Album.class);
		criteria.add(Restrictions.eq(Album.TYPE, type));
		criteria.add(Restrictions.eq(Album.NUMBER, number));
		return (Album) criteria.uniqueResult();
	}
}
