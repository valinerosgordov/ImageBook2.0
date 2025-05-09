package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.client.admin.ctl.questions.QuestionCategoryActivity;
import ru.imagebook.client.admin.service.QuestionCategoryServiceAsync;
import ru.imagebook.client.admin.service.QuestionServiceAsync;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.Folder;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.TopSection;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SiteController extends Controller {
	private final SiteView view;
	private Section section;
	private TopSection topSection;
	private Phrase phrase;
	private Document document;
	private Banner banner;
	private Folder folder;
	private DirSection1 section1;
	private DirSection2 section2;
	private final I18nService i18nService;
	
	@Inject
	private QuestionCategoryActivity questionCategoryActivity;
	@Inject
	private QuestionServiceAsync questionService;
	@Inject
	private QuestionCategoryServiceAsync questionCategoryService;

	@Inject
	public SiteController(Dispatcher dispatcher, SiteView view,
			I18nService i18nService) {
		super(dispatcher);

		this.view = view;
		this.i18nService = i18nService;
	}

	@Override
	public void registerHandlers() {
		addHandler(SiteMessages.SHOW_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadSectionsMessage());

				view.showSection();
			}
		});

		addHandler(SiteMessages.LOAD_SECTIONS_RESULT,
				new MessageHandler<LoadSectionsResultMessage>() {
					@Override
					public void handle(LoadSectionsResultMessage message) {
						view.showSections(message.getRoot());
					}
				});

		addHandler(SiteMessages.SECTION_SELECTED,
				new MessageHandler<SectionSelectedMessage>() {
					@Override
					public void handle(SectionSelectedMessage message) {
						section = message.getSection();
						view.showSection(section);
					}
				});

		addHandler(SiteMessages.SAVE_SECTION,
				new MessageHandler<SaveSectionMessage>() {
					@Override
					public void handle(SaveSectionMessage message) {
						message.getSection().setId(section.getId());
					}
				});

		addHandler(SiteMessages.SAVE_SECTION_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadSectionsMessage());
			}
		});

		addHandler(SiteMessages.ADD_SECTION_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadSectionsMessage());
			}
		});

		addHandler(SiteMessages.DELETE_SECTIONS_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadSectionsMessage());
					}
				});

		addHandler(SiteMessages.ROOT_DELETE_ERROR, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertRootDelete();
			}
		});

		addHandler(SiteMessages.KEY_EXISTS_ERROR, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertkeyExists();
			}
		});

		addHandler(SiteMessages.CHILD_EXISTS_ERROR, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertChildExists();
			}
		});

		addHandler(SiteMessages.SHOW_TOP_SECTIONS_REQUEST,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						view.showTopSectionsPanel();

						send(new LoadTopSectionsMessage());
					}
				});

		addHandler(SiteMessages.LOAD_TOP_SECTIONS_RESULT,
				new MessageHandler<LoadTopSectionsResultMessage>() {
					@Override
					public void handle(LoadTopSectionsResultMessage message) {
						view.showTopSections(message.getTopSections());
					}
				});

		addHandler(SiteMessages.ADD_TOP_SECTION_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadTopSectionsMessage());
					}
				});

		addHandler(SiteMessages.TOP_SECTION_SELECTED,
				new MessageHandler<TopSectionSelectedMessage>() {
					@Override
					public void handle(TopSectionSelectedMessage message) {
						topSection = message.getTopSection();
						view.showTopSection(topSection);
					}
				});

		addHandler(SiteMessages.SAVE_TOP_SECTION,
				new MessageHandler<SaveTopSectionMessage>() {
					@Override
					public void handle(SaveTopSectionMessage message) {
						message.getTopSection().setId(topSection.getId());
					}
				});

		addHandler(SiteMessages.SAVE_TOP_SECTION_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadTopSectionsMessage());
					}
				});

		addHandler(SiteMessages.DELETE_TOP_SECTIONS_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadTopSectionsMessage());
					}
				});

		addHandler(SiteMessages.SHOW_PHRASES_SECTION,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						view.showPhrasesSection();

						send(new LoadPhrasesMessage());
					}
				});

		addHandler(SiteMessages.LOAD_PHRASES_RESULT,
				new MessageHandler<LoadPhrasesResultMessage>() {
					@Override
					public void handle(LoadPhrasesResultMessage message) {
						view.showPhrases(message.getPhrases());
					}
				});

		addHandler(SiteMessages.PHRASE_SELECTED,
				new MessageHandler<PhraseSelectedMessage>() {
					@Override
					public void handle(PhraseSelectedMessage message) {
						phrase = message.getPhrase();
						view.showPhrase(phrase);
					}
				});

		addHandler(SiteMessages.ADD_PHRASE_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadPhrasesMessage());
			}
		});

		addHandler(SiteMessages.SAVE_PHRASE,
				new MessageHandler<SavePhraseMessage>() {
					@Override
					public void handle(SavePhraseMessage message) {
						message.getPhrase().setId(phrase.getId());
					}
				});

		addHandler(SiteMessages.SAVE_PHRASE_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadPhrasesMessage());
			}
		});

		addHandler(SiteMessages.DELETE_PHRASES_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadPhrasesMessage());
					}
				});

		addHandler(SiteMessages.SHOW_DOCUMENTS_SECTION,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						view.showDocumentsSection();

						send(new LoadFoldersMessage());
					}
				});

		addHandler(SiteMessages.LOAD_FOLDERS_RESULT,
				new MessageHandler<LoadFoldersResultMessage>() {
					@Override
					public void handle(LoadFoldersResultMessage message) {
						view.showFolders(message.getFolders());
					}
				});

		addHandler(SiteMessages.FOLDER_SELECTED,
				new MessageHandler<FolderSelectedMessage>() {
					@Override
					public void handle(FolderSelectedMessage message) {
						folder = message.getFolder();
						view.showFolder(folder);
					}
				});

		addHandler(SiteMessages.DOCUMENT_SELECTED,
				new MessageHandler<DocumentSelectedMessage>() {
					@Override
					public void handle(DocumentSelectedMessage message) {
						document = message.getDocument();
						view.showDocument(document);
					}
				});

		addHandler(SiteMessages.ADD_FOLDER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadFoldersMessage());
			}
		});

		addHandler(SiteMessages.ADD_DOCUMENT,
				new MessageHandler<AddDocumentMessage>() {
					@Override
					public void handle(AddDocumentMessage message) {
						if (folder != null)
							message.setFolderId(folder.getId());
						else
							message.cancel();
					}
				});

		addHandler(SiteMessages.ADD_DOCUMENT_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadFoldersMessage());
			}
		});

		addHandler(SiteMessages.SAVE_FOLDER,
				new MessageHandler<SaveFolderMessage>() {
					@Override
					public void handle(SaveFolderMessage message) {
						message.getFolder().setId(folder.getId());
					}
				});

		addHandler(SiteMessages.SAVE_DOCUMENT,
				new MessageHandler<SaveDocumentMessage>() {
					@Override
					public void handle(SaveDocumentMessage message) {
						message.getDocument().setId(document.getId());
					}
				});

		addHandler(SiteMessages.SAVE_FOLDER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadFoldersMessage());
			}
		});

		addHandler(SiteMessages.SAVE_DOCUMENT_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadFoldersMessage());
					}
				});

		addHandler(SiteMessages.DELETE_FOLDERS_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadFoldersMessage());
					}
				});

		addHandler(SiteMessages.DELETE_DOCUMENTS_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadFoldersMessage());
					}
				});

		addHandler(SiteMessages.SHOW_BANNERS_SECTION,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						view.showBannersSection();

						send(new LoadBannersMessage());
					}
				});

		addHandler(SiteMessages.LOAD_BANNERS_RESULT,
				new MessageHandler<LoadBannersResultMessage>() {
					@Override
					public void handle(LoadBannersResultMessage message) {
						view.showBanners(message.getBanners());
					}
				});

		addHandler(SiteMessages.BANNER_SELECTED,
				new MessageHandler<BannerSelectedMessage>() {
					@Override
					public void handle(BannerSelectedMessage message) {
						banner = message.getBanner();
						view.showBanner(banner);
					}
				});

		addHandler(SiteMessages.ADD_BANNER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadBannersMessage());
			}
		});

		addHandler(SiteMessages.SAVE_BANNER,
				new MessageHandler<SaveBannerMessage>() {
					@Override
					public void handle(SaveBannerMessage message) {
						message.getBanner().setId(banner.getId());
					}
				});

		addHandler(SiteMessages.SAVE_BANNER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadBannersMessage());
			}
		});

		addHandler(SiteMessages.DELETE_BANNERS_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadBannersMessage());
					}
				});

		addHandler(SiteMessages.SHOW_DIR_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadDirSectionDataMessage());
			}
		});

		addHandler(SiteMessages.LOAD_DIR_SECTION_DATA_RESULT,
				new MessageHandler<LoadDirSectionDataResultMessage>() {
					@Override
					public void handle(LoadDirSectionDataResultMessage message) {
						view.showDirSection(message.getAlbums(), i18nService.getLocale());

						send(new LoadDirSectionsMessage());
					}
				});

		addHandler(SiteMessages.LOAD_DIR_SECTIONS_RESULT,
				new MessageHandler<LoadDirSectionsResultMessage>() {
					@Override
					public void handle(LoadDirSectionsResultMessage message) {
						view.showSections(message.getSections());
					}
				});

		addHandler(SiteMessages.ADD_DIR_SECTION1_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadDirSectionsMessage());
					}
				});

		addHandler(SiteMessages.DIR_SECTION1_SELECTED,
				new MessageHandler<DirSection1SelectedMessage>() {
					@Override
					public void handle(DirSection1SelectedMessage message) {
						section1 = message.getSection();
						view.showSection1(section1);
					}
				});

		addHandler(SiteMessages.SAVE_DIR_SECTION1,
				new MessageHandler<SaveDirSection1Message>() {
					@Override
					public void handle(SaveDirSection1Message message) {
						message.getSection().setId(section1.getId());
					}
				});

		addHandler(SiteMessages.SAVE_DIR_SECTION1_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadDirSectionsMessage());
					}
				});

		addHandler(SiteMessages.ADD_DIR_SECTION_2,
				new MessageHandler<AddDirSection2Message>() {
					@Override
					public void handle(AddDirSection2Message message) {
						if (section1 != null)
							message.setSection1Id(section1.getId());
						else
							message.cancel();
					}
				});

		addHandler(SiteMessages.ADD_DIR_SECTION_2_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadDirSectionsMessage());
					}
				});

		addHandler(SiteMessages.DIR_SECTION2_SELECTED,
				new MessageHandler<DirSection2SelectedMessage>() {
					@Override
					public void handle(DirSection2SelectedMessage message) {
						section2 = message.getSection();
						view.showSection2(section2);
					}
				});

		addHandler(SiteMessages.SAVE_DIR_SECTION2,
				new MessageHandler<SaveDirSection2Message>() {
					@Override
					public void handle(SaveDirSection2Message message) {
						message.getSection().setId(section2.getId());
					}
				});

		addHandler(SiteMessages.SAVE_DIR_SECTION2_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(new LoadDirSectionsMessage());
					}
				});
		
		addHandler(SiteMessages.SHOW_QUESTIONS,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						questionCategoryActivity = new QuestionCategoryActivity(view.getQuestionCategoryView(), questionService, questionCategoryService);
						view.showQuestionsPanel(questionCategoryActivity);
						//send(new LoadTopSectionsMessage());
					}
				});
	}
}
