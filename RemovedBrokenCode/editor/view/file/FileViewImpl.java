package ru.imagebook.client.editor.view.file;

import java.util.List;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.ListViewSelectionModel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.editor.ctl.file.AddImageMessage;
import ru.imagebook.client.editor.ctl.file.CancelShowNotificationMessage;
import ru.imagebook.client.editor.ctl.file.CreateFolderMessage;
import ru.imagebook.client.editor.ctl.file.DeleteFolderMessage;
import ru.imagebook.client.editor.ctl.file.DeleteImageMessage;
import ru.imagebook.client.editor.ctl.file.DisposeMessage;
import ru.imagebook.client.editor.ctl.file.EditFolderMessage;
import ru.imagebook.client.editor.ctl.file.FileMessages;
import ru.imagebook.client.editor.ctl.file.FileView;
import ru.imagebook.client.editor.ctl.file.FolderSelectedMessage;
import ru.imagebook.client.editor.ctl.file.ImageSelectedMessage;
import ru.imagebook.client.editor.ctl.file.ShowNotificationMessage;
import ru.imagebook.client.editor.view.DesktopConstants;
import ru.imagebook.client.editor.view.EditorWidgets;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.editor.ImageLayoutType;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.NotificationType;
import ru.imagebook.shared.model.editor.Page;
import ru.imagebook.shared.model.editor.PageType;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.file.FileBean;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gwt.crypto.UUID;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.HintPlugin;
import ru.minogin.core.client.gxt.form.XFormPanel;

@Singleton
public class FileViewImpl extends View implements FileView {
	private static final String PATH = "path";
	private static final String IMAGE_URL = "url";
	private static final String NAME = "name";

	private final Widgets widgets;
	private final FileConstants constants;
	private TreeStore<FileModel> foldersStore;
	private final FileBundle bundle;
	private final CommonConstants appConstants;

	private Window uploadWindow, showNotificationWindow;
	private Window createFolderWindow;
	private Window editFolderWindow;
	private XTreePanel<FileModel> treePanel;
	private ListStore<ModelData> imagesStore;
	private ListView<ModelData> imagesView;
	private Button imagesButton;
	private final ru.imagebook.client.editor.view.file.FileMessages messages;
	private MessageBox uploadProgress;
	private final DesktopConstants desktopConstants;
	private MenuItem disposeButton;
	private MenuItem addRightMenuItemButton;
	private MenuItem addLeftMenuItemButton;
	private MenuItem addSpreadMenuItemButton;
	private MenuItem mainAddRightMenuItemButton;
	private MenuItem mainAddLeftMenuItemButton;
	private MenuItem mainAddSpreadMenuItemButton;
	private MessageBox unpackingProgress;
	private MessageBox resizingProgress;

	@Inject
	public FileViewImpl(Dispatcher dispatcher, Widgets widgets, FileConstants constants,
			FileBundle bundle, CommonConstants appConstants,
			ru.imagebook.client.editor.view.file.FileMessages messages, DesktopConstants desktopConstants) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.bundle = bundle;
		this.appConstants = appConstants;
		this.messages = messages;
		this.desktopConstants = desktopConstants;
	}

	@Override
	public void layout() {
		ContentPanel foldersPanel = widgets.get(EditorWidgets.FOLDERS_PANEL);

		ToolBar toolBar = new ToolBar();

		Button folderActionsButton = new Button(constants.folderActionsButton());
		toolBar.add(folderActionsButton);

		foldersPanel.setTopComponent(toolBar);

		// ToolBar toolBar = new ToolBar();
		// toolBar.add(new Button(, new SelectionListener<ButtonEvent>() {
		// @Override
		// public void componentSelected(ButtonEvent ce) {
		//
		// }
		// }));
		// foldersPanel.setTopComponent(toolBar);

		foldersStore = new TreeStore<FileModel>();
		foldersStore.setMonitorChanges(true);
		foldersStore.setKeyProvider(new ModelKeyProvider<FileModel>() {
			public String getKey(FileModel model) {
				return model.getFile().getPath();
			}
		});

		treePanel = new XTreePanel<FileModel>(foldersStore);
		treePanel.setId("imagebook_editor_folders_panel");
		treePanel.setStateful(true);
		treePanel.setIconProvider(new ModelIconProvider<FileModel>() {
			@Override
			public AbstractImagePrototype getIcon(FileModel model) {
				return AbstractImagePrototype.create(bundle.folder());
			}
		});
		treePanel.setDisplayProperty(FileModel.NAME);
		TreePanelSelectionModel<FileModel> selectionModel = treePanel.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.SINGLE);
		selectionModel.addSelectionChangedListener(new SelectionChangedListener<FileModel>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<FileModel> se) {
				FileModel item = se.getSelectedItem();

				if (item != null)
					send(new FolderSelectedMessage(item.getFile()));
			}
		});
		Menu menu = new Menu();
		menu.add(new MenuItem(constants.uploadButton(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(FileMessages.UPLOAD);
			}
		}));

		menu.add(new SeparatorMenuItem());

		addLeftMenuItemButton = new MenuItem(constants.addLeftMenuItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(new ShowNotificationMessage(NotificationType.CREATE_PACKAGE_ORDER.getType(), ImageLayoutType.FOLDER_BACKGROUND_LEFT, true));
			}
		});
		addLeftMenuItemButton.disable();
		menu.add(addLeftMenuItemButton);

		addRightMenuItemButton = new MenuItem(constants.addRightMenuItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(new ShowNotificationMessage(NotificationType.CREATE_PACKAGE_ORDER.getType(), ImageLayoutType.FOLDER_BACKGROUND_RIGHT, true));
			}
		});
		addRightMenuItemButton.disable();
		menu.add(addRightMenuItemButton);

		addSpreadMenuItemButton = new MenuItem(constants.addSpreadMenuItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(new ShowNotificationMessage(NotificationType.CREATE_PACKAGE_ORDER.getType(), ImageLayoutType.FOLDER_NORMAL, true));
			}
		});
		addSpreadMenuItemButton.disable();
		menu.add(addSpreadMenuItemButton);

		menu.add(new SeparatorMenuItem());

		menu.add(new MenuItem(constants.createFolderItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(FileMessages.CREATE_FOLDER_REQUEST);
			}
		}));
		menu.add(new MenuItem(constants.renameFolderItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(FileMessages.EDIT_FOLDER_REQUEST);
			}
		}));
		menu.add(new MenuItem(constants.deleteItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				new ConfirmMessageBox(appConstants.warning(), constants.confirmDeleteFolder(),
						new Listener<BaseEvent>() {
							@Override
							public void handleEvent(BaseEvent be) {
								send(new DeleteFolderMessage());
							}
						});
			}
		}));



		menu.add(new SeparatorMenuItem());

		disposeButton = new MenuItem(desktopConstants.disposeButton(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						new ConfirmMessageBox(appConstants.warning(), desktopConstants.confirmDispose(),
								new Listener<BaseEvent>() {
									@Override
									public void handleEvent(BaseEvent be) {
										send(new DisposeMessage());
									}
								});
					}
				});
		disposeButton.disable();
		menu.add(disposeButton);

		treePanel.setContextMenu(menu);

		menu = new Menu();
		menu.setWidth(200);
		menu.add(new MenuItem(constants.uploadButton(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(FileMessages.UPLOAD);
			}
		}));
		menu.add(new SeparatorMenuItem());
		menu.add(new MenuItem(constants.createFolderItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(FileMessages.CREATE_FOLDER_REQUEST);
			}
		}));
		menu.add(new MenuItem(constants.renameFolderItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(FileMessages.EDIT_FOLDER_REQUEST);
			}
		}));
		menu.add(new MenuItem(constants.deleteItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				new ConfirmMessageBox(appConstants.warning(), constants.confirmDeleteFolder(),
						new Listener<BaseEvent>() {
							@Override
							public void handleEvent(BaseEvent be) {
								send(new DeleteFolderMessage());
							}
						});
			}
		}));
		menu.add(new SeparatorMenuItem());
		mainAddLeftMenuItemButton = new MenuItem(constants.addLeftMenuItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(new ShowNotificationMessage(NotificationType.CREATE_PACKAGE_ORDER.getType(), ImageLayoutType.FOLDER_BACKGROUND_LEFT, true));
			}
		});
		mainAddLeftMenuItemButton.disable();
		menu.add(mainAddLeftMenuItemButton);

		mainAddRightMenuItemButton = new MenuItem(constants.addRightMenuItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(new ShowNotificationMessage(NotificationType.CREATE_PACKAGE_ORDER.getType(), ImageLayoutType.FOLDER_BACKGROUND_RIGHT, true));
			}
		});
		mainAddRightMenuItemButton.disable();
		menu.add(mainAddRightMenuItemButton);

		mainAddSpreadMenuItemButton = new MenuItem(constants.addSpreadMenuItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				send(new ShowNotificationMessage(NotificationType.CREATE_PACKAGE_ORDER.getType(), ImageLayoutType.FOLDER_NORMAL, true));
			}
		});
		mainAddSpreadMenuItemButton.disable();
		menu.add(mainAddSpreadMenuItemButton);
		folderActionsButton.setMenu(menu);

		foldersPanel.add(treePanel);

		ContentPanel imagesPanel = widgets.get(EditorWidgets.PREVIEW_PANEL);

		toolBar = new ToolBar();
		imagesButton = new Button(constants.imageActionsButton());
		imagesButton.setMenu(new Menu());
		toolBar.add(imagesButton);
		imagesPanel.setTopComponent(toolBar);

		imagesStore = new ListStore<ModelData>();

		imagesView = new ListView<ModelData>(imagesStore, XTemplate.create(getTemplate()));
		imagesView.setBorders(false);
		imagesView.setItemSelector("div.editor-preview-wrap");
		ListViewSelectionModel<ModelData> imagesSelectionModel = imagesView.getSelectionModel();
		imagesSelectionModel.setSelectionMode(SelectionMode.SINGLE);
		imagesSelectionModel.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<ModelData> se) {
				ModelData item = se.getSelectedItem();
				if (item != null) {
					String path = (String) item.get(PATH);
					send(new ImageSelectedMessage(path));
				}
			}
		});
		imagesPanel.add(imagesView);
	}

	private void enableDisposeFolderButtons(AlbumOrder order, int pageNumber) {
		if (order != null) {
			Layout layout = order.getLayout();
			List<Page> pages = layout.getPages();
			Page page = pages.get(pageNumber);

			if (!page.isBlocked()) {
				if (pageNumber == 0 || page.getType() == PageType.NORMAL) {
					addSpreadMenuItemButton.enable();
					addSpreadMenuItemButton.setText(constants.addMenuItem());
					addLeftMenuItemButton.disable();
					addRightMenuItemButton.disable();
					mainAddLeftMenuItemButton.disable();
					mainAddRightMenuItemButton.disable();
					mainAddSpreadMenuItemButton.enable();
					mainAddSpreadMenuItemButton.setText(constants.addMenuItem());
				} else {
					addLeftMenuItemButton.enable();
					addRightMenuItemButton.enable();
					addSpreadMenuItemButton.enable();
					addSpreadMenuItemButton.setText(constants.addSpreadMenuItem());
					mainAddLeftMenuItemButton.enable();
					mainAddRightMenuItemButton.enable();
					mainAddSpreadMenuItemButton.enable();
					mainAddSpreadMenuItemButton.setText(constants.addSpreadMenuItem());
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void showFolders(FileBean root) {
		FileModel rootModel = new FileModel(root);
		addChildren(rootModel, root);

		foldersStore.removeAll();
		foldersStore.add((List) rootModel.getChildren(), true);
		treePanel.stExpand(foldersStore.getRootItems());
	}

	public void showNotificationMessage(final Integer type, final int imageLayoutType, final boolean isShowMessage) {
		if (isShowMessage) {
			showNotificationWindow = new Window();
			showNotificationWindow.setHeading(constants.showNotificationWindow());
			showNotificationWindow.setLayout(new FitLayout());
			showNotificationWindow.setModal(true);
			showNotificationWindow.setSize(335, 150);
			FormPanel formPanel = new XFormPanel();
			formPanel.setHeaderVisible(false);
			final NotificationType notificationType = NotificationType.getNotificationTypeById(type);
			final Label nameField = new Label(notificationType.getMessage());
			final com.extjs.gxt.ui.client.widget.form.CheckBox cb = new com.extjs.gxt.ui.client.widget.form.CheckBox();
			formPanel.add(nameField);
			//formPanel.add(cb);
			Button createButton = new Button(appConstants.ok(),
					new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							if (cb.getValue()) {
								send(new CancelShowNotificationMessage(notificationType.getType()));
								send(new AddImageMessage(imageLayoutType));
							} else {
								send(new AddImageMessage(imageLayoutType));
							}
							showNotificationWindow.hide();
						}
					});
			formPanel.getButtonBar().add(cb);
			formPanel.getButtonBar().add(new Label(constants.cancelShowMessage()));
			formPanel.addButton(createButton);
			new FormButtonBinding(formPanel).addButton(createButton);
			formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					showNotificationWindow.hide();
				}
			}));
			showNotificationWindow.add(formPanel);
			showNotificationWindow.show();
		} else {
			send(new AddImageMessage(imageLayoutType));
		}
	}

	private void addChildren(FileModel model, FileBean file) {
		for (FileBean child : file.getChildren()) {
			FileModel childModel = new FileModel(child);
			model.getChildren().add(childModel);
			addChildren(childModel, child);
		}
	}

	@Override
	public void showImages(List<String> names, String sessionId, FileBean folder) {
		imagesStore.removeAll();

		for (final String name : names) {
			final String path = folder.getPath() + "/" + name;
			String url = GWT.getHostPageBaseURL() + "preview?a=" + sessionId + "&b=" + URL.encode(path);
			String salt = new UUID().toString();
			url += "&" + salt;

			ModelData model = new BaseModel();
			model.set(PATH, path);
			model.set(IMAGE_URL, url);
			model.set(NAME, name);
			imagesStore.add(model);
		}

		imagesView.getSelectionModel().select(0, false);
	}

	@Override
	public void showImageMenu(AlbumOrder order, int pageNumber) {
		imagesButton.setMenu(createMenu(order, pageNumber));
		imagesView.setContextMenu(createMenu(order, pageNumber));
		enableDisposeFolderButtons(order, pageNumber);
	}

	@Override
	public void disableDisposeFolderImagesMenu() {
		addRightMenuItemButton.disable();
		addLeftMenuItemButton.disable();
		addSpreadMenuItemButton.disable();
		mainAddRightMenuItemButton.disable();
		mainAddLeftMenuItemButton.disable();
		mainAddSpreadMenuItemButton.disable();
	}

	private Menu createMenu(AlbumOrder order, int pageNumber) {
		Menu menu = new Menu();

		if (order != null) {
			Layout layout = order.getLayout();
			List<Page> pages = layout.getPages();
			Page page = pages.get(pageNumber);

			if (!page.isBlocked()) {
				final NotificationType notificationType = page.isCommon() ? NotificationType.CHANGE_IMAGE_ON_COMMON_PAGE : NotificationType.CHANGE_IMAGE_ON_INDIVIDUAL_PAGE;
				if (page.getType() == PageType.NORMAL) {
					menu.add(new MenuItem(constants.addMenuItem(), new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							send(new ShowNotificationMessage(notificationType.getType(), ImageLayoutType.NORMAL, false));
						}
					}));
				}
				else if (page.isSpreadOrFlyLeafPage()) {
					menu.add(new MenuItem(constants.addLeftMenuItem(), new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							send(new ShowNotificationMessage(notificationType.getType(), ImageLayoutType.BACKGROUND_LEFT, false));
						}
					}));
					menu.add(new MenuItem(constants.addRightMenuItem(), new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							send(new ShowNotificationMessage(notificationType.getType(), ImageLayoutType.BACKGROUND_RIGHT, false));
						}
					}));
					menu.add(new MenuItem(constants.addSpreadMenuItem(), new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							send(new ShowNotificationMessage(notificationType.getType(), ImageLayoutType.NORMAL, false));
						}
					}));
				}
				menu.add(new SeparatorMenuItem());
			}
		}

		menu.add(new MenuItem(constants.deleteImageMenuItem(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				new ConfirmMessageBox(appConstants.warning(), constants.confirmDeleteImage(),
						new Listener<BaseEvent>() {
							@Override
							public void handleEvent(BaseEvent be) {
								send(new DeleteImageMessage());
							}
						});
			}
		}));

		return menu;
	}

	private native String getTemplate() /*-{
		return ['<tpl for=".">',
		'<div class="editor-preview-wrap" id="{path}" style="border: 1px solid white">',
		'<div class="editor-preview"><img src="{url}" title="{name}"></div>',
		'<span class="editor-preview-name">{name}</span>',
		'</div>',
		'</tpl>',
		'<div class="x-clear"></div>'].join("");
	}-*/;

	@Override
	public void showUploadForm(FileBean folder, String sessionId) {
		uploadWindow = new Window();
		uploadWindow.setSize(400, 150);
		uploadWindow.setLayout(new FitLayout());

		final FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setEncoding(Encoding.MULTIPART);
		formPanel.setMethod(Method.POST);
		formPanel.setAction(GWT.getHostPageBaseURL() + "upload");
		formPanel.setLabelWidth(150);

		// HiddenField<String> sessionField = new HiddenField<String>();
		// sessionField.setName("a");
		// sessionField.setValue(sessionId);
		// formPanel.add(sessionField);

		HiddenField<String> pathField = new HiddenField<String>();
		pathField.setName("a");
		pathField.setValue(URL.encode(folder.getPath()));
		formPanel.add(pathField);

		FileUploadField field = new FileUploadField();
		field.setName("file");
		field.setAllowBlank(false);
		field.setFieldLabel(constants.uploadField());
		field.addPlugin(new HintPlugin(constants.uploadFieldHint()));
		formPanel.add(field);

		Button uploadButton = new Button(constants.uploadFormButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						uploadProgress = MessageBox.progress(constants.uploadProgressTitle(),
								constants.uploadProgressMessage(), "");

						formPanel.submit();
					}
				});
		formPanel.addButton(uploadButton);

		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				uploadWindow.hide();
			}
		}));

		new FormButtonBinding(formPanel).addButton(uploadButton);

		uploadWindow.add(formPanel);

		// String text = bundle.uploadForm().getText();
		// text = text.replace("${sessionId}", sessionId);
		// String path = URL.encode(folder.getPath());
		// text = text.replace("${path}", path);
		// text = text.replace("${uploadUrl}", GWT.getModuleBaseURL() + "upload");
		// Html html = new Html(text);
		// uploadWindow.add(html);

		uploadWindow.show();
	}

	@Override
	public void alertFolderNotSelected() {
		MessageBox.alert(appConstants.warning(), constants.folderNotSelected(), null);
	}

	@Override
	public void hideUploadForm() {
		uploadWindow.hide();
	}

	@Override
	public void showCreateFolderForm() {
		createFolderWindow = new Window();
		createFolderWindow.setHeading(constants.createFolderWindow());
		createFolderWindow.setLayout(new FitLayout());
		createFolderWindow.setModal(true);
		createFolderWindow.setSize(400, 150);
		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		final TextField<String> nameField = new TextField<String>();
		nameField.setAllowBlank(false);
		nameField.setFieldLabel(constants.folderNameField());
		formPanel.add(nameField);
		Button createButton = new Button(constants.createButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(new CreateFolderMessage(nameField.getValue()));
					}
				});
		formPanel.addButton(createButton);
		new FormButtonBinding(formPanel).addButton(createButton);
		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				createFolderWindow.hide();
			}
		}));
		createFolderWindow.add(formPanel);
		createFolderWindow.show();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				nameField.focus();
			}
		});
	}

	@Override
	public void hideCreateFolderForm() {
		createFolderWindow.hide();
	}

	@Override
	public void showEditFolderForm(String name) {
		editFolderWindow = new Window();
		editFolderWindow.setHeading(constants.editFolderWindow());
		editFolderWindow.setLayout(new FitLayout());
		editFolderWindow.setModal(true);
		editFolderWindow.setSize(400, 150);
		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		final TextField<String> nameField = new TextField<String>();
		nameField.setAllowBlank(false);
		nameField.setFieldLabel(constants.folderNameField());
		nameField.setValue(name);
		formPanel.add(nameField);
		Button saveButton = new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(new EditFolderMessage(nameField.getValue()));
			}
		});
		formPanel.addButton(saveButton);
		new FormButtonBinding(formPanel).addButton(saveButton);
		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				editFolderWindow.hide();
			}
		}));
		editFolderWindow.add(formPanel);
		editFolderWindow.show();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				nameField.focus();
			}
		});
	}

	@Override
	public void hideEditFolderForm() {
		editFolderWindow.hide();
	}

	@Override
	public void selectFolder(FileBean baseFolder) {
		TreePanelSelectionModel<FileModel> selectionModel = treePanel.getSelectionModel();
		FileModel model = foldersStore.findModel(FileModel.FILE, baseFolder);
		selectionModel.select(model, false);
	}

	@Override
	public void updateUploadProgress(double value) {
		uploadProgress.updateProgress(value, messages.uploadProgressText((int) (value * 100)));
	}

	@Override
	public void hideUploadProgress() {
		uploadProgress.close();
	}

	@Override
	public void alertUploadFailed() {
		MessageBox.alert(appConstants.error(), constants.uploadFailed(), null);
	}

	@Override
	public void alertWrongFileType() {
		MessageBox.alert(appConstants.error(), constants.wrongFileType(), null);
	}

	@Override
	public void enableDisposeButton() {
		disposeButton.enable();
	}

	@Override
	public void disableDisposeButton() {
		disposeButton.disable();
	}

	@Override
	public void showUnpackingProgress() {
		unpackingProgress = MessageBox.wait(constants.unpackingProgressTitle(),
				constants.unpackingProgressMessage(), appConstants.pleaseWait());
	}

	@Override
	public void hideUnpackingProgress() {
		unpackingProgress.close();
	}

    @Override
    public void alertNonZipArchiveFormat() {
        MessageBox.alert(appConstants.error(), constants.nonZipArchiveFormat(), null);
    }

    @Override
    public void alertUnsupportedImageType(String filename) {
        MessageBox.alert(appConstants.error(), constants.unsupportedImageType(filename), null);
    }

	@Override
	public void alertUnpackingFailed() {
		MessageBox.alert(appConstants.error(), constants.unpackingFailed(), null);
	}

	@Override
	public void showResizingProgress() {
		resizingProgress = MessageBox.progress(constants.resizingProgressTitle(),
				constants.resizingProgressMessage(), "");
	}

	@Override
	public void hideResizingProgress() {
		resizingProgress.close();
	}

	@Override
	public void alertResizingFailed() {
		MessageBox.alert(appConstants.error(), constants.resizingFailed(), null);
	}

	@Override
	public void updateResizeProgress(double value) {
		resizingProgress.updateProgress(value, messages.resizingProgressText((int) (value * 100)));
	}
}
