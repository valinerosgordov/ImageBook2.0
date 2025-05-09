package ru.imagebook.server.ctl.site;

import java.util.List;

import ru.imagebook.client.admin.ctl.site.AddBannerMessage;
import ru.imagebook.client.admin.ctl.site.AddDirSection1Message;
import ru.imagebook.client.admin.ctl.site.AddDirSection2Message;
import ru.imagebook.client.admin.ctl.site.AddDocumentMessage;
import ru.imagebook.client.admin.ctl.site.AddFolderMessage;
import ru.imagebook.client.admin.ctl.site.AddPhraseMessage;
import ru.imagebook.client.admin.ctl.site.AddSectionMessage;
import ru.imagebook.client.admin.ctl.site.AddTopSectionMessage;
import ru.imagebook.client.admin.ctl.site.DeleteBannersMessage;
import ru.imagebook.client.admin.ctl.site.DeleteDirSectionsMessage;
import ru.imagebook.client.admin.ctl.site.DeleteDocumentsMessage;
import ru.imagebook.client.admin.ctl.site.DeleteFoldersMessage;
import ru.imagebook.client.admin.ctl.site.DeletePhrasesMessage;
import ru.imagebook.client.admin.ctl.site.DeleteSectionsMessage;
import ru.imagebook.client.admin.ctl.site.DeleteTopSectionsMessage;
import ru.imagebook.client.admin.ctl.site.LoadBannersMessage;
import ru.imagebook.client.admin.ctl.site.LoadBannersResultMessage;
import ru.imagebook.client.admin.ctl.site.LoadDirSectionDataMessage;
import ru.imagebook.client.admin.ctl.site.LoadDirSectionDataResultMessage;
import ru.imagebook.client.admin.ctl.site.LoadDirSectionsMessage;
import ru.imagebook.client.admin.ctl.site.LoadDirSectionsResultMessage;
import ru.imagebook.client.admin.ctl.site.LoadFoldersMessage;
import ru.imagebook.client.admin.ctl.site.LoadFoldersResultMessage;
import ru.imagebook.client.admin.ctl.site.LoadPhrasesMessage;
import ru.imagebook.client.admin.ctl.site.LoadPhrasesResultMessage;
import ru.imagebook.client.admin.ctl.site.LoadSectionsMessage;
import ru.imagebook.client.admin.ctl.site.LoadSectionsResultMessage;
import ru.imagebook.client.admin.ctl.site.LoadTopSectionsMessage;
import ru.imagebook.client.admin.ctl.site.LoadTopSectionsResultMessage;
import ru.imagebook.client.admin.ctl.site.SaveBannerMessage;
import ru.imagebook.client.admin.ctl.site.SaveDirSection1Message;
import ru.imagebook.client.admin.ctl.site.SaveDirSection2Message;
import ru.imagebook.client.admin.ctl.site.SaveDocumentMessage;
import ru.imagebook.client.admin.ctl.site.SaveFolderMessage;
import ru.imagebook.client.admin.ctl.site.SavePhraseMessage;
import ru.imagebook.client.admin.ctl.site.SaveSectionMessage;
import ru.imagebook.client.admin.ctl.site.SaveTopSectionMessage;
import ru.imagebook.client.admin.ctl.site.SiteMessages;
import ru.imagebook.server.service.site.ChildExistsError;
import ru.imagebook.server.service.site.KeyExistsError;
import ru.imagebook.server.service.site.PageNotFoundError;
import ru.imagebook.server.service.site.RootSectionDeleteError;
import ru.imagebook.server.service.site.SiteService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.Folder;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.TopSection;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.flow.remoting.MessageError;

public class SiteController extends Controller {
	private final SiteService service;

	public SiteController(Dispatcher dispatcher, SiteService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(SiteMessages.LOAD_SECTIONS, new MessageHandler<LoadSectionsMessage>() {
			@Override
			public void handle(LoadSectionsMessage message) {
				Section root = service.loadSections();
				send(new LoadSectionsResultMessage(root));
			}
		});

		addHandler(SiteMessages.SAVE_SECTION, new MessageHandler<SaveSectionMessage>() {
			@Override
			public void handle(SaveSectionMessage message) {
				try {
					service.saveSection(message.getSection());
				}
				catch (KeyExistsError e) {
					throw new MessageError(SiteMessages.KEY_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.SAVE_SECTION_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.ADD_SECTION, new MessageHandler<AddSectionMessage>() {
			@Override
			public void handle(AddSectionMessage message) {
				try {
					service.addSection(message.getParentId());
				}
				catch (KeyExistsError e) {
					throw new MessageError(SiteMessages.KEY_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.ADD_SECTION_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.DELETE_SECTIONS, new MessageHandler<DeleteSectionsMessage>() {
			@Override
			public void handle(DeleteSectionsMessage message) {
				try {
					service.deleteSections(message.getIds());
				}
				catch (RootSectionDeleteError e) {
					throw new MessageError(SiteMessages.ROOT_DELETE_ERROR);
				}
				catch (ChildExistsError e) {
					throw new MessageError(SiteMessages.CHILD_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.DELETE_SECTIONS_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ru.imagebook.server.ctl.site.SiteMessages.SHOW_PAGE,
				new MessageHandler<ShowPageMessage>() {
					@Override
					public void handle(ShowPageMessage message) {
						try {
							service.showPage(message.getUri(), message.getWriter(), message.getResponse());
						}
						catch (PageNotFoundError e) {
							Message reply = new BaseMessage(
									ru.imagebook.server.ctl.site.SiteMessages.PAGE_NOT_FOUND);
							reply.addAspects(RemotingAspect.CLIENT);
							send(reply);
						}
					}
				});

		addHandler(SiteMessages.LOAD_TOP_SECTIONS, new MessageHandler<LoadTopSectionsMessage>() {
			@Override
			public void handle(LoadTopSectionsMessage message) {
				List<TopSection> topSections = service.loadTopSections();

				send(new LoadTopSectionsResultMessage(topSections));
			}
		});

		addHandler(SiteMessages.ADD_TOP_SECTION, new MessageHandler<AddTopSectionMessage>() {
			@Override
			public void handle(AddTopSectionMessage message) {
				service.addTopSection();

				Message reply = new BaseMessage(SiteMessages.ADD_TOP_SECTION_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.SAVE_TOP_SECTION, new MessageHandler<SaveTopSectionMessage>() {
			@Override
			public void handle(SaveTopSectionMessage message) {
				try {
					service.saveTopSection(message.getTopSection());
				}
				catch (KeyExistsError e) {
					throw new MessageError(SiteMessages.KEY_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.SAVE_TOP_SECTION_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.DELETE_TOP_SECTIONS, new MessageHandler<DeleteTopSectionsMessage>() {
			@Override
			public void handle(DeleteTopSectionsMessage message) {
				service.deleteTopSections(message.getIds());

				Message reply = new BaseMessage(SiteMessages.DELETE_TOP_SECTIONS_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.LOAD_PHRASES, new MessageHandler<LoadPhrasesMessage>() {
			@Override
			public void handle(LoadPhrasesMessage message) {
				List<Phrase> phrases = service.loadPhrases();
				send(new LoadPhrasesResultMessage(phrases));
			}
		});

		addHandler(SiteMessages.ADD_PHRASE, new MessageHandler<AddPhraseMessage>() {
			@Override
			public void handle(AddPhraseMessage message) {
				service.addPhrase();

				Message reply = new BaseMessage(SiteMessages.ADD_PHRASE_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.SAVE_PHRASE, new MessageHandler<SavePhraseMessage>() {
			@Override
			public void handle(SavePhraseMessage message) {
				try {
					service.savePhrase(message.getPhrase());
				}
				catch (KeyExistsError e) {
					throw new MessageError(SiteMessages.KEY_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.SAVE_PHRASE_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.DELETE_PHRASES, new MessageHandler<DeletePhrasesMessage>() {
			@Override
			public void handle(DeletePhrasesMessage message) {
				service.deletePhrases(message.getIds());

				Message reply = new BaseMessage(SiteMessages.DELETE_PHRASES_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.LOAD_FOLDERS, new MessageHandler<LoadFoldersMessage>() {
			@Override
			public void handle(LoadFoldersMessage message) {
				List<Folder> folders = service.loadFolders();
				send(new LoadFoldersResultMessage(folders));
			}
		});

		addHandler(SiteMessages.ADD_FOLDER, new MessageHandler<AddFolderMessage>() {
			@Override
			public void handle(AddFolderMessage message) {
				service.addFolder();

				Message reply = new BaseMessage(SiteMessages.ADD_FOLDER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.ADD_DOCUMENT, new MessageHandler<AddDocumentMessage>() {
			@Override
			public void handle(AddDocumentMessage message) {
				service.addDocument(message.getFolderId());

				Message reply = new BaseMessage(SiteMessages.ADD_DOCUMENT_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.SAVE_FOLDER, new MessageHandler<SaveFolderMessage>() {
			@Override
			public void handle(SaveFolderMessage message) {
				service.saveFolder(message.getFolder());

				Message reply = new BaseMessage(SiteMessages.SAVE_FOLDER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.SAVE_DOCUMENT, new MessageHandler<SaveDocumentMessage>() {
			@Override
			public void handle(SaveDocumentMessage message) {
				try {
					service.saveDocument(message.getDocument());
				}
				catch (KeyExistsError e) {
					throw new MessageError(SiteMessages.KEY_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.SAVE_DOCUMENT_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.DELETE_FOLDERS, new MessageHandler<DeleteFoldersMessage>() {
			@Override
			public void handle(DeleteFoldersMessage message) {
				service.deleteFolders(message.getIds());

				Message reply = new BaseMessage(SiteMessages.DELETE_FOLDERS_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.DELETE_DOCUMENTS, new MessageHandler<DeleteDocumentsMessage>() {
			@Override
			public void handle(DeleteDocumentsMessage message) {
				service.deleteDocuments(message.getIds());

				Message reply = new BaseMessage(SiteMessages.DELETE_DOCUMENTS_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.LOAD_BANNERS, new MessageHandler<LoadBannersMessage>() {
			@Override
			public void handle(LoadBannersMessage message) {
				List<Banner> banners = service.loadBanners();
				send(new LoadBannersResultMessage(banners));
			}
		});

		addHandler(SiteMessages.ADD_BANNER, new MessageHandler<AddBannerMessage>() {
			@Override
			public void handle(AddBannerMessage message) {
				service.addBanner();

				Message reply = new BaseMessage(SiteMessages.ADD_BANNER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.SAVE_BANNER, new MessageHandler<SaveBannerMessage>() {
			@Override
			public void handle(SaveBannerMessage message) {
				try {
					service.saveBanner(message.getBanner());
				}
				catch (KeyExistsError e) {
					throw new MessageError(SiteMessages.KEY_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.SAVE_BANNER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.DELETE_BANNERS, new MessageHandler<DeleteBannersMessage>() {
			@Override
			public void handle(DeleteBannersMessage message) {
				service.deleteBanners(message.getIds());

				Message reply = new BaseMessage(SiteMessages.DELETE_BANNERS_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.LOAD_DIR_SECTIONS, new MessageHandler<LoadDirSectionsMessage>() {
			@Override
			public void handle(LoadDirSectionsMessage message) {
				List<DirSection1> sections = service.loadDirSections();
				send(new LoadDirSectionsResultMessage(sections));
			}
		});

		addHandler(SiteMessages.ADD_DIR_SECTION_1, new MessageHandler<AddDirSection1Message>() {
			@Override
			public void handle(AddDirSection1Message message) {
				service.addDirSection1();

				Message reply = new BaseMessage(SiteMessages.ADD_DIR_SECTION1_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.SAVE_DIR_SECTION1, new MessageHandler<SaveDirSection1Message>() {
			@Override
			public void handle(SaveDirSection1Message message) {
				try {
					service.saveDirSection1(message.getSection());
				}
				catch (KeyExistsError e) {
					throw new MessageError(SiteMessages.KEY_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.SAVE_DIR_SECTION1_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.ADD_DIR_SECTION_2, new MessageHandler<AddDirSection2Message>() {
			@Override
			public void handle(AddDirSection2Message message) {
				service.addDirSection2(message.getSection1Id());

				Message reply = new BaseMessage(SiteMessages.ADD_DIR_SECTION_2_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.SAVE_DIR_SECTION2, new MessageHandler<SaveDirSection2Message>() {
			@Override
			public void handle(SaveDirSection2Message message) {
				try {
					service.saveDirSection2(message.getSection());
				}
				catch (KeyExistsError e) {
					throw new MessageError(SiteMessages.KEY_EXISTS_ERROR);
				}

				Message reply = new BaseMessage(SiteMessages.SAVE_DIR_SECTION2_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(SiteMessages.LOAD_DIR_SECTION_DATA, new MessageHandler<LoadDirSectionDataMessage>() {
			@Override
			public void handle(LoadDirSectionDataMessage message) {
				List<Album> albums = service.loadAlbums();

				send(new LoadDirSectionDataResultMessage(albums));
			}
		});

		addHandler(SiteMessages.DELETE_DIR_SECTIONS, new MessageHandler<DeleteDirSectionsMessage>() {
			@Override
			public void handle(DeleteDirSectionsMessage message) {
				service.deleteDirSections(message.getLevel(), message.getIds());

				Message reply = new BaseMessage(SiteMessages.DELETE_DIR_SECTIONS_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});
	}
}
