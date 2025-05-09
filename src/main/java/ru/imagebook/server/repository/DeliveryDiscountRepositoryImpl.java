package ru.imagebook.server.repository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.imagebook.shared.model.DeliveryDiscount;
import ru.minogin.core.server.hibernate.BaseRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rifat on 19.01.17.
 */
@Repository
public class DeliveryDiscountRepositoryImpl extends BaseRepository implements DeliveryDiscountRepository {
    DeliveryDiscountRepositoryImpl() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer addDeliveryDiscount(DeliveryDiscount deliveryDiscount) {
        Session session = getSession();
        return ((Integer) session.save(deliveryDiscount));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateDeliveryDiscount(DeliveryDiscount deliveryDiscount) {
        Session session = getSession();
        session.saveOrUpdate(deliveryDiscount);
        session.flush();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DeliveryDiscount> loadDeliveryDiscounts() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(DeliveryDiscount.class);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteDeliveryDiscount(Integer id) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(DeliveryDiscount.class);
        criteria.add(Restrictions.eq(DeliveryDiscount.ID, id));
        DeliveryDiscount deliveryDiscount = (DeliveryDiscount) criteria.uniqueResult();
        session.delete(deliveryDiscount);
        session.flush();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Integer, DeliveryDiscount> getDiscounts(int sum) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(DeliveryDiscount.class);
        criteria.add(Restrictions.le(DeliveryDiscount.SUM, sum));
        List<DeliveryDiscount> deliveryDiscounts = criteria.list();

        HashMap<Integer, DeliveryDiscount> maxDeliveryDiscountMap = new HashMap<>();
        for (DeliveryDiscount deliveryDiscount : deliveryDiscounts) {
            DeliveryDiscount currentMaxDeliveryDiscount = maxDeliveryDiscountMap.get(deliveryDiscount.getDeliveryType());
            if (currentMaxDeliveryDiscount == null || deliveryDiscount.getSum() > currentMaxDeliveryDiscount.getSum()) {
                maxDeliveryDiscountMap.put(deliveryDiscount.getDeliveryType(), deliveryDiscount);
            }
        }

        return maxDeliveryDiscountMap;
    }
}
