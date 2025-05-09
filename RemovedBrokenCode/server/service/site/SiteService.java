package ru.imagebook.server.service.site;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.Folder;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.TopSection;

public interface SiteService {
	void addSection(int parentId);

	void deleteSections(List<Integer> ids);

	void saveSection(Section section);

	Section loadSections();

	List<TopSection> loadTopSections();

	void addTopSection();

	void saveTopSection(TopSection topSection);

	void deleteTopSections(List<Integer> ids);

	void addPhrase();

	List<Phrase> loadPhrases();

	void savePhrase(Phrase phrase);

	void deletePhrases(List<Integer> ids);

	List<Folder> loadFolders();

	void addDocument(int folderId);

	void saveDocument(Document document);

	void deleteDocuments(List<Integer> ids);

	List<Banner> loadBanners();

	void addBanner();

	void saveBanner(Banner banner);

	void deleteBanners(List<Integer> ids);

	void addFolder();

	void saveFolder(Folder folder);

	void deleteFolders(List<Integer> ids);

	List<DirSection1> loadDirSections();

	void addDirSection1();

	void saveDirSection1(DirSection1 section);

	void addDirSection2(int section1Id);

	void saveDirSection2(DirSection2 section);

	List<Album> loadAlbums();

	void deleteDirSections(int level, List<Integer> ids);

	void showPage(String uri, Writer writer, HttpServletResponse response);

	String getNonLocalizedUrl(String path);
}
