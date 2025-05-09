package ru.imagebook.server.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.shared.model.AdminSettings;
import ru.imagebook.shared.model.OrderFilter;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.server.hibernate.BaseRepository;
import ru.saasengine.client.model.auth.AbstractUserAccount;

@Repository
public class AuthRepositoryImpl extends BaseRepository implements
		AuthRepository {
	@Override
	@SuppressWarnings("unchecked")
	public List<AbstractUserAccount> findActiveAccounts(String userName,
			Vendor vendor) {
		Session session = getSession();

		Criteria criteria = session.createCriteria(UserAccount.class);
		criteria.setFetchMode(UserAccount.USER, FetchMode.JOIN);
		criteria.setFetchMode(UserAccount.USER + "." + User.EMAILS, FetchMode.JOIN);
		criteria.setFetchMode(UserAccount.USER + "." + User.PHONES, FetchMode.JOIN);
		criteria.setFetchMode(UserAccount.USER + "." + User.ADDRESSES,
				FetchMode.JOIN);
		criteria.setFetchMode(UserAccount.USER + "." + User.SETTINGS,
				FetchMode.JOIN);
		criteria.setFetchMode(UserAccount.USER + "." + User.ROLES, FetchMode.JOIN);
		criteria
				.setFetchMode(UserAccount.USER + "." + User.SETTINGS + "."
						+ AdminSettings.ORDER_FILTER + "." + OrderFilter.STATES,
						FetchMode.JOIN);
		criteria.add(Restrictions.eq(UserAccount.USER_NAME, userName));
		criteria.add(Restrictions.eq(UserAccount.ACTIVE, true));
		Criteria userCriteria = criteria.createCriteria(UserAccount.USER);
		userCriteria.add(Restrictions.eq(User.VENDOR, vendor));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public UserAccount findAccount(User user) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(UserAccount.class);
		criteria.setFetchMode(UserAccount.USER, FetchMode.JOIN);
		criteria.setFetchMode(UserAccount.USER + "." + User.EMAILS, FetchMode.JOIN);
		criteria.setFetchMode(UserAccount.USER + "." + User.PHONES, FetchMode.JOIN);
		criteria.setFetchMode(UserAccount.USER + "." + User.ADDRESSES,
				FetchMode.JOIN);
		criteria.add(Restrictions.eq(UserAccount.USER, user));
		return (UserAccount) criteria.uniqueResult();
	}

	@Override
	public void saveAccount(UserAccount account) {
		Session session = getSession();
		session.saveOrUpdate(account);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAccount> loadAccounts(Collection<User> users) {
		if (users.isEmpty())
			return new ArrayList<UserAccount>();

		Session session = getSession();
		Criteria criteria = session.createCriteria(UserAccount.class);
		criteria.add(Restrictions.in(UserAccount.USER, users));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteAccounts(List<Integer> userIds) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(UserAccount.class);
		criteria.add(Restrictions.in(UserAccount.USER + ".id", userIds));
		Collection<UserAccount> userAccounts = criteria.list();
		for (UserAccount account : userAccounts) {
			session.delete(account);
		}
	}

    @Override
    public void deleteAccount(Integer userId) {
        deleteAccounts(Collections.singletonList(userId));
    }

    @Override
	public UserAccount findAccountByEmailId(int emailId) {
		Session session = getSession();
		Query query = session
				.createQuery("select account from UserAccount account join account.user join account.user.emails email where email.id = :emailId");
		query.setParameter("emailId", emailId);
		UserAccount account = (UserAccount) query.uniqueResult();
		if (account != null)
			Hibernate.initialize(account.getUser());
		return account;
	}

	@Transactional
	@Override
	public String loadCommonPasswordHash() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(UserAccount.class);
		criteria.add(Restrictions.eq(UserAccount.USER_NAME, "common"));
		criteria.add(Restrictions.eq(UserAccount.ACTIVE, true));
		UserAccount account = (UserAccount) criteria.uniqueResult();
		return account.getPasswordHash();
	}
}
