package ru.imagebook.server.repository;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.BillState;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.StorageState;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class CleanRepositoryImpl extends BaseRepository implements CleanRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Order<?>> loadStorageOrders() {
		Criteria criteria = createCriteria(Order.class);
		criteria.setFetchMode(Order.USER, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Order.STORAGE_STATE, StorageState.STORAGE));
		return criteria.list();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Bill> loadNotPaidBillsCreatedLessThan(Date date) {
        Criteria criteria = createCriteria(Bill.class);
        criteria.add(Restrictions.ne(Bill.STATE, BillState.PAID));
        criteria.add(Restrictions.lt(Bill.DATE, date));
        return criteria.list();
    }
}
