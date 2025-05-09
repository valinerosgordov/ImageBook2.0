package ru.imagebook.client.admin.view.site;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.ctl.site.AddBannerMessage;
import ru.imagebook.client.admin.ctl.site.AddDirSection1Message;
import ru.imagebook.client.admin.ctl.site.AddDirSection2Message;
import ru.imagebook.client.admin.ctl.site.AddDocumentMessage;
import ru.imagebook.client.admin.ctl.site.AddFolderMessage;
import ru.imagebook.client.admin.ctl.site.AddPhraseMessage;
import ru.imagebook.client.admin.ctl.site.AddSectionMessage;
import ru.imagebook.client.admin.ctl.site.AddTopSectionMessage;
import ru.imagebook.client.admin.ctl.site.BannerSelectedMessage;
import ru.imagebook.client.admin.ctl.site.DeleteBannersMessage;
import ru.imagebook.client.admin.ctl.site.DeleteDirSectionsMessage;
import ru.imagebook.client.admin.ctl.site.DeleteDocumentsMessage;
import ru.imagebook.client.admin.ctl.site.DeleteFoldersMessage;
import ru.imagebook.client.admin.ctl.site.DeletePhrasesMessage;
import ru.imagebook.client.admin.ctl.site.DeleteSectionsMessage;
import ru.imagebook.client.admin.ctl.site.DeleteTopSectionsMessage;
import ru.imagebook.client.admin.ctl.site.DirSection1SelectedMessage;
import ru.imagebook.client.admin.ctl.site.DirSection2SelectedMessage;
import ru.imagebook.client.admin.ctl.site.DocumentSelectedMessage;
import ru.imagebook.client.admin.ctl.site.FolderSelectedMessage;
import ru.imagebook.client.admin.ctl.site.PhraseSelectedMessage;
import ru.imagebook.client.admin.ctl.site.SaveBannerMessage;
import ru.imagebook.client.admin.ctl.site.SaveDirSection1Message;
import ru.imagebook.client.admin.ctl.site.SaveDirSection2Message;
import ru.imagebook.client.admin.ctl.site.SaveDocumentMessage;
import ru.imagebook.client.admin.ctl.site.SaveFolderMessage;
import ru.imagebook.client.admin.ctl.site.SavePhraseMessage;
import ru.imagebook.client.admin.ctl.site.SaveSectionMessage;
import ru.imagebook.client.admin.ctl.site.SaveTopSectionMessage;
import ru.imagebook.client.admin.ctl.site.SectionSelectedMessage;
import ru.imagebook.client.admin.ctl.site.SiteMessages;
import ru.imagebook.client.admin.ctl.site.SitePlace;
import ru.imagebook.client.admin.ctl.site.SiteView;
import ru.imagebook.client.admin.ctl.site.TopSectionSelectedMessage;
import ru.imagebook.client.admin.ctl.site.tag.TagPlace;
import ru.imagebook.client.admin.service.TagServiceAsync;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.admin.view.questions.QuestionCategoryPresenter;
import ru.imagebook.client.admin.view.questions.QuestionCategoryView;
import ru.imagebook.client.admin.view.questions.QuestionCategoryViewImpl;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.DocumentImpl;
import ru.imagebook.shared.model.site.Folder;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.SectionImpl;
import ru.imagebook.shared.model.site.Tag;
import ru.imagebook.shared.model.site.TopSection;
import ru.imagebook.shared.model.site.TopSectionImpl;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.format.Formatter;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.GxtConstants;
import ru.minogin.core.client.gxt.form.MultiSelectField;
import ru.minogin.core.client.gxt.form.XHtmlField;
import ru.minogin.core.client.i18n.MultiString;
import ru.minogin.core.client.mvp.InjectedPlaceController;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.CardPanel;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.WidgetComponent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SiteViewImpl extends View implements SiteView {
	private final Widgets widgets;
	private final SiteConstants constants;
	private final CoreFactory coreFactory;
	private final GxtConstants xgxtConstants;
	private final SiteBundle bundle;
	private final CommonConstants appConstants;

	private TreeStore<SectionModel> sectionStore;
	private TreeGrid<SectionModel> sectionGrid;
	private FormPanel formPanel;
	private NumberField numberField;
	private LayoutContainer mainPanel;
	private DocumentForm sectionForm;
	private ListStore<BeanModel<TopSection>> topSectionsStore;
	private NumberField topNumberField;
	private DocumentForm topSectionForm;
	private Grid<BeanModel<TopSection>> topSectionGrid;
	private LabelField topUrlField;
	private LabelField urlField;
	private ListStore<BeanModel<Phrase>> phraseStore;
	private Grid<BeanModel<Phrase>> phraseGrid;
	private TextField<String> phraseNameField;
	private TextField<String> phraseKeyField;
	private XHtmlField phraseValueField;
	private CheckBox hiddenField;
	private TreeStore<DocumentModel> documentsStore;
	private TreeGrid<DocumentModel> documentsGrid;
	private DocumentForm documentForm;
	private LabelField documentUrlField;
	private ListStore<BeanModel<Banner>> bannerStore;
	private Grid<BeanModel<Banner>> bannerGrid;
	private TextField<String> bannerNameField;
	private TextField<String> bannerTitleField;
	private TextField<String> bannerUrlField;
	private XHtmlField bannerContentField;
	private CheckBox bannerTargetBlankField;
	private TextField<String> folderNameField;
	private FormPanel folderFormPanel;
	private FormPanel documentFormPanel;
	private CardPanel documentCardPanel;
	private TreeStore<DirModel> dirStore;
	private TreeGrid<DirModel> dirGrid;
	private CardPanel dirCardPanel;
	private FormPanel section1FormPanel;
	private TextField<String> section1NameField;
	private NumberField section1IndexField;
	private TextField<String> section1KeyField;
	private FormPanel section2FormPanel;
	private NumberField section2IndexField;
	private Field<String> section2KeyField;
	private TextField<String> section2NameField;
	private XHtmlField section2PreviewField;
	private MultiSelectField<Album> albumsField;
	private final InjectedPlaceController placeController;
	private final TagServiceAsync tagService;
	private List<Tag> tags;
	
	private QuestionCategoryView questionCategoryView = new QuestionCategoryViewImpl();	

	@Inject
	public SiteViewImpl(Dispatcher dispatcher, Widgets widgets,
			SiteConstants constants, SiteBundle bundle, CommonConstants appConstants,
			CoreFactory coreFactory, GxtConstants xgxtConstants,
			InjectedPlaceController placeController, TagServiceAsync tagService) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.bundle = bundle;
		this.appConstants = appConstants;
		this.coreFactory = coreFactory;
		this.xgxtConstants = xgxtConstants;
		this.placeController = placeController;
		this.tagService = tagService;
	}

	@Override
	public void showSection() {
		placeController.goTo(new SitePlace());

		final LayoutContainer container = widgets.get(DesktopWidgets.DESKTOP);
		container.removeAll();

		final ContentPanel panel = new ContentPanel(new FitLayout());

		panel.setHeaderVisible(false);
		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(constants.sectionsButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(SiteMessages.SHOW_SECTION);
					}
				}));
		toolBar.add(new Button(constants.documentsButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(SiteMessages.SHOW_DOCUMENTS_SECTION);
					}
				}));
		toolBar.add(new Button(constants.topSectionsButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(SiteMessages.SHOW_TOP_SECTIONS_REQUEST);
					}
				}));
		toolBar.add(new Button(constants.questionCategoryButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(SiteMessages.SHOW_QUESTIONS);
					}
				}));
		toolBar.add(new Button(constants.phrasesButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(SiteMessages.SHOW_PHRASES_SECTION);
					}
				}));
		toolBar.add(new Button(constants.bannersButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(SiteMessages.SHOW_BANNERS_SECTION);
					}
				}));
		toolBar.add(new Button(constants.dirButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(SiteMessages.SHOW_DIR_SECTION);
					}
				}));
		toolBar.add(new Button(constants.tagsButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						placeController.goTo(new TagPlace());
					}
				}));
		toolBar.add(new WidgetComponent(new Anchor(constants.allSectionsButton(),
				"/admin/sections", "allSections")));
		panel.setTopComponent(toolBar);

		mainPanel = new LayoutContainer(new BorderLayout());

		ContentPanel sectionPanel = new ContentPanel(new FitLayout());
		sectionPanel.setHeading(constants.sectionHeading());

		toolBar = new ToolBar();
		toolBar.add(new Button(constants.addSection(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						SectionModel parentModel = sectionGrid.getSelectionModel()
								.getSelectedItem();
						if (parentModel != null) {
							Section parent = parentModel.getSection();
							send(new AddSectionMessage(parent));
						}
					}
				}));
		toolBar.add(new Button(appConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<SectionModel> items = sectionGrid.getSelectionModel()
								.getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(), constants
									.confirmDelete(), new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									List<Integer> ids = new ArrayList<Integer>();
									for (SectionModel model : items) {
										ids.add(model.getSection().getId());
									}
									send(new DeleteSectionsMessage(ids));
								}
							});
						}
					}
				}));
		sectionPanel.setTopComponent(toolBar);

		sectionStore = new TreeStore<SectionModel>();
		sectionStore.setKeyProvider(new ModelKeyProvider<SectionModel>() {
			public String getKey(SectionModel model) {
				return model.getSection().getId() + "";
			}
		});

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		ColumnConfig sectionNameColumn = new ColumnConfig(Section.NAME,
				constants.sectionNameColumn(), 200);
		sectionNameColumn.setRenderer(new TreeGridCellRenderer<SectionModel>());
		columns.add(sectionNameColumn);
		sectionGrid = new TreeGrid<SectionModel>(sectionStore, new ColumnModel(
				columns));
		sectionGrid.setId("sectionGrid");
		// sectionGrid.setStateful(true);
		sectionGrid.getStyle().setLeafIcon(
				AbstractImagePrototype.create(bundle.leaf()));
		sectionGrid.setAutoExpandColumn(Section.NAME);
		sectionGrid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<SectionModel>() {
					@Override
					public void selectionChanged(SelectionChangedEvent<SectionModel> se) {
						SectionModel selectedItem = se.getSelectedItem();
						if (selectedItem != null) {
							Section selectedSection = selectedItem.getSection();
							send(new SectionSelectedMessage(selectedSection));
						}
					}
				});
		sectionGrid.getView().setSortingEnabled(false);
		sectionPanel.add(sectionGrid);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER, 0.3f);
		data.setSplit(true);
		data.setMargins(new Margins(5));
		mainPanel.add(sectionPanel, data);

		formPanel = new FormPanel();
		formPanel.setScrollMode(Scroll.AUTO);
		formPanel.setLabelWidth(150);
		formPanel.setFieldWidth(300);

		urlField = new LabelField();
		formPanel.add(urlField);

		numberField = new NumberField();
		numberField.setPropertyEditorType(Integer.class);
		numberField.setFieldLabel(constants.numberField());
		formPanel.add(numberField);

		hiddenField = new CheckBox();
		hiddenField.setFieldLabel(constants.hiddenField());
		hiddenField.setBoxLabel("");
		formPanel.add(hiddenField);

		tagService.loadTags(new AsyncCallback<List<Tag>>() {
			@Override
			public void onSuccess(List<Tag> tags) {
				SiteViewImpl.this.tags = tags;

				sectionForm = new DocumentForm(constants, formPanel, xgxtConstants,
						tags);
				sectionForm.render();

				formPanel.setButtonAlign(HorizontalAlignment.LEFT);
				Button saveButton = new Button(appConstants.save(),
						new SelectionListener<ButtonEvent>() {
							@Override
							public void componentSelected(ButtonEvent ce) {
								Section section = new SectionImpl();
								section.setNumber((Integer) numberField.getValue());
								sectionForm.fetch(section);
								section.setHidden(hiddenField.getValue());
								send(new SaveSectionMessage(section));
							}
						});
				new FormButtonBinding(formPanel).addButton(saveButton);
				formPanel.addButton(saveButton);

				BorderLayoutData data = new BorderLayoutData(LayoutRegion.EAST, 0.7f);
				data.setSplit(true);
				data.setMargins(new Margins(5, 5, 5, 0));
				mainPanel.add(formPanel, data);

				panel.add(mainPanel);

				container.add(panel);

				container.layout();
			}

			@Override
			public void onFailure(Throwable caught) {
				Exceptions.rethrow(caught);
			}
		});
	}

	@Override
	public void showSections(Section root) {
		sectionStore.removeAll();

		SectionModel rootModel = new SectionModel(root);
		sectionStore.add(rootModel, false);
		addSections(rootModel);

		sectionGrid.expandAll();
	}

	private void addSections(SectionModel sectionModel) {
		Section section = sectionModel.getSection();
		for (Section child : section.getChildren()) {
			SectionModel childModel = new SectionModel(child);
			sectionStore.add(sectionModel, childModel, false);
			addSections(childModel);
		}
	}

	@Override
	public void showSection(Section section) {
		numberField.setValue(section.getNumber());
		sectionForm.setValues(section);
		urlField.setText("<a href=\"http://imagebook.ru/" + section.getKey()
				+ "\" target=\"_blank\">Посмотреть на сайте</a>");
		hiddenField.setValue(section.isHidden());
	}

	@Override
	public void alertRootDelete() {
		MessageBox.alert(appConstants.error(), constants.rootDelete(), null);
	}

	@Override
	public void alertkeyExists() {
		MessageBox.alert(appConstants.error(), constants.keyExists(), null);
	}

	@Override
	public void alertChildExists() {
		MessageBox.alert(appConstants.error(), constants.childExists(), null);
	}

	@Override
	public void showTopSectionsPanel() {
		mainPanel.removeAll();

		ContentPanel sectionsPanel = new ContentPanel(new FitLayout());
		sectionsPanel.setHeading(constants.topSectionsHeading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(appConstants.add(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(new AddTopSectionMessage());
					}
				}));
		toolBar.add(new Button(appConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<BeanModel<TopSection>> items = topSectionGrid
								.getSelectionModel().getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(), constants
									.confirmDelete(), new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<TopSection> model : items) {
										ids.add(model.getBean().getId());
									}
									send(new DeleteTopSectionsMessage(ids));
								}
							});
						}
					}
				}));
		sectionsPanel.setTopComponent(toolBar);

		topSectionsStore = new ListStore<BeanModel<TopSection>>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		columns.add(new ColumnConfig(TopSection.NUMBER, constants.numberColumn(),
				50));

		ColumnConfig nameConfig = new ColumnConfig(TopSection.NAME,
				constants.nameColumn(), 200);
		nameConfig.setRenderer(new GridCellRenderer<BeanModel<TopSection>>() {
			@Override
			public Object render(BeanModel<TopSection> model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BeanModel<TopSection>> store,
					Grid<BeanModel<TopSection>> grid) {
				if (property.equals(TopSection.NAME)
						&& model.getBean().getName() == null)
					return "<i>" + constants.topSectionNoName() + "</i>";
				return model.get(property);
			}
		});
		columns.add(nameConfig);

		topSectionGrid = new Grid<BeanModel<TopSection>>(topSectionsStore,
				new ColumnModel(columns));
		topSectionGrid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<BeanModel<TopSection>>() {
					@Override
					public void selectionChanged(
							SelectionChangedEvent<BeanModel<TopSection>> se) {
						BeanModel<TopSection> selectedItem = se.getSelectedItem();
						if (selectedItem != null) {
							TopSection section = selectedItem.getBean();
							send(new TopSectionSelectedMessage(section));
						}
					}
				});
		topSectionGrid.getView().setSortingEnabled(false);
		sectionsPanel.add(topSectionGrid);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER, 0.3f);
		data.setSplit(true);
		data.setMargins(new Margins(5));
		mainPanel.add(sectionsPanel, data);

		FormPanel formPanel = new FormPanel();
		formPanel.setScrollMode(Scroll.AUTO);
		formPanel.setLabelWidth(150);
		formPanel.setFieldWidth(300);

		topUrlField = new LabelField();
		formPanel.add(topUrlField);

		topNumberField = new NumberField();
		topNumberField.setPropertyEditorType(Integer.class);
		topNumberField.setFieldLabel(constants.numberField());
		formPanel.add(topNumberField);

		topSectionForm = new DocumentForm(constants, formPanel, xgxtConstants, tags);
		topSectionForm.render();

		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						TopSection topSection = new TopSectionImpl();
						topSection.setNumber((Integer) topNumberField.getValue());
						topSectionForm.fetch(topSection);
						send(new SaveTopSectionMessage(topSection));
					}
				});
		new FormButtonBinding(formPanel).addButton(saveButton);
		formPanel.addButton(saveButton);

		data = new BorderLayoutData(LayoutRegion.EAST, 0.7f);
		data.setSplit(true);
		data.setMargins(new Margins(5, 5, 5, 0));
		mainPanel.add(formPanel, data);

		mainPanel.layout();
	}
	
	@Override
	public void showQuestionsPanel(QuestionCategoryPresenter presenter) {
		mainPanel.removeAll();

		questionCategoryView.setPresenter(presenter);
		
		LayoutContainer questionsContainer = (LayoutContainer) questionCategoryView.asWidget();
		

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER, 0.3f);
		data.setSplit(true);
		data.setMargins(new Margins(5));
		mainPanel.add(questionsContainer, data);


		mainPanel.layout();
	}
	
	@Override
	public QuestionCategoryView getQuestionCategoryView(){
		return questionCategoryView;
	}

	@Override
	public void showTopSections(List<TopSection> topSections) {
		topSectionsStore.removeAll();

		for (TopSection topSection : topSections) {
			BeanModel<TopSection> model = new BeanModel<TopSection>(topSection);
			topSectionsStore.add(model);
		}
	}

	@Override
	public void showTopSection(TopSection topSection) {
		topNumberField.setValue(topSection.getNumber());
		topSectionForm.setValues(topSection);
		topUrlField.setText("<a href=\"http://web.imagebook.ru/"
				+ topSection.getKey() + "\" target=\"_blank\">Посмотреть на сайте</a>");
	}

	@Override
	public void showPhrasesSection() {
		mainPanel.removeAll();

		ContentPanel phrasesPanel = new ContentPanel(new FitLayout());
		phrasesPanel.setHeading(constants.phrasesHeading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(appConstants.add(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(new AddPhraseMessage());
					}
				}));
		toolBar.add(new Button(appConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<BeanModel<Phrase>> items = phraseGrid
								.getSelectionModel().getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(), constants
									.confirmDelete(), new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<Phrase> model : items) {
										ids.add(model.getBean().getId());
									}
									send(new DeletePhrasesMessage(ids));
								}
							});
						}
					}
				}));
		phrasesPanel.setTopComponent(toolBar);

		phraseStore = new ListStore<BeanModel<Phrase>>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig nameConfig = new ColumnConfig(Phrase.NAME,
				constants.nameColumn(), 200);
		nameConfig.setRenderer(new GridCellRenderer<BeanModel<Phrase>>() {
			@Override
			public Object render(BeanModel<Phrase> model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BeanModel<Phrase>> store, Grid<BeanModel<Phrase>> grid) {
				if (property.equals(Phrase.NAME) && model.getBean().getName() == null)
					return "<i>" + constants.phraseNoName() + "</i>";
				return model.get(property);
			}
		});
		columns.add(nameConfig);

		columns.add(new ColumnConfig(Phrase.KEY, constants.keyColumn(), 200));

		phraseGrid = new Grid<BeanModel<Phrase>>(phraseStore, new ColumnModel(
				columns));
		phraseGrid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<BeanModel<Phrase>>() {
					@Override
					public void selectionChanged(
							SelectionChangedEvent<BeanModel<Phrase>> se) {
						BeanModel<Phrase> selectedItem = se.getSelectedItem();
						if (selectedItem != null) {
							Phrase phrase = selectedItem.getBean();
							send(new PhraseSelectedMessage(phrase));
						}
					}
				});
		phraseGrid.getView().setSortingEnabled(false);
		phrasesPanel.add(phraseGrid);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER, 0.3f);
		data.setSplit(true);
		data.setMargins(new Margins(5));
		mainPanel.add(phrasesPanel, data);

		FormPanel formPanel = new FormPanel();
		formPanel.setScrollMode(Scroll.AUTO);

		phraseNameField = new TextField<String>();
		phraseNameField.setFieldLabel(constants.phraseNameField());
		formPanel.add(phraseNameField);

		phraseKeyField = new TextField<String>();
		phraseKeyField.setFieldLabel(constants.phraseKeyField());
		formPanel.add(phraseKeyField);

		phraseValueField = new XHtmlField();
		phraseValueField.setHideLabel(true);
		formPanel.add(phraseValueField, new FormData(800, -1));

		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						Phrase phrase = new Phrase();
						phrase.setName(phraseNameField.getValue());
						phrase.setKey(phraseKeyField.getValue());
						phrase.setValue(phraseValueField.getValue());
						send(new SavePhraseMessage(phrase));
					}
				});
		new FormButtonBinding(formPanel).addButton(saveButton);
		formPanel.addButton(saveButton);

		data = new BorderLayoutData(LayoutRegion.EAST, 0.7f);
		data.setSplit(true);
		data.setMargins(new Margins(5, 5, 5, 0));
		mainPanel.add(formPanel, data);

		mainPanel.layout();
	}

	@Override
	public void showPhrases(List<Phrase> phrases) {
		phraseStore.removeAll();

		for (Phrase phrase : phrases) {
			BeanModel<Phrase> model = new BeanModel<Phrase>(phrase);
			phraseStore.add(model);
		}
	}

	@Override
	public void showPhrase(Phrase phrase) {
		phraseNameField.setValue(phrase.getName());
		phraseKeyField.setValue(phrase.getKey());
		phraseValueField.setValue(phrase.getValue());
	}

	@Override
	public void showDocumentsSection() {
		mainPanel.removeAll();

		ContentPanel documentsPanel = new ContentPanel(new FitLayout());
		documentsPanel.setHeading(constants.documentsHeading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(constants.addFolder(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(new AddFolderMessage());
					}
				}));
		toolBar.add(new Button(constants.addDocument(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(new AddDocumentMessage());
					}
				}));
		toolBar.add(new Button(appConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<DocumentModel> items = documentsGrid.getSelectionModel()
								.getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(), constants
									.confirmDelete(), new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									List<Integer> folderIds = new ArrayList<Integer>();
									List<Integer> documentIds = new ArrayList<Integer>();
									for (TreeModel treeModel : items) {
										DocumentModel model = (DocumentModel) treeModel;
										if (model.getFolder() != null)
											folderIds.add(model.getFolder().getId());
										else
											documentIds.add(model.getDocument().getId());
									}
									send(new DeleteFoldersMessage(folderIds));
									send(new DeleteDocumentsMessage(documentIds));
								}
							});
						}
					}
				}));
		documentsPanel.setTopComponent(toolBar);

		documentsStore = new TreeStore<DocumentModel>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig nameConfig = new ColumnConfig(DocumentModel.NAME,
				constants.nameColumn(), 200);
		nameConfig.setRenderer(new TreeGridCellRenderer<DocumentModel>() {
			@Override
			public Object render(DocumentModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<DocumentModel> store, Grid<DocumentModel> grid) {
				String value = (String) super.render(model, property, config, rowIndex,
						colIndex, store, grid);
				if (model.getFolder() != null) {
					Folder folder = model.getFolder();
					if (property.equals(DocumentModel.NAME) && folder.getName() == null)
						value = value.replace("null", "<i>" + constants.folderNoName()
								+ "</i>");
				}
				else {
					Document document = model.getDocument();
					if (property.equals(DocumentModel.NAME) && document.getName() == null)
						value = value.replace("null", "<i>" + constants.documentNoName()
								+ "</i>");
				}
				return value;
			}

			// else {
			// Document document = model.getDocument();
			// if (property.equals(Document.NAME) && document.getName() == null)
			// return "<i>" + constants.documentNoName() + "</i>";
			// else
			// return model.get(property);
		});
		columns.add(nameConfig);

		documentsGrid = new TreeGrid<DocumentModel>(documentsStore,
				new ColumnModel(columns));
		documentsGrid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<DocumentModel>() {
					@Override
					public void selectionChanged(SelectionChangedEvent<DocumentModel> se) {
						DocumentModel selectedItem = se.getSelectedItem();
						if (selectedItem != null) {
							if (selectedItem.getFolder() != null)
								send(new FolderSelectedMessage(selectedItem.getFolder()));
							else
								send(new DocumentSelectedMessage(selectedItem.getDocument()));
						}
					}
				});
		documentsGrid.getView().setSortingEnabled(false);
		documentsPanel.add(documentsGrid);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER, 0.3f);
		data.setSplit(true);
		data.setMargins(new Margins(5));
		mainPanel.add(documentsPanel, data);

		documentCardPanel = new CardPanel();

		folderFormPanel = new FormPanel();
		folderFormPanel.setScrollMode(Scroll.AUTO);
		folderFormPanel.setLabelWidth(150);
		folderFormPanel.setFieldWidth(300);

		folderNameField = new TextField<String>();
		folderNameField.setFieldLabel(constants.folderNameField());
		folderFormPanel.add(folderNameField);

		folderFormPanel.setButtonAlign(HorizontalAlignment.LEFT);
		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						Folder folder = new Folder();
						folder.setName(folderNameField.getValue());
						send(new SaveFolderMessage(folder));
					}
				});
		new FormButtonBinding(folderFormPanel).addButton(saveButton);
		folderFormPanel.addButton(saveButton);

		documentCardPanel.add(folderFormPanel);

		documentFormPanel = new FormPanel();
		documentFormPanel.setScrollMode(Scroll.AUTO);
		documentFormPanel.setLabelWidth(150);
		documentFormPanel.setFieldWidth(300);

		documentUrlField = new LabelField();
		documentFormPanel.add(documentUrlField);

		documentForm = new DocumentForm(constants, documentFormPanel,
				xgxtConstants, tags);
		documentForm.render();

		documentFormPanel.setButtonAlign(HorizontalAlignment.LEFT);
		saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						Document document = new DocumentImpl();
						documentForm.fetch(document);
						send(new SaveDocumentMessage(document));
					}
				});
		new FormButtonBinding(documentFormPanel).addButton(saveButton);
		documentFormPanel.addButton(saveButton);

		documentCardPanel.add(documentFormPanel);

		data = new BorderLayoutData(LayoutRegion.EAST, 0.7f);
		data.setSplit(true);
		data.setMargins(new Margins(5, 5, 5, 0));
		mainPanel.add(documentCardPanel, data);

		mainPanel.layout();
	}

	@Override
	public void showFolders(List<Folder> folders) {
		documentsStore.removeAll();

		for (Folder folder : folders) {
			DocumentModel folderModel = new DocumentModel();
			folderModel.setFolder(folder);

			for (Document document : folder.getDocuments()) {
				DocumentModel docModel = new DocumentModel();
				docModel.setDocument(document);
				folderModel.getChildren().add(docModel);
			}

			documentsStore.add(folderModel, true);
		}
	}

	@Override
	public void showFolder(Folder folder) {
		documentCardPanel.setActiveItem(folderFormPanel);
		folderNameField.setValue(folder.getName());
	}

	@Override
	public void showDocument(Document document) {
		documentCardPanel.setActiveItem(documentFormPanel);
		documentForm.setValues(document);
		documentUrlField.setText("<a href=\"http://web.imagebook.ru/"
				+ document.getKey() + "\" target=\"_blank\">Посмотреть на сайте</a>");
	}

	@Override
	public void showBannersSection() {
		mainPanel.removeAll();

		ContentPanel bannersPanel = new ContentPanel(new FitLayout());
		bannersPanel.setHeading(constants.bannersHeading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(appConstants.add(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(new AddBannerMessage());
					}
				}));
		toolBar.add(new Button(appConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<BeanModel<Banner>> items = bannerGrid
								.getSelectionModel().getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(), constants
									.confirmDelete(), new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<Banner> model : items) {
										ids.add(model.getBean().getId());
									}
									send(new DeleteBannersMessage(ids));
								}
							});
						}
					}
				}));
		bannersPanel.setTopComponent(toolBar);

		bannerStore = new ListStore<BeanModel<Banner>>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig nameConfig = new ColumnConfig(Banner.NAME,
				constants.nameColumn(), 200);
		nameConfig.setRenderer(new GridCellRenderer<BeanModel<Banner>>() {
			@Override
			public Object render(BeanModel<Banner> model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BeanModel<Banner>> store, Grid<BeanModel<Banner>> grid) {
				if (property.equals(Banner.NAME) && model.getBean().getName() == null)
					return "<i>" + constants.bannerNoName() + "</i>";
				return model.get(property);
			}
		});
		columns.add(nameConfig);

		bannerGrid = new Grid<BeanModel<Banner>>(bannerStore, new ColumnModel(
				columns));
		bannerGrid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<BeanModel<Banner>>() {
					@Override
					public void selectionChanged(
							SelectionChangedEvent<BeanModel<Banner>> se) {
						BeanModel<Banner> selectedItem = se.getSelectedItem();
						if (selectedItem != null) {
							Banner banner = selectedItem.getBean();
							send(new BannerSelectedMessage(banner));
						}
					}
				});
		bannerGrid.getView().setSortingEnabled(false);
		bannersPanel.add(bannerGrid);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER, 0.3f);
		data.setSplit(true);
		data.setMargins(new Margins(5));
		mainPanel.add(bannersPanel, data);

		FormPanel formPanel = new FormPanel();
		formPanel.setScrollMode(Scroll.AUTO);

		bannerNameField = new TextField<String>();
		bannerNameField.setFieldLabel(constants.bannerNameField());
		formPanel.add(bannerNameField);

		bannerTitleField = new TextField<String>();
		bannerTitleField.setFieldLabel(constants.bannerTitleField());
		formPanel.add(bannerTitleField);

		bannerUrlField = new TextField<String>();
		bannerUrlField.setFieldLabel(constants.bannerUrlField());
		formPanel.add(bannerUrlField);

		bannerTargetBlankField = new CheckBox();
		bannerTargetBlankField.setFieldLabel(constants.bannerTargetBlankField());
		bannerTargetBlankField.setBoxLabel("");
		formPanel.add(bannerTargetBlankField);

		bannerContentField = new XHtmlField();
		bannerContentField.setHideLabel(true);
		formPanel.add(bannerContentField, new FormData(800, -1));

		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						Banner banner = new Banner();
						banner.setName(bannerNameField.getValue());
						banner.setTitle(bannerTitleField.getValue());
						banner.setUrl(bannerUrlField.getValue());
						banner.setTargetBlank(bannerTargetBlankField.getValue());
						banner.setContent(bannerContentField.getValue());
						send(new SaveBannerMessage(banner));
					}
				});
		new FormButtonBinding(formPanel).addButton(saveButton);
		formPanel.addButton(saveButton);

		data = new BorderLayoutData(LayoutRegion.EAST, 0.7f);
		data.setSplit(true);
		data.setMargins(new Margins(5, 5, 5, 0));
		mainPanel.add(formPanel, data);

		mainPanel.layout();
	}

	@Override
	public void showBanners(List<Banner> banners) {
		bannerStore.removeAll();

		for (Banner banner : banners) {
			BeanModel<Banner> model = new BeanModel<Banner>(banner);
			bannerStore.add(model);
		}
	}

	@Override
	public void showBanner(Banner banner) {
		bannerNameField.setValue(banner.getName());
		bannerTitleField.setValue(banner.getTitle());
		bannerUrlField.setValue(banner.getUrl());
		bannerTargetBlankField.setValue(banner.isTargetBlank());
		bannerContentField.setValue(banner.getContent());
	}

	@Override
	public void showDirSection(List<Album> albums, String locale) {
		mainPanel.removeAll();

		ContentPanel dirPanel = new ContentPanel(new FitLayout());
		dirPanel.setHeading(constants.dirHeading());

		ToolBar toolBar = new ToolBar();
		Button addButton = new Button(constants.addDirButton());
		Menu menu = new Menu();
		menu.add(new MenuItem(constants.addSection1(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						send(new AddDirSection1Message());
					}
				}));
		menu.add(new MenuItem(constants.addSection2(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						send(new AddDirSection2Message());
					}
				}));
		addButton.setMenu(menu);
		toolBar.add(addButton);

		Button deleteButton = new Button(constants.deleteDirSection(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<DirModel> items = dirGrid.getSelectionModel()
								.getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(),
									constants.confirmDelete(), new Listener<BaseEvent>() {
										@Override
										public void handleEvent(BaseEvent be) {
											List<Integer> section1Ids = new ArrayList<Integer>();
											List<Integer> section2Ids = new ArrayList<Integer>();
											for (DirModel model : items) {
												if (model.getSection1() != null)
													section1Ids.add(model.getSection1().getId());
												else if (model.getSection2() != null)
													section2Ids.add(model.getSection2().getId());
											}
											send(new DeleteDirSectionsMessage(section1Ids, 1));
											send(new DeleteDirSectionsMessage(section2Ids, 2));
										}
									});
						}
					}
				});
		toolBar.add(deleteButton);

		dirPanel.setTopComponent(toolBar);

		dirStore = new TreeStore<DirModel>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig nameConfig = new ColumnConfig(DirModel.NAME,
				constants.nameColumn(), 200);
		nameConfig.setRenderer(new TreeGridCellRenderer<DirModel>() {
			@Override
			public Object render(DirModel model, String property, ColumnData config,
					int rowIndex, int colIndex, ListStore<DirModel> store,
					Grid<DirModel> grid) {
				String value = (String) super.render(model, property, config, rowIndex,
						colIndex, store, grid);
				if (model.getSection1() != null) {
					DirSection1 section1 = model.getSection1();
					if (property.equals(DirModel.NAME) && section1.getName() == null)
						value = value.replace("null", "<i>" + constants.sectionNoName()
								+ "</i>");
				}
				else if (model.getSection2() != null) {
					DirSection2 section2 = model.getSection2();
					if (property.equals(DirModel.NAME) && section2.getName() == null)
						value = value.replace("null", "<i>" + constants.sectionNoName()
								+ "</i>");
				}
				return value;
			}
		});
		columns.add(nameConfig);

		dirGrid = new TreeGrid<DirModel>(dirStore, new ColumnModel(columns));
		dirGrid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<DirModel>() {
					@Override
					public void selectionChanged(SelectionChangedEvent<DirModel> se) {
						DirModel selectedItem = se.getSelectedItem();
						if (selectedItem != null) {
							if (selectedItem.getSection1() != null)
								send(new DirSection1SelectedMessage(selectedItem.getSection1()));
							else if (selectedItem.getSection2() != null)
								send(new DirSection2SelectedMessage(selectedItem.getSection2()));
						}
					}
				});
		dirGrid.getView().setSortingEnabled(false);
		dirPanel.add(dirGrid);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER, 0.3f);
		data.setSplit(true);
		data.setMargins(new Margins(5));
		mainPanel.add(dirPanel, data);

		dirCardPanel = new CardPanel();

		section1FormPanel = new FormPanel();
		section1FormPanel.setScrollMode(Scroll.AUTO);
		section1FormPanel.setLabelWidth(150);
		section1FormPanel.setFieldWidth(300);

		section1IndexField = new NumberField();
		section1IndexField.setPropertyEditorType(Integer.class);
		section1IndexField.setFieldLabel(constants.sectionIndexField());
		section1FormPanel.add(section1IndexField);

		section1KeyField = new TextField<String>();
		section1KeyField.setFieldLabel(constants.sectionKeyField());
		section1FormPanel.add(section1KeyField);

		section1NameField = new TextField<String>();
		section1NameField.setFieldLabel(constants.sectionNameField());
		section1FormPanel.add(section1NameField);

		section1FormPanel.setButtonAlign(HorizontalAlignment.LEFT);
		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						DirSection1 section1 = new DirSection1();
						section1.setIndex((Integer) section1IndexField.getValue());
						section1.setKey(section1KeyField.getValue());
						section1.setName(section1NameField.getValue());
						send(new SaveDirSection1Message(section1));
					}
				});
		new FormButtonBinding(section1FormPanel).addButton(saveButton);
		section1FormPanel.addButton(saveButton);

		dirCardPanel.add(section1FormPanel);

		section2FormPanel = new FormPanel();
		section2FormPanel.setScrollMode(Scroll.AUTO);
		section2FormPanel.setLabelWidth(150);
		section2FormPanel.setFieldWidth(300);

		section2IndexField = new NumberField();
		section2IndexField.setPropertyEditorType(Integer.class);
		section2IndexField.setFieldLabel(constants.sectionIndexField());
		section2FormPanel.add(section2IndexField);

		section2KeyField = new TextField<String>();
		section2KeyField.setFieldLabel(constants.sectionKeyField());
		section2FormPanel.add(section2KeyField);

		section2NameField = new TextField<String>();
		section2NameField.setFieldLabel(constants.sectionNameField());
		section2FormPanel.add(section2NameField);

		albumsField = new MultiSelectField<Album>();
		albumsField.setFieldLabel(constants.albumsField());
		Formatter formatter = coreFactory.createFormatter();
		for (Album album : albums) {
			MultiString name = album.getName();
			String text = formatter.n2(album.getNumber()) + " - "
					+ name.getNonEmptyValue(locale);
			albumsField.add(album, text);
		}
		section2FormPanel.add(albumsField, new FormData(500, -1));

		section2PreviewField = new XHtmlField();
		section2PreviewField.setHideLabel(true);
		section2FormPanel.add(section2PreviewField, new FormData(800, -1));

		section2FormPanel.setButtonAlign(HorizontalAlignment.LEFT);
		saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						DirSection2 section2 = new DirSection2();
						section2.setIndex((Integer) section2IndexField.getValue());
						section2.setKey(section2KeyField.getValue());
						section2.setName(section2NameField.getValue());
						section2.setPreview(section2PreviewField.getValue());
						section2.getAlbums().clear();
						section2.getAlbums().addAll(albumsField.getXValue());
						send(new SaveDirSection2Message(section2));
					}
				});
		new FormButtonBinding(section2FormPanel).addButton(saveButton);
		section2FormPanel.addButton(saveButton);

		dirCardPanel.add(section2FormPanel);

		data = new BorderLayoutData(LayoutRegion.EAST, 0.7f);
		data.setSplit(true);
		data.setMargins(new Margins(5, 5, 5, 0));
		mainPanel.add(dirCardPanel, data);

		mainPanel.layout();
	}

	@Override
	public void showSections(List<DirSection1> sections) {
		dirStore.removeAll();

		for (DirSection1 section1 : sections) {
			DirModel section1Model = new DirModel();
			section1Model.setSection1(section1);

			for (DirSection2 section2 : section1.getSections()) {
				DirModel section2Model = new DirModel();
				section2Model.setSection2(section2);

				section1Model.getChildren().add(section2Model);
			}

			dirStore.add(section1Model, true);
		}
	}

	@Override
	public void showSection1(DirSection1 section) {
		dirCardPanel.setActiveItem(section1FormPanel);
		section1IndexField.setValue(section.getIndex());
		section1KeyField.setValue(section.getKey());
		section1NameField.setValue(section.getName());
	}

	@Override
	public void showSection2(DirSection2 section) {
		dirCardPanel.setActiveItem(section2FormPanel);
		section2IndexField.setValue(section.getIndex());
		section2KeyField.setValue(section.getKey());
		section2NameField.setValue(section.getName());
		section2PreviewField.setValue(section.getPreview());
		albumsField.setXValue(section.getAlbums());
	}
}
