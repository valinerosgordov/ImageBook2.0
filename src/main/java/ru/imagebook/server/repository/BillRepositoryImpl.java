package ru.imagebook.server.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.BillFilter;
import ru.imagebook.shared.model.BillState;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.DsSendState;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class BillRepositoryImpl extends BaseRepository implements BillRepository {
    @Override
    public Bill getBill(int billId) {
        return (Bill) createCriteria(Bill.class)
            .setFetchMode(Bill.ORDERS, FetchMode.JOIN)
            .setFetchMode(Bill.ORDERS + "." + Order.PRODUCT, FetchMode.JOIN)
            .add(Restrictions.idEq(billId))
            .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Bill> loadBills(int offset, int limit, final BillFilter billFilter) {
        Criteria criteria = createCriteria(Bill.class);
        criteria.setProjection(Projections.id());
        if (offset >= 0) {
            criteria.setFirstResult(offset);
        }
        if (limit >= 0) {
            criteria.setMaxResults(limit);
        }
        criteria.addOrder(org.hibernate.criterion.Order.desc(Bill.DATE));

        billFilterCriteria(billFilter, criteria);

        List<Integer> ids = criteria.list();
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        criteria = createCriteria(Bill.class);
        criteria.setFetchMode(Bill.USER, FetchMode.JOIN);
        criteria.setFetchMode(Bill.USER + "." + User.VENDOR, FetchMode.JOIN);
        criteria.setFetchMode(Bill.ORDERS, FetchMode.JOIN);
        criteria.add(Restrictions.in(Bill.ID, ids));
        criteria.addOrder(org.hibernate.criterion.Order.desc(Bill.DATE));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    private void billFilterCriteria(final BillFilter billFilter, final Criteria criteria) {
        if (billFilter != null) {
            Conjunction conjunction = Restrictions.conjunction();
            if (billFilter.getNumBill() != null) {
                conjunction.add(Restrictions.idEq(billFilter.getNumBill()));
            }
            if (billFilter.getDateFrom() != null && billFilter.getDateTo() != null) {
                conjunction.add(Restrictions.between(Bill.DATE, billFilter.getDateFrom(), billFilter.getDateTo()));
            } else if (billFilter.getDateFrom() != null) {
                conjunction.add(Restrictions.ge(Bill.DATE, billFilter.getDateFrom()));
            } else if (billFilter.getDateTo() != null) {
                conjunction.add(Restrictions.le(Bill.DATE, billFilter.getDateTo()));
            }
            if (billFilter.isAdv()) {
                conjunction.add(Restrictions.eq(Bill.ADV, billFilter.isAdv()));
            }
            if (billFilter.getState() != null) {
                conjunction.add(Restrictions.eq(Bill.STATE, billFilter.getState()));
            }
            if (!Strings.isNullOrEmpty(billFilter.getCustomer())) {
                criteria.createAlias(Bill.USER, Bill.USER);
                Criterion like1 = Restrictions.ilike(Bill.USER + "." + User.LAST_NAME, billFilter.getCustomer(), MatchMode.ANYWHERE);
                Criterion like2 = Restrictions.ilike(Bill.USER + "." + User.NAME, billFilter.getCustomer(), MatchMode.ANYWHERE);
                Criterion like3 = Restrictions.ilike(Bill.USER + "." + User.USER_NAME, billFilter.getCustomer(), MatchMode.ANYWHERE);
                LogicalExpression or12 = Restrictions.or(like1, like2);
                conjunction.add(Restrictions.or(or12, like3));
            }
            if (billFilter.getAlbumNumber() != null) {
                criteria.createAlias(Bill.ORDERS, Bill.ORDERS);
                conjunction.add(Restrictions.eq(Bill.ORDERS + "." + Order.PRODUCT + "." + Product.ID, billFilter.getAlbumNumber()));
            }
            criteria.add(conjunction);
        }
    }

    @Override
    public long countBills(final BillFilter billFilter) {
        Criteria criteria = createCriteria(Bill.class);
        if (billFilter != null) {
            criteria.setProjection(Projections.id());
            billFilterCriteria(billFilter, criteria);
            return criteria.list().size();
        } else {
            criteria.setProjection(Projections.rowCount());
            return (Long) criteria.uniqueResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Bill> loadBills(List<Integer> ids) {
        Criteria criteria = createCriteria(Bill.class);
        criteria.setFetchMode(Bill.ORDERS, FetchMode.JOIN);
        criteria.add(Restrictions.in(Bill.ID, ids));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Bill> loadPaidBills(User user) {
        Criteria criteria = createCriteria(Bill.class);
        criteria.add(Restrictions.eq(Bill.USER, user));
        criteria.add(Restrictions.eq(Bill.STATE, BillState.PAID));
        return criteria.list();
    }

    @Override
    public void saveBill(Bill bill) {
        getSession().saveOrUpdate(bill);
    }

    @Override
    public List<Bill> loadReadyToTransferToPickPoint() {
        return loadReadyToTransferToDeliverySystem(DeliveryType.POSTAMATE);
    }

    @Override
    public List<Bill> loadReadyToTransferToSDEK() {
        return loadReadyToTransferToDeliverySystem(DeliveryType.SDEK);
    }

    @SuppressWarnings("unchecked")
    private List<Bill> loadReadyToTransferToDeliverySystem(int deliveryType) {
        return createCriteria(Bill.class)
            .add(Restrictions.and(
                Restrictions.eq(Bill.STATE, BillState.PAID),
                Restrictions.eq(Bill.DELIVERY_TYPE, deliveryType),
                Restrictions.eq(Bill.DS_SEND_STATE, DsSendState.NOT_SENT)
            ))
            .list();
    }
}
