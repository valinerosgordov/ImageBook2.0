package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.User;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class UpdateRepositoryImpl extends BaseRepository implements UpdateRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadUsers() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(User.class);
		return criteria.list();
	}
}
