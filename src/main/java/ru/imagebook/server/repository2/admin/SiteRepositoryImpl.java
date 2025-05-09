package ru.imagebook.server.repository2.admin;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.SectionImpl;
import ru.imagebook.shared.model.site.Tag;
import ru.minogin.core.server.hibernate.BaseRepository;

public class SiteRepositoryImpl extends BaseRepository implements SiteRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<Section> loadSections() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Section.class);
		criteria.setFetchMode(Section.PARENT, FetchMode.JOIN);
		criteria.setFetchMode(Section.CHILDREN, FetchMode.JOIN);
		criteria.setFetchMode(Section.TAG, FetchMode.JOIN);
		criteria.addOrder(Order.asc(Section.NUMBER));
		return criteria.list();
	}

	@Override
	public Section getSection(int id) {
		Session session = getSession();
		return (Section) session.get(SectionImpl.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> loadTags() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Tag.class);
		criteria.addOrder(Order.asc(Tag.NAME));
		return criteria.list();
	}

	@Override
	public Tag getTag(int tagId) {
		Session session = getSession();
		return (Tag) session.get(Tag.class, tagId);
	}
}
