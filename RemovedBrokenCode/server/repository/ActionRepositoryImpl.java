package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.ObjectDeletedException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class ActionRepositoryImpl extends BaseRepository implements ActionRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<BonusAction> loadActions(final String query) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(BonusAction.class);
		criteria.addOrder(Order.desc(BonusAction.DATE_START));
		addCodeSearchRestrictions(criteria, query);
		return criteria.list();
	}

	private void addCodeSearchRestrictions(Criteria criteria, String query) {

		if (query != null && !query.trim().isEmpty()) {
			criteria.createAlias(BonusAction.CODES, BonusAction.CODES, JoinType.INNER_JOIN);
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike(BonusAction.CODES + "." + BonusCode.CODE, query, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
	}

	@Override
	public void saveAction(BonusAction action) {
		Session session = getSession();
		session.save(action);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteActions(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(BonusAction.class);
		criteria.add(Restrictions.in(BonusAction.ID, ids));
		List<BonusAction> actions = criteria.list();
		for (BonusAction action : actions) {
			session.delete(action);
		}
	}

	@Override
	public void deleteBonusCodes(List<Integer> ids) {
		try {
			Session session = getSession();
			Criteria criteria = session.createCriteria(BonusCode.class);
			criteria.add(Restrictions.in(BonusCode.ID, ids));
			List<BonusCode> codes = criteria.list();
			for (BonusCode code : codes) {
				session.delete(code);
			}
			session.flush();
		} catch (ObjectDeletedException e) {
			throw new DataIntegrityViolationException(e.getMessage());
		}
	}

	@Override
	public BonusAction getAction(int id) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(BonusAction.class);
		criteria.add(Restrictions.eq(BonusAction.ID, Integer.valueOf(id)));
		return (BonusAction) criteria.uniqueResult();
//		Session session = getSession();
//		return (BonusAction) session.get(BonusAction.class, id);
	}

	@Override
	public long countCodesStartingWith(String prefix) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(BonusCode.class);
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.like(BonusCode.CODE, prefix, MatchMode.START));
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StatusRequest> loadStatusRequests() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(StatusRequest.class);
		criteria.setFetchMode(StatusRequest.USER, FetchMode.JOIN);
		criteria.addOrder(Order.desc(StatusRequest.DATE));
		return criteria.list();
	}

	@Override
	public void save(StatusRequest request) {
		Session session = getSession();
		session.save(request);
	}

	@Override
	public StatusRequest getLastRequest(User user) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(StatusRequest.class);
		criteria.add(Restrictions.eq(StatusRequest.USER, user));
		criteria.addOrder(Order.desc(StatusRequest.DATE));
		criteria.setMaxResults(1);
		return (StatusRequest) criteria.uniqueResult();
	}

	@Override
	public StatusRequest getRequest(int id) {
		Session session = getSession();
		return (StatusRequest) session.get(StatusRequest.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BonusAction> loadActions(final Vendor vendor, final String query) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(BonusAction.class);
		criteria.add(Restrictions.eq(BonusAction.VENDOR, vendor));
		criteria.addOrder(Order.desc(BonusAction.DATE_START));
		addCodeSearchRestrictions(criteria, query);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BonusAction getBonusActionWithAlbums(final BonusAction bonusAction) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(BonusAction.class);
		criteria.add(Restrictions.eq(BonusAction.ID, bonusAction.getId()));
		criteria.setFetchMode("albums", FetchMode.JOIN);
		return (BonusAction)criteria.uniqueResult();
	}
}
