package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Mailing;
import ru.imagebook.shared.model.MailingType;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.collections.XArrayList;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class MailingRepositoryImpl extends BaseRepository implements
		MailingRepository {
	@Override
	public void saveMailing(Mailing mailing) {
		Session session = getSession();
		session.save(mailing);
	}

	@Override
	public Mailing getMailing(int id) {
		Session session = getSession();
		return (Mailing) session.get(Mailing.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mailing> loadMailings() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Mailing.class);
		criteria.addOrder(Order.desc(Mailing.DATE));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteMailings(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Mailing.class);
		criteria.add(Restrictions.in(Mailing.ID, ids));
		List<Mailing> mailings = criteria.list();
		for (Mailing mailing : mailings) {
			session.delete(mailing);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadVendorUsers(Vendor vendor, int type) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq(User.VENDOR, vendor));
		Criteria accountCriteria = criteria.createCriteria(User.ACCOUNT);
		accountCriteria.add(Restrictions.eq(UserAccount.ACTIVE, true));

		if (type == MailingType.NO_PHOTOGRAPHERS)
			criteria.add(Restrictions.eq(User.PHOTOGRAPHER, false));
		else if (type == MailingType.PHOTOGRAPHERS_ONLY)
			criteria.add(Restrictions.eq(User.PHOTOGRAPHER, true));

		criteria.addOrder(Order.asc(User.ID));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadTestUsers() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.in(User.ID, new XArrayList<Integer>(1, 5, 5573,
				7511, 7513, 7514, 7515, 7516)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
}
