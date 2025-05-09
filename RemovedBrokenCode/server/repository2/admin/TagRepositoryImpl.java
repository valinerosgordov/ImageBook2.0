package ru.imagebook.server.repository2.admin;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ru.imagebook.shared.model.site.Tag;
import ru.minogin.core.server.hibernate.BaseRepository;

public class TagRepositoryImpl extends BaseRepository implements TagRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> loadTags() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Tag.class);
		criteria.addOrder(Order.asc(Tag.NAME));
		return criteria.list();
	}

	@Override
	public void saveTag(Tag tag) {
		Session session = getSession();
		session.save(tag);
	}

	@Override
	public Tag getTag(int id) {
		Session session = getSession();
		return (Tag) session.get(Tag.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteTags(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Tag.class);
		criteria.add(Restrictions.in(Tag.ID, ids));
		List<Tag> tags = criteria.list();
		for (Tag tag : tags) {
			session.delete(tag);
		}
	}
}
