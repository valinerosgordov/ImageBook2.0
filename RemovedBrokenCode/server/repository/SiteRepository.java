package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.Folder;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.TopSection;

public interface SiteRepository {
	Section getSection(int id);

	void save(Section section);

	void deleteSection(Section section);

	Section getSection(String key);

	List<Section> loadSections();

	List<Section> loadSections(Section parent);

	void flush();

	Section getRootSection();

	List<TopSection> loadTopSections();

	void save(TopSection section);

	TopSection getTopSection(int id);

	void deleteTopSections(List<Integer> ids);

	TopSection getTopSection(String key);

	void savePhrase(Phrase phrase);

	List<Phrase> loadPhrases();

	Phrase getPhrase(int id);

	void deletePhrases(List<Integer> ids);

	void saveDocument(Document document);

	Document getDocument(int id);

	void deleteDocuments(List<Integer> ids);

	Document getDocument(String key);

	List<Banner> loadBanners();

	void saveBanner(Banner banner);

	Banner getBanner(int id);

	void deleteBanners(List<Integer> ids);

	List<Banner> loadPageBanners();

	Phrase findPhrase(String key);

	List<Album> loadAlbums();

	List<Color> loadColors();

	Album getAlbum(int id);

	List<Folder> loadFolders();

	void saveFolder(Folder folder);

	Folder getFolder(int id);

	void deleteFolders(List<Integer> ids);

	List<DirSection1> loadDirSections();

	void saveDirSection1(DirSection1 section1);

	DirSection1 getDirSection1(int id);

	void saveSection2(DirSection2 section2);

	DirSection2 getDirSection2(int id);

	List<DirSection2> loadDirSections2(DirSection1 section1);
	
	List<DirSection2> loadDirSections2FullInformation(DirSection1 section1);

	DirSection1 findDirSection1(String key);

	DirSection2 findDirSection2(String key);

	List<DirSection1> loadDirSections1();

	void deleteDirSections1(List<Integer> ids);

	void deleteDirSections2(List<Integer> ids);

	Document findDocument(String key);
}
