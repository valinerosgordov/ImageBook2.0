package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumImpl;
import ru.imagebook.shared.model.Availability;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.DocumentImpl;
import ru.imagebook.shared.model.site.Folder;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.SectionImpl;
import ru.imagebook.shared.model.site.TopSection;
import ru.imagebook.shared.model.site.TopSectionImpl;
import ru.minogin.core.server.hibernate.BaseRepository;

@Repository
public class SiteRepositoryImpl extends BaseRepository implements
		SiteRepository {
	@Override
	public Section getSection(int id) {
		Session session = getSession();
		return (Section) session.get(SectionImpl.class, id);
	}

	@Override
	public void save(Section section) {
		Session session = getSession();
		session.save(section);
	}

	@Override
	public void deleteSection(Section section) {
		Session session = getSession();
		session.delete(section);
		session.flush();
	}

	@Override
	public Section getSection(String key) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Section.class);
		criteria.add(Restrictions.eq(Section.KEY, key));
		return (Section) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Section> loadSections() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Section.class);
		criteria.setFetchMode(Section.PARENT, FetchMode.JOIN);
		criteria.setFetchMode(Section.TAG, FetchMode.JOIN);
		criteria.addOrder(Order.asc(Section.NUMBER));
		return criteria.list();
	}

	@Override
	public void flush() {
		Session session = getSession();
		session.flush();
	}

	@Override
	public Section getRootSection() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Section.class);
		criteria.add(Restrictions.isNull(Section.PARENT));
		return (Section) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Section> loadSections(Section parent) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Section.class);
		criteria.setFetchMode(Section.TAG, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Section.PARENT, parent));
		criteria.add(Restrictions.eq(Section.HIDDEN, false));
		criteria.addOrder(Order.asc(Section.NUMBER));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TopSection> loadTopSections() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(TopSection.class);
		criteria.addOrder(Order.asc(TopSection.NUMBER));
		return criteria.list();
	}

	@Override
	public void save(TopSection section) {
		Session session = getSession();
		session.save(section);
	}

	@Override
	public TopSection getTopSection(int id) {
		Session session = getSession();
		return (TopSection) session.get(TopSectionImpl.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteTopSections(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(TopSection.class);
		criteria.add(Restrictions.in(TopSection.ID, ids));
		List<TopSection> topSections = criteria.list();
		for (TopSection topSection : topSections) {
			session.delete(topSection);
		}
	}

	@Override
	public TopSection getTopSection(String key) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(TopSection.class);
		criteria.add(Restrictions.eq(TopSection.KEY, key));
		return (TopSection) criteria.uniqueResult();
	}

	@Override
	public void savePhrase(Phrase phrase) {
		Session session = getSession();
		session.save(phrase);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Phrase> loadPhrases() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Phrase.class);
		criteria.addOrder(Order.asc(Phrase.NAME));
		return criteria.list();
	}

	@Override
	public Phrase getPhrase(int id) {
		Session session = getSession();
		return (Phrase) session.get(Phrase.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deletePhrases(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Phrase.class);
		criteria.add(Restrictions.in(Phrase.ID, ids));
		List<Phrase> phrases = criteria.list();
		for (Phrase phrase : phrases) {
			session.delete(phrase);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> loadFolders() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Folder.class);
		criteria.setFetchMode(Folder.DOCUMENTS, FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public void saveDocument(Document document) {
		Session session = getSession();
		session.save(document);
	}

	@Override
	public Document getDocument(int id) {
		Session session = getSession();
		return (Document) session.get(DocumentImpl.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteDocuments(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Document.class);
		criteria.add(Restrictions.in(Document.ID, ids));
		List<Document> documents = criteria.list();
		for (Document document : documents) {
			session.delete(document);
		}
	}

	@Override
	public Document getDocument(String key) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Document.class);
		criteria.add(Restrictions.eq(Document.KEY, key));
		return (Document) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Banner> loadBanners() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Banner.class);
		criteria.addOrder(Order.asc(Banner.NAME));
		return criteria.list();
	}

	@Override
	public void saveBanner(Banner banner) {
		Session session = getSession();
		session.save(banner);
	}

	@Override
	public Banner getBanner(int id) {
		Session session = getSession();
		return (Banner) session.get(Banner.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteBanners(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Banner.class);
		criteria.add(Restrictions.in(Banner.ID, ids));
		List<Banner> banners = criteria.list();
		for (Banner banner : banners) {
			session.delete(banner);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Banner> loadPageBanners() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Banner.class);
		criteria.addOrder(Order.asc(Banner.NAME));
		criteria.setMaxResults(2);
		return criteria.list();
	}

	@Override
	public Phrase findPhrase(String key) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Phrase.class);
		criteria.add(Restrictions.eq(Phrase.KEY, key));
		return (Phrase) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Album> loadAlbums() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Album.class);
		criteria.setFetchMode(Album.COVER_LAM_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Album.PAGE_LAM_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Album.COLOR_RANGE, FetchMode.JOIN);
		criteria.add(Restrictions.eq(Album.AVAILABILITY, Availability.PRESENT));
		criteria.addOrder(Order.asc(Album.TYPE));
		criteria.addOrder(Order.asc(Album.NUMBER));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Color> loadColors() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Color.class);
		criteria.addOrder(Order.asc(Color.NUMBER));
		return criteria.list();
	}

	@Override
	public Album getAlbum(int id) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(AlbumImpl.class);
		criteria.add(Restrictions.idEq(id));
		criteria.setFetchMode(Album.COVER_LAM_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Album.PAGE_LAM_RANGE, FetchMode.JOIN);
		criteria.setFetchMode(Album.COLOR_RANGE, FetchMode.JOIN);
		return (Album) criteria.uniqueResult();
	}

	@Override
	public void saveFolder(Folder folder) {
		Session session = getSession();
		session.save(folder);
	}

	@Override
	public Folder getFolder(int id) {
		Session session = getSession();
		return (Folder) session.get(Folder.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteFolders(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Folder.class);
		criteria.add(Restrictions.in(Folder.ID, ids));
		List<Folder> folders = criteria.list();
		for (Folder folder : folders) {
			session.delete(folder);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DirSection1> loadDirSections() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirSection1.class);
		criteria.setFetchMode(DirSection1.SECTIONS, FetchMode.JOIN);
		criteria.setFetchMode(DirSection1.SECTIONS + "." + DirSection2.ALBUMS,
				FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public void saveDirSection1(DirSection1 section1) {
		Session session = getSession();
		session.save(section1);
	}

	@Override
	public DirSection1 getDirSection1(int id) {
		Session session = getSession();
		return (DirSection1) session.get(DirSection1.class, id);
	}

	@Override
	public void saveSection2(DirSection2 section2) {
		Session session = getSession();
		session.save(section2);
	}

	@Override
	public DirSection2 getDirSection2(int id) {
		Session session = getSession();
		return (DirSection2) session.get(DirSection2.class, id);
	}

	@Override
	public DirSection1 findDirSection1(String key) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirSection1.class);
		criteria.add(Restrictions.eq(DirSection1.KEY, key));
		return (DirSection1) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DirSection1> loadDirSections1() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirSection1.class);
		criteria.setFetchMode(DirSection1.SECTIONS, FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DirSection2> loadDirSections2(DirSection1 section1) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirSection2.class);
		criteria.setFetchMode(DirSection2.ALBUMS, FetchMode.JOIN);
		criteria.add(Restrictions.eq(DirSection2.SECTION, section1));
		criteria.addOrder(Order.asc(DirSection2.INDEX));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DirSection2> loadDirSections2FullInformation(DirSection1 section1) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirSection2.class);
		criteria.setFetchMode(DirSection2.ALBUMS, FetchMode.JOIN);
		criteria.setFetchMode(DirSection2.ALBUMS + "." + Album.COVER_LAM_RANGE,
				FetchMode.JOIN);
		criteria.setFetchMode(DirSection2.ALBUMS + "." + Album.PAGE_LAM_RANGE,
				FetchMode.JOIN);
		criteria.setFetchMode(DirSection2.ALBUMS + "." + Album.COLOR_RANGE,
				FetchMode.JOIN);
		criteria.add(Restrictions.eq(DirSection2.SECTION, section1));
		criteria.addOrder(Order.asc(DirSection2.INDEX));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	
	@Override
	public DirSection2 findDirSection2(String key) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirSection2.class);
		criteria.setFetchMode(DirSection2.SECTION, FetchMode.JOIN);
		criteria.setFetchMode(DirSection2.ALBUMS, FetchMode.JOIN);
		criteria.setFetchMode(DirSection2.ALBUMS + "." + Album.COVER_LAM_RANGE,
				FetchMode.JOIN);
		criteria.setFetchMode(DirSection2.ALBUMS + "." + Album.PAGE_LAM_RANGE,
				FetchMode.JOIN);
		criteria.setFetchMode(DirSection2.ALBUMS + "." + Album.COLOR_RANGE,
				FetchMode.JOIN);
		criteria.add(Restrictions.eq(DirSection2.KEY, key));
		return (DirSection2) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteDirSections1(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirSection1.class);
		criteria.add(Restrictions.in(DirSection1.ID, ids));
		List<DirSection1> sections = criteria.list();
		for (DirSection1 section : sections) {
			session.delete(section);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteDirSections2(List<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirSection2.class);
		criteria.add(Restrictions.in(DirSection2.ID, ids));
		List<DirSection2> sections = criteria.list();
		for (DirSection2 section : sections) {
			session.delete(section);
		}
	}

	@Override
	public Document findDocument(String key) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Document.class);
		criteria.add(Restrictions.eq(Document.KEY, key));
		return (Document) criteria.uniqueResult();
	}
	
	
}
