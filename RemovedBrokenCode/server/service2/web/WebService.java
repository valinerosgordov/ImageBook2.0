package ru.imagebook.server.service2.web;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.TopSection;

public interface WebService {
	Section getSection(String key);

	List<TopSection> loadTopSections();

	List<Banner> loadPageBanners();

	Section getRootSection();

	List<Section> loadSections1();

	TopSection getTopSection(String key);

	Document getDocument(String key);

	List<Phrase> loadPhrases();

	List<Section> loadSections(Section parent);

	Section getSection(int id);

	List<DirSection1> loadDirSections1();

	List<Album> loadAlbums();

	List<Color> loadColors();

	DirSection1 findDirSection1(String key);

	List<DirSection2> loadDirSections2(DirSection1 section1);
	
	List<DirSection2> loadDirSections2FullInformation(DirSection1 section1);

	DirSection2 findDirSection2(String key);

	Album getAlbum(int id);

	Document findDocument(String key);

	BonusAction getBonusAction(String code);
}
