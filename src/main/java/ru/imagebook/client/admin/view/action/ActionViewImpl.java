package ru.imagebook.client.admin.view.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import static ru.imagebook.shared.model.DeliveryDiscount.DISCOUNT_PC;
import static ru.imagebook.shared.model.DeliveryDiscount.SUM;
import ru.imagebook.client.admin.ctl.action.ActionMessages;
import ru.imagebook.client.admin.ctl.action.ActionView;
import ru.imagebook.client.admin.ctl.action.AddActionMessage;
import ru.imagebook.client.admin.ctl.action.AddCodesMessage;
import ru.imagebook.client.admin.ctl.action.DeleteActionsMessage;
import ru.imagebook.client.admin.ctl.action.DeleteBonusCodesMessage;
import ru.imagebook.client.admin.ctl.action.GenerateCodesMessage;
import ru.imagebook.client.admin.ctl.action.GenerateCodesRequestMessage;
import ru.imagebook.client.admin.ctl.action.LoadAlbumsMessage;
import ru.imagebook.client.admin.ctl.action.LoadVendorActionsMessage;
import ru.imagebook.client.admin.ctl.action.OpenStatusRequestMessage;
import ru.imagebook.client.admin.ctl.action.RejectRequestMessage;
import ru.imagebook.client.admin.ctl.action.SearchActionCodeMessage;
import ru.imagebook.client.admin.ctl.action.SendStatusCodeMessage;
import ru.imagebook.client.admin.ctl.action.ShowBonusCodesByActionMessage;
import ru.imagebook.client.admin.ctl.action.ShowCodesMessage;
import ru.imagebook.client.admin.ctl.action.ShowRejectFormMessage;
import ru.imagebook.client.admin.ctl.action.ShowSendFormMessage;
import ru.imagebook.client.admin.ctl.action.UpdateActionMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.admin.Actions;
import ru.imagebook.client.common.service.order.OrderService;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.DeliveryDiscount;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.StatusRequest.Source;
import ru.imagebook.shared.model.StatusRequestState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gwt.DateFormat;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.SearchField;
import ru.minogin.core.client.gxt.SearchHandler;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.BooleanField;
import ru.minogin.core.client.gxt.form.EmailField;
import ru.minogin.core.client.gxt.form.IntegerField;
import ru.minogin.core.client.gxt.form.MultiSelectField;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.SelectValue;
import ru.minogin.core.client.gxt.form.XDateField;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextArea;
import ru.minogin.core.client.gxt.form.XTextField;
import ru.minogin.core.client.text.StringUtil;

@Singleton
public class ActionViewImpl extends View implements ActionView {
	private static final String USER_TEXT = "userText";
	private static final String STATE_TEXT = "stateText";
	private static final String DATE_TEXT = "dateText";
	private static final String SOURCE_TEXT = "sourceText";
	private static final String NUMBER_TEXT = "num";
	private static final String APPLIED_CODE_TEXT = "applied";
    private static final String CODE_ORDERS_PATTERN = " - ";

	private final Widgets widgets;
	private final ActionConstants constants;
	private final ActionBundle bundle;
	private final CommonConstants appConstants;
	private final SecurityService securityService;
	private final OrderService orderService;
	private final I18nService i18nService;

	private Grid<BeanModel<BonusAction>> grid;
	private Grid<BeanModel<BonusCode>> codesGrid;
	private ListStore<BeanModel<BonusAction>> store;
	private ListStore<BeanModel<BonusCode>> codesStore;
	private Button generateButton;
	private FormButtonBinding binding;
	private com.extjs.gxt.ui.client.widget.Window window;
	private ContentPanel mainPanel;
	private ListStore<BeanModel<StatusRequest>> statusRequestsStore;
	private Window sendWindow;

	private Grid<BeanModel<StatusRequest>> requestsGrid;
	private Window requestWindow;
	private XTextField nameField;
	private XDateField dateStartField;
	private XDateField dateEndField;
	private IntegerField level1Field;
	private IntegerField level2Field;
	private IntegerField permanentLevelField;
	private IntegerField discount1Field;
	private IntegerField discount2Field;
	private Window addWindow;
	private Window editWindow;
	private BooleanField repeatalField;
	private Window addCodesWindow;
	private IntegerField discountSumField;
	private IntegerField codeLengthField;
	private MultiSelectField<Album> productField;
    private IntegerField discountPCField;
	private ActionPresenter presenter;
	private Map<Integer, ListStore<DeliveryDiscountModel>> deliveryDiscountStoreMap;
	private Map<Integer, EditorGrid<DeliveryDiscountModel>> deliveryDiscountGridMap;


	@Inject
	public ActionViewImpl(Dispatcher dispatcher, Widgets widgets, ActionConstants constants,
						  CommonConstants appConstants, ActionBundle bundle, SecurityService securityService,
						  OrderService orderService, I18nService i18nService) {
		super(dispatcher);
		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
		this.bundle = bundle;
		this.securityService = securityService;
		this.orderService = orderService;
		this.i18nService = i18nService;
	}

	@Override
	public void setPresenter(ActionPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showSection() {
		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);

		ContentPanel panel = new ContentPanel(new FitLayout());
		panel.setHeaderVisible(false);

		ToolBar toolBar = new ToolBar();
		if (securityService.isAllowed(Actions.REQUEST_STATUS)) {
			toolBar.add(new Button(constants.status(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(ActionMessages.SHOW_STATUS_SECTION);
				}
			}));
		}
		toolBar.add(new Button(constants.actions(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(ActionMessages.SHOW_ACTIONS_SECTION);
			}
		}));
		toolBar.add(new Button(constants.delivery(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				presenter.showDeliverySection();
			}
		}));
		panel.setTopComponent(toolBar);

		mainPanel = new ContentPanel(new FitLayout());
		mainPanel.setHeaderVisible(false);

		panel.add(mainPanel);

		desktop.removeAll();
		desktop.add(panel);
		desktop.layout();
	}

	@Override
	public void showActionSection(List<Vendor> vendors) {
		mainPanel.removeAll();
		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);

		LayoutContainer container = new LayoutContainer(new BorderLayout());

		final ContentPanel innerActionPanel = new ContentPanel(new FitLayout());
		final ContentPanel innerBonusCodePanel = new ContentPanel(new FitLayout());
		innerActionPanel.setHeaderVisible(false);
		innerBonusCodePanel.setHeaderVisible(false);

		final ToolBar toolBarAction = new ToolBar();
		final ToolBar toolBarBonusCode = new ToolBar();

		toolBarAction.add(new Button(appConstants.add(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(new LoadAlbumsMessage(null/*BonusAction*/));
			}
		}));
		final Button editButton = new Button(appConstants.edit(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				BeanModel<BonusAction> selectedItem = grid.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					send(new LoadAlbumsMessage(selectedItem.getBean()));
				}
			}
		});
		toolBarAction.add(editButton);
		toolBarBonusCode.add(new Button(constants.codesButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				BeanModel<BonusAction> item = grid.getSelectionModel().getSelectedItem();
				if (item != null)
					send(new ShowCodesMessage(item.getBean()));
			}
		}));
		toolBarBonusCode.add(new Button(constants.addCodesButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				BeanModel<BonusAction> item = grid.getSelectionModel().getSelectedItem();
				if (item != null)
					showAddCodesForm(item.getBean());
			}
		}));
		toolBarBonusCode.add(new Button(constants.generateButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				BeanModel<BonusAction> item = grid.getSelectionModel().getSelectedItem();
				if (item != null)
					send(new GenerateCodesRequestMessage(item.getBean()));
			}
		}));
		toolBarAction.add(new Button(appConstants.delete(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final List<BeanModel<BonusAction>> selectedItems = grid.getSelectionModel().getSelectedItems();
				if (selectedItems.isEmpty())
					return;

				new ConfirmMessageBox(appConstants.warning(), constants.confirmDelete(), new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						List<Integer> ids = new ArrayList<Integer>();
						for (BeanModel<BonusAction> item : selectedItems) {
							ids.add(item.getBean().getId());
						}
						send(new DeleteActionsMessage(ids));
					}
				});

			}
		}));

		toolBarBonusCode.add(new Button(appConstants.delete(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final List<BeanModel<BonusCode>> selectedItems = codesGrid.getSelectionModel().getSelectedItems();
				final BeanModel<BonusAction> item = grid.getSelectionModel().getSelectedItem();
				if (item != null) {
					if (selectedItems.isEmpty())
						return;

					new ConfirmMessageBox(appConstants.warning(), constants.confirmBonusCodeDelete(), new Listener<BaseEvent>() {
						@Override
						public void handleEvent(BaseEvent be) {
							List<Integer> ids = new ArrayList<Integer>();
							for (BeanModel<BonusCode> item : selectedItems) {
								ids.add(item.getBean().getId());
							}
							send(new DeleteBonusCodesMessage(ids, item.getBean().getId() ));
						}
					});
				}

			}
		}));

		if (vendors != null) {
			Vendor imgbook = null;
            SelectField<Vendor> shownVendor = new SelectField<Vendor>();
			for (Vendor vendor : vendors) {
				shownVendor.add(vendor, vendor.getName());
				if (vendor.getType() == VendorType.IMAGEBOOK) {
					imgbook = vendor;
				}
			}
			toolBarAction.add(shownVendor);
			shownVendor.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Vendor>>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<SelectValue<Vendor>> se) {
					Vendor vendor = se.getSelectedItem().getValue();
					if (vendor.getType() == VendorType.IMAGEBOOK) {
						editButton.setText(appConstants.edit());
					}
					else {
						editButton.setText(constants.show());
					}
					send(new LoadVendorActionsMessage(vendor.getId()));
				}
			});
			shownVendor.setValue(new SelectValue<Vendor>(imgbook, imgbook.getName()));
		}

		toolBarAction.add(new FillToolItem());

		final SearchField searchField = new SearchField(new SearchHandler() {
			@Override
			public void onSearch(String query) {
				send(new SearchActionCodeMessage(query));
			}
		});
		toolBarAction.add(searchField);
		innerActionPanel.setTopComponent(toolBarAction);
		innerBonusCodePanel.setTopComponent(toolBarBonusCode);


		store = new ListStore<BeanModel<BonusAction>>();
		codesStore = new ListStore<BeanModel<BonusCode>>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		columns.add(new ColumnConfig(BonusAction.NAME, constants.nameColumn(), 200));
		columns.add(new ColumnConfig(BonusAction.DATE_START_FIELD, constants.dateStartField(), 200));
		columns.add(new ColumnConfig(BonusAction.DATE_END_FIELD, constants.dateEndField(), 200));

		List<ColumnConfig> codesColumns = new ArrayList<ColumnConfig>();
		codesColumns.add(new ColumnConfig(NUMBER_TEXT, constants.numberField(), 20));
		codesColumns.add(new ColumnConfig(BonusCode.CODE, constants.codeField(), 100));
		codesColumns.add(new ColumnConfig(APPLIED_CODE_TEXT, constants.appliedByField(), 300));


		codesGrid = new Grid<BeanModel<BonusCode>>(codesStore, new ColumnModel(codesColumns));
		codesGrid.setSelectionModel(new GridSelectionModel<BeanModel<BonusCode>>());
		codesGrid.getView().setSortingEnabled(false);

		grid = new Grid<BeanModel<BonusAction>>(store, new ColumnModel(columns));
		grid.setSelectionModel(new GridSelectionModel<BeanModel<BonusAction>>());
		grid.getView().setSortingEnabled(false);
		grid.addListener(Events.RowClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				loadBonusCodes();
			}
		});

		innerActionPanel.add(grid);
		innerBonusCodePanel.add(codesGrid);

		container.add(innerActionPanel, new BorderLayoutData(Style.LayoutRegion.CENTER, 0.5f));
		container.add(innerBonusCodePanel, new BorderLayoutData(Style.LayoutRegion.EAST, 0.5f));

		mainPanel.add(container);
		mainPanel.layout();
	}

	@Override
	public void loadBonusCodes() {
		BeanModel<BonusAction> item = grid.getSelectionModel().getSelectedItem();
		if (item != null)
            send(new ShowBonusCodesByActionMessage(item.getBean()));
	}

	private void showAddCodesForm(final BonusAction action) {
		addCodesWindow = new XWindow(constants.addCodesHeading() + ": " + action.getName());
		addCodesWindow.setHeight(700);
		FormPanel formPanel = new XFormPanel();

		final XTextArea codesField = new XTextArea(constants.codesField(), false, formPanel);
		codesField.setHeight(600);

// final XTextField codeField = new XTextField(constants.codeField(), false,
// null, formPanel);

		final Button saveButton = new Button(appConstants.save());
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				saveButton.disable();
				send(new AddCodesMessage(action.getId(), codesField.getValue()));
			}
		});
		formPanel.addButton(saveButton);
		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				addCodesWindow.hide();
			}
		}));
		new FormButtonBinding(formPanel).addButton(saveButton);

		addCodesWindow.add(formPanel);
		addCodesWindow.show();
	}

	@Override
	public void hideAddCodesForm() {
		addCodesWindow.hide();
		loadBonusCodes();
	}

	private void showAddForm(final List<Album> albums) {
		addWindow = new XWindow();
		addWindow.setHeading(constants.addWindowHeading());

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel, albums);

		Button saveButton = new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				BonusAction action = new BonusAction();
				if (checkAndFetch(action)) {
					send(new AddActionMessage(action));
				} else {
					MessageBox.alert(appConstants.error(), constants.dateConstraintError(), null);
				}
			}
		});
		formPanel.addButton(saveButton);
		new FormButtonBinding(formPanel).addButton(saveButton);

		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				addWindow.hide();
			}
		}));
		addWindow.add(formPanel);

		addWindow.show();
	}

	@Override
	public void hideAddForm() {
		addWindow.hide();
	}

	@Override
	public void alertBonusCodeOrderExists() {
		MessageBox.alert(appConstants.error(), constants.bonusCodeOrderError(), null);
	}

	public void showActionForm(final List<Album> albums, BonusAction bonusAction) {
		if (bonusAction == null) {
			showAddForm(albums);
		} else {
			showEditForm(bonusAction, albums);
		}

	}

	private void showEditForm(final BonusAction action, final List<Album> albums) {
		editWindow = new XWindow();
		editWindow.setHeading(constants.editWindowHeading());

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel, albums);
		nameField.setValue(action.getName());
		dateStartField.setValue(action.getDateStart());
		dateEndField.setValue(action.getDateEnd());
		level1Field.setValue(action.getLevel1());
		level2Field.setValue(action.getLevel2());
		permanentLevelField.setValue(action.getPermanentLevel());
		discount1Field.setValue(action.getDiscount1());
		discount2Field.setValue(action.getDiscount2());
		repeatalField.setValue(action.isRepeatal());
		discountSumField.setValue(action.getDiscountSum());
		discountPCField.setValue(action.getDiscountPCenter());
		codeLengthField.setValue(action.getCodeLength());
		productField.setXValue(action.getAlbums());

		if (!securityService.isAllowed(Actions.VIEW_ALL_VENDORS) || action.getVendor().getType() == VendorType.IMAGEBOOK) {
			Button saveButton = new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (checkAndFetch(action)) {
						send(new UpdateActionMessage(action));
					} else {
						MessageBox.alert(appConstants.error(), constants.dateConstraintError(), null);
					}
				}
			});
			formPanel.addButton(saveButton);
			new FormButtonBinding(formPanel).addButton(saveButton);
		}
		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				editWindow.hide();
			}
		}));
		editWindow.add(formPanel);

		editWindow.show();
	}

	@Override
	public void hideEditForm() {
		editWindow.hide();
	}

	private void addFields(FormPanel formPanel, final List<Album> albums) {
		nameField = new XTextField(constants.nameField(), false, null, formPanel);
		dateStartField = new XDateField(constants.dateStartField(), false, new Date(), formPanel);
		dateEndField = new XDateField(constants.dateEndField(), true, null, formPanel);
		level1Field = new IntegerField(constants.level1Field(), false, null, formPanel);
		level2Field = new IntegerField(constants.level2Field(), false, null, formPanel);
		permanentLevelField = new IntegerField(constants.permanentLevelField(), false, null, formPanel);
		discount1Field = new IntegerField(constants.discount1Field(), false, null, formPanel);
		discount2Field = new IntegerField(constants.discount2Field(), false, null, formPanel);
		repeatalField = new BooleanField(constants.repeatalField(), false, formPanel);
		discountSumField = new IntegerField(constants.discountSumField(), true, null, formPanel);
		discountPCField = new IntegerField(constants.discountPCField(), true, null, formPanel);
		codeLengthField = new IntegerField(constants.codeLengthField(), true, null, formPanel);
		productField = new MultiSelectField<Album>();
		productField.setFieldLabel(constants.productsField());
		for (Album album : albums) {
			productField.add(album, orderService.getProductArticle(album) + " - " + album.getName().getNonEmptyValue(i18nService.getLocale()));
		}
		formPanel.add(productField);
	}

	private boolean checkAndFetch(BonusAction action) {
		if (dateEndField.getValue() != null && dateStartField.getValue().after(dateEndField.getValue())) {
			return false;
		}
		action.setName(nameField.getValue());
		action.setDateStart(dateStartField.getValue());
		action.setDateEnd(dateEndField.getValue());
		action.setLevel1(level1Field.getValue());
		action.setLevel2(level2Field.getValue());
		action.setPermanentLevel(permanentLevelField.getValue());
		action.setDiscount1(discount1Field.getValue());
		action.setDiscount2(discount2Field.getValue());
		action.setRepeatal(repeatalField.getValue());
		action.setDiscountSum(discountSumField.getValue());
		action.setDiscountPCenter(discountPCField.getValue());
		action.setCodeLength(codeLengthField.getValue());
		action.setAlbums(new TreeSet<Album>(productField.getXValueSet()));
		return true;
	}

	@Override
	public void showActions(List<BonusAction> actions) {
		store.removeAll();
		codesStore.removeAll();
		for (BonusAction action : actions) {
			store.add(new BeanModel<BonusAction>(action));
		}
	}

    @Override
	public void showBonusCodes(Map<BonusCode, List<Order<?>>> map) {
		codesStore.removeAll();
		for (Map.Entry<BonusCode, List<Order<?>>> code : map.entrySet()) {
			final BonusCode _code = code.getKey();
			_code.setTransient(NUMBER_TEXT, codesStore.getCount()+1);
			final StringBuilder textBuilder = new StringBuilder();
			for (Order order : code.getValue()) {
				if (textBuilder.length() > 0) textBuilder.append(", ");
				textBuilder.append(order.getUser().getUserName()).append(CODE_ORDERS_PATTERN).append(order.getUser().getFullName()).append(CODE_ORDERS_PATTERN).append(order.getNumber());
			}
			_code.setTransient(APPLIED_CODE_TEXT, textBuilder.toString());
			codesStore.add(new BeanModel<BonusCode>(_code));
		}
	}
	@Override
	public void showCodes(BonusAction action, String sessionId) {
		com.google.gwt.user.client.Window.open(
				GWT.getHostPageBaseURL() + "bonusCodes?sid=" + sessionId + "&id=" + action.getId(), null, null);
	}

	@Override
	public void showGenerateForm(final BonusAction action) {
		window = new com.extjs.gxt.ui.client.widget.Window();
		window.setModal(true);
		window.setOnEsc(false);
		window.setHeading(constants.generateHeading());
		window.setLayout(new FitLayout());
		window.setSize(400, 300);

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		LabelField nameField = new LabelField(action.getName());
		nameField.setHideLabel(true);
		formPanel.add(nameField);

		final NumberField quantityField = new NumberField();
		quantityField.setPropertyEditorType(Integer.class);
		quantityField.setAllowBlank(false);
		quantityField.setAllowNegative(false);
		quantityField.setAllowDecimals(false);
		quantityField.setFieldLabel(constants.quantity());
		formPanel.add(quantityField);

		generateButton = new Button(constants.generateButton2(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				binding.stopMonitoring();
				generateButton.disable();
				send(new GenerateCodesMessage(action.getId(), (Integer) quantityField.getValue()));
			}
		});
		formPanel.addButton(generateButton);

		binding = new FormButtonBinding(formPanel);
		binding.addButton(generateButton);

		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				window.hide();
			}
		}));

		window.add(formPanel);

		window.show();
	}

	@Override
	public void informGenerateResult() {
		MessageBox.info(appConstants.info(), constants.generateResult(), null);
		loadBonusCodes();
		window.hide();
	}

	@Override
	public void showStatusSection() {
		mainPanel.removeAll();

		ContentPanel panel = new ContentPanel(new FitLayout());
		panel.setHeaderVisible(false);

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(constants.openButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				BeanModel<StatusRequest> item = requestsGrid.getSelectionModel().getSelectedItem();
				if (item != null) {
					StatusRequest request = item.getBean();
					send(new OpenStatusRequestMessage(request));
				}
			}
		}));

		panel.setTopComponent(toolBar);

		statusRequestsStore = new ListStore<BeanModel<StatusRequest>>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(DATE_TEXT, constants.dateColumn(), 80));
		columns.add(new ColumnConfig(USER_TEXT, constants.userColumn(), 200));
		columns.add(new ColumnConfig(STATE_TEXT, constants.stateColumn(), 150));
		columns.add(new ColumnConfig(SOURCE_TEXT, constants.sourceColumn(), 80));

		requestsGrid = new Grid<BeanModel<StatusRequest>>(statusRequestsStore, new ColumnModel(columns));
		panel.add(requestsGrid);

		mainPanel.add(panel);
		mainPanel.layout();
	}

	@Override
	public void showStatusRequests(List<StatusRequest> requests, String locale) {
		statusRequestsStore.removeAll();
		for (StatusRequest request : requests) {
			request.setTransient(DATE_TEXT, DateFormat.formatDate(request.getDate()));
			request.setTransient(USER_TEXT, request.getUser().getFullName());
			request.setTransient(STATE_TEXT, StatusRequestState.values.get(request.getState()).get(locale));
			if (request.getSource() != null) {
				Optional<Source> source = Enums.getIfPresent(Source.class, request.getSource());
				request.setTransient(SOURCE_TEXT, source.isPresent() ? source.get().getName() : "");
			}
			statusRequestsStore.add(new BeanModel<StatusRequest>(request));
		}
	}

	@Override
	public void openRequest(final StatusRequest request) {
		requestWindow = new Window();
		requestWindow.setLayout(new FitLayout());
		requestWindow.setModal(true);
		requestWindow.setSize(500, 400);

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLayout(new FitLayout());

		String text = bundle.request().getText();
		User user = request.getUser();
		text = text.replace("#{user}", user.getFullName());
		text = text.replace("#{userName}", user.getUserName());
		if (request.getRequest() != null)
			text = text.replace("#{request}", StringUtil.nlToBr(request.getRequest()));
		else
			text = text.replace("#{request}", "");
		Html html = new Html(text);
		html.setStyleAttribute("margin", "5px");
		formPanel.add(html);

		if (request.getState() == StatusRequestState.NEW) {
			formPanel.addButton(new Button(constants.sendCodeButton(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(new ShowSendFormMessage(request));
				}
			}));
			formPanel.addButton(new Button(constants.rejectRequestButton(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(new ShowRejectFormMessage(request));
				}
			}));
		}
		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				requestWindow.hide();
			}
		}));

		requestWindow.add(formPanel);

		requestWindow.show();
	}

	@Override
	public void showSendForm(final StatusRequest request) {
		sendWindow = new Window();
		sendWindow.setModal(true);
		sendWindow.setSize(400, 200);
		sendWindow.setOnEsc(false);
		sendWindow.setLayout(new FitLayout());

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelAlign(LabelAlign.TOP);

		final EmailField emailField = new EmailField();
		emailField.setFieldLabel(constants.emailField());
		emailField.setAllowBlank(false);
		formPanel.add(emailField);

		Button sendButton = new Button(constants.sendButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(new SendStatusCodeMessage(request.getId(), emailField.getValue()));
			}
		});
		formPanel.addButton(sendButton);

		new FormButtonBinding(formPanel).addButton(sendButton);

		sendWindow.add(formPanel);

		sendWindow.show();
	}

	@Override
	public void showRejectForm(final StatusRequest request) {
		final Window window = new Window();
		window.setLayout(new FitLayout());
		window.setModal(true);
		window.setSize(400, 300);

		FormPanel formPanel = new FormPanel();
		formPanel.setLabelAlign(LabelAlign.TOP);
		formPanel.setHeaderVisible(false);

		final TextArea reasonField = new TextArea();
		reasonField.setFieldLabel(constants.reasonField());
		formPanel.add(reasonField, new FormData(300, 200));

		formPanel.addButton(new Button(constants.rejectButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(new RejectRequestMessage(request.getId(), reasonField.getValue()));

				window.hide();
				requestWindow.hide();
			}
		}));
		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				window.hide();
			}
		}));

		window.add(formPanel);

		window.show();
	}

	@Override
	public void hideSendDialogs() {
		sendWindow.hide();
		requestWindow.hide();
	}

	@Override
	public void informBadCode() {
		MessageBox.alert(appConstants.error(), constants.badPrefix(), null);
	}

    @Override
    public void showDeliverySection(List<DeliveryDiscount> deliveryDiscounts) {
        mainPanel.removeAll();

        ContentPanel panel = new ContentPanel(new FlowLayout());
        panel.setHeaderVisible(false);
        panel.setScrollMode(Style.Scroll.AUTOY);

        deliveryDiscountStoreMap = new HashMap<Integer, ListStore<DeliveryDiscountModel>>();
		deliveryDiscountGridMap = new HashMap<Integer, EditorGrid<DeliveryDiscountModel>>();

        for (final Integer deliveryType : DeliveryType.DELIVERY_DISCOUNT_TYPES) {
            FieldSet fieldSet = new FieldSet();
            fieldSet.setWidth(500);
            fieldSet.setHeading(DeliveryType.values.get(deliveryType));
            fieldSet.setCollapsible(true);

            ContentPanel gridPanel = new ContentPanel();
            gridPanel.setHeaderVisible(false);

            final ListStore<DeliveryDiscountModel> store = new ListStore<DeliveryDiscountModel>();
//            store.setDefaultSort(SUM, Style.SortDir.ASC);
            deliveryDiscountStoreMap.put(deliveryType, store);

            List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
            columns.add(initColumnConfig(SUM, constants.sumColumnName(), 200));
            columns.add(initColumnConfig(DISCOUNT_PC, constants.discountColumnName(), 200));

            final EditorGrid<DeliveryDiscountModel> grid = new EditorGrid<DeliveryDiscountModel>(store, new ColumnModel(columns));
			grid.setAutoExpandColumn(SUM);
            grid.setHeight(200);
            grid.addListener(Events.AfterEdit, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent baseEvent) {
                    grid.getStore().commitChanges();

                    DeliveryDiscountModel model = grid.getSelectionModel().getSelectedItem();
                    DeliveryDiscount deliveryDiscount = model.getEntity();
                    boolean valueHasChanged = deliveryDiscount.getSum() != model.getSum()
                        || deliveryDiscount.getDiscountPc() != model.getDiscountPc();
                    if (valueHasChanged) {
                        deliveryDiscount.setSum(model.getSum());
                        deliveryDiscount.setDiscountPc(model.getDiscountPc());
                        presenter.updateDeliveryDiscount(deliveryDiscount);
                    }
                }
            });
            deliveryDiscountGridMap.put(deliveryType, grid);

            ToolBar toolBar = new ToolBar();
            toolBar.add(new Button(appConstants.add(), new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent buttonEvent) {
					presenter.addDeliveryDiscount(new DeliveryDiscount(deliveryType, 0, 0));
					grid.stopEditing();
                }
            }));
            toolBar.add(new Button(appConstants.delete(), new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    DeliveryDiscountModel selectedItem = grid.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        store.remove(selectedItem);
                        presenter.deleteDeliveryDiscount(selectedItem.getEntity().getId());
                    } else {
                        MessageBox.alert(appConstants.error(), constants.noRowsToDelete(), null);
                    }
                }
            }));

            gridPanel.add(toolBar);
            gridPanel.add(grid);
            fieldSet.add(gridPanel);
            panel.add(fieldSet, new FlowData(5, 10, 5, 10));
        }

        mainPanel.add(panel);
        mainPanel.layout();

        // fill grids
        for (DeliveryDiscount deliveryDiscount : deliveryDiscounts) {
            ListStore<DeliveryDiscountModel> store = deliveryDiscountStoreMap.get(deliveryDiscount.getDeliveryType());
            store.add(new DeliveryDiscountModel(deliveryDiscount));
        }
//        for (ListStore<DeliveryDiscountModel> store : deliveryDiscountStoreMap.values()) {
//            store.sort(SUM, Style.SortDir.ASC);
//        }
    }

    private ColumnConfig initColumnConfig(final String id, String name, final int width) {
        ColumnConfig columnConfig = new ColumnConfig(id, name, width);

        IntegerField numberField = new IntegerField();
        numberField.setAllowDecimals(false);
        numberField.setAllowNegative(false);
        numberField.setMinValue(1);

        if (DISCOUNT_PC.equalsIgnoreCase(id)) {
            numberField.setMaxValue(100);
        }

        numberField.setAutoValidate(true);
        columnConfig.setEditor(new CellEditor(numberField));

        return columnConfig;
    }

    @Override
    public void addDeliveryDiscountToGrid(DeliveryDiscount deliveryDiscount) {
        ListStore<DeliveryDiscountModel> activeStore = deliveryDiscountStoreMap.get(deliveryDiscount.getDeliveryType());
		EditorGrid<DeliveryDiscountModel> activeGrid = deliveryDiscountGridMap.get(deliveryDiscount.getDeliveryType());

		DeliveryDiscountModel model = new DeliveryDiscountModel(deliveryDiscount);

		activeStore.insert(model, 0);
		activeGrid.startEditing(activeStore.indexOf(model), 0);
    }

    @Override
    public void alertDeliveryDiscountExists() {
        MessageBox.alert(appConstants.error(), constants.deliveryDiscountExists(), null);
    }

    @Override
    public void alertAddingDeliveryDiscountError() {
        MessageBox.alert(appConstants.error(), constants.addingDeliveryDiscountError(), null);
    }
}
