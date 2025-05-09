package ru.imagebook.server.repository;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Product;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.hibernate.BaseRepository;

import java.util.List;

@Repository
public class ProductRepositoryImpl extends BaseRepository implements ProductRepository {
    @Override
    public Product getProduct(int productId) {
        Criteria criteria = createCriteria(Product.class);
        criteria.add(Restrictions.idEq(productId));
        return (Product) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Album> loadAlbums() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Album.class);
        criteria.addOrder(Order.asc(Product.TYPE));
        criteria.addOrder(Order.asc(Product.NUMBER));
        criteria.createAlias(Product.ACCESSED_USERS, Product.ACCESSED_USERS, JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode(Product.COLOR_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Product.COVER_LAM_RANGE, FetchMode.JOIN);
        criteria.setFetchMode(Product.PAGE_LAM_RANGE, FetchMode.JOIN);
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
    public void saveAlbum(Album album) {
        Session session = getSession();
        session.save(album);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteAlbums(List<Integer> ids) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Album.class);
        criteria.add(Restrictions.in(Product.ID, ids));
        List<Album> albums = criteria.list();
        for (Album album : albums) {
            session.delete(album);
        }
        session.flush();
    }

    @Override
    public void updateAlbum(Album album) {
        Session session = getSession();
        session.merge(album);
    }

    @Override
    public void saveOrUpdate(Color color) {
        Session session = getSession();
        session.saveOrUpdate(color);
    }

    @Override
    public void deleteColors(List<Color> colors) {
        Session session = getSession();
        for (Color color : colors) {
            session.delete(color);
        }
        session.flush();
    }

    @Override
    public Album getAlbumByProductTypeAndNumber(int type, int number) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Album.class);
        criteria.add(Restrictions.eq(Product.TYPE, type));
        criteria.add(Restrictions.eq(Product.NUMBER, number));
        return (Album) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> loadProducts(int offset, int limit, String query) {
        Criteria criteria = createProductQueryCriteria(query);
        criteria.addOrder(Order.asc(Product.TYPE));
        criteria.addOrder(Order.asc(Product.NUMBER));
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public long countProducts(String query) {
        Criteria criteria = createProductQueryCriteria(query);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private Criteria createProductQueryCriteria(String query) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Product.class);
        if (StringUtils.isNotEmpty(query)) {
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.ilike(Product.NAME + "." + Locales.RU, query, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike(Product.ARTICLE, query, MatchMode.ANYWHERE));
            criteria.add(disjunction);
        }
        return criteria;
    }
}
