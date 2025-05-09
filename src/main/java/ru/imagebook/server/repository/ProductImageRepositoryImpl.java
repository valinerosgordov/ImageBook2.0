package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductImage;

@Repository
public class ProductImageRepositoryImpl extends BaseGenericRepository<ProductImage, Integer>
    implements ProductImageRepository {

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductImage> loadPhotos(Integer productId, int offset, int limit) {
        Criteria criteria = getLoadProductImagesCriteria(productId);
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        return criteria.list();
    }

    @Override
    public long countPhotos(Integer productId) {
        Criteria criteria = createCriteria(ProductImage.class);
        criteria.add(Restrictions.eq(ProductImage.PRODUCT + "." + Product.ID, productId));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductImage> loadPhotos(Integer productId) {
        Criteria criteria = getLoadProductImagesCriteria(productId);
        return criteria.list();
    }

    private Criteria getLoadProductImagesCriteria(Integer productId) {
        Criteria criteria = createCriteria(ProductImage.class);
        criteria.add(Restrictions.eq(ProductImage.PRODUCT + "." + Product.ID, productId));
        criteria.addOrder(Order.asc(ProductImage.NUMBER));
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductImage> loadPhotos(List<Integer> ids) {
        Criteria criteria = createCriteria(ProductImage.class);
        criteria.add(Restrictions.in(ProductImage.ID, ids));
        return  criteria.list();
    }

    @Override
    public void deletePhotos(List<ProductImage> images) {
        for (ProductImage image : images) {
            delete(image);
        }
    }
}
