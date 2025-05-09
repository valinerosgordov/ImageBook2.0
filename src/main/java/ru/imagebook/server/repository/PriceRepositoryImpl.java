package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Availability;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Product;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class PriceRepositoryImpl extends BaseRepository implements PriceRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Album> loadAlbums() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Album.class);
		criteria.setFetchMode(Product.COLOR_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Product.COVER_LAM_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Product.PAGE_LAM_RANGE, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Product.AVAILABILITY, Availability.PRESENT));
		criteria.addOrder(Order.asc(Product.TYPE));
		criteria.addOrder(Order.asc(Product.NUMBER));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Color> loadColors() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Color.class);
		criteria.addOrder(Order.asc(Color.NUMBER));
		return criteria.list();
	}

	@Override
	public Album getAlbum(Integer productId) {
		Session session = getSession();
		return (Album) session.get(Album.class, productId);
	}
}
