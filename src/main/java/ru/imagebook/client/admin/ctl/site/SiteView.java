package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.imagebook.client.admin.view.questions.QuestionCategoryPresenter;
import ru.imagebook.client.admin.view.questions.QuestionCategoryView;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.Folder;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.TopSection;

public interface SiteView {
	void showSection();

	void showSections(Section root);

	void showSection(Section section);

	void alertRootDelete();

	void alertkeyExists();

	void alertChildExists();

	void showTopSectionsPanel();
	
	void showQuestionsPanel(QuestionCategoryPresenter presenter);
	
	QuestionCategoryView getQuestionCategoryView();

	void showTopSections(List<TopSection> topSections);

	void showTopSection(TopSection topSection);

	void showPhrasesSection();

	void showPhrases(List<Phrase> phrases);

	void showPhrase(Phrase phrase);

	void showDocumentsSection();

	void showFolders(List<Folder> folders);

	void showDocument(Document document);

	void showBannersSection();

	void showBanners(List<Banner> banners);

	void showBanner(Banner banner);

	void showFolder(Folder folder);

	void showSections(List<DirSection1> sections);

	void showSection1(DirSection1 section1);

	void showSection2(DirSection2 section2);

	void showDirSection(List<Album> albums, String locale);
}
