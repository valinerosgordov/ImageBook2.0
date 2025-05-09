package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Availability;
import ru.imagebook.shared.model.Product;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class CalcRepositoryImpl extends BaseRepository implements CalcRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> loadProductTypes() {
		Criteria criteria = calcProductsCommonsCriteria();
		criteria.setProjection(Projections.property(Product.TYPE));
		criteria.setProjection(Projections.distinct(Projections.property(Product.TYPE)));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> loadProductsByType(Integer type) {
		Criteria criteria = calcProductsCommonsCriteria();
		criteria.add(Restrictions.eq(Product.TYPE, type));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	private Criteria calcProductsCommonsCriteria() {
		Criteria criteria = createCriteria(Product.class);
		criteria.add(Restrictions.eq(Product.AVAILABILITY, Availability.PRESENT));
		criteria.add(Restrictions.eq(Product.NON_CALC, false));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Product.TYPE));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Product.NUMBER));
		return criteria;
	}

	@Override
	public Product getProduct(Integer productId) {
		Criteria criteria = createCriteria(Product.class);
		criteria.setFetchMode(Product.COLOR_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Product.PAGE_LAM_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Product.COVER_LAM_RANGE, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Product.ID, productId));
		return (Product) criteria.uniqueResult();
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> loadProductsOld() {
		Criteria criteria = createCriteria(Product.class);
		criteria.setFetchMode(Product.COLOR_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Product.PAGE_LAM_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Product.COVER_LAM_RANGE, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Product.AVAILABILITY, Availability.PRESENT));
		criteria.add(Restrictions.eq(Product.NON_CALC, false));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Product.TYPE));
		criteria.addOrder(org.hibernate.criterion.Order.asc(Product.NUMBER));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
}
