package ru.imagebook.client.admin.view.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.grid.LiveGridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LiveToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.ctl.order.AddOrderMessage;
import ru.imagebook.client.admin.ctl.order.ApplyFilterMessage;
import ru.imagebook.client.admin.ctl.order.CopyOrderMessage;
import ru.imagebook.client.admin.ctl.order.DeleteOrdersMessage;
import ru.imagebook.client.admin.ctl.order.DeleteOrdersRequestMessage;
import ru.imagebook.client.admin.ctl.order.GeneratePdfMessage;
import ru.imagebook.client.admin.ctl.order.LoadOrdersMessage;
import ru.imagebook.client.admin.ctl.order.LoadUsersMessage;
import ru.imagebook.client.admin.ctl.order.NotifyNewOrdersMessage;
import ru.imagebook.client.admin.ctl.order.NotifyOrdersAcceptedMessage;
import ru.imagebook.client.admin.ctl.order.OrderMessages;
import ru.imagebook.client.admin.ctl.order.OrderView;
import ru.imagebook.client.admin.ctl.order.ProductSelectedMessage;
import ru.imagebook.client.admin.ctl.order.PublishWebFlashMessage;
import ru.imagebook.client.admin.ctl.order.RegenerateOrderRequestMessage;
import ru.imagebook.client.admin.ctl.order.SearchOrderMessage;
import ru.imagebook.client.admin.ctl.order.ShowEditFormMessage;
import ru.imagebook.client.admin.ctl.order.UpdateOrderMessage;
import ru.imagebook.client.admin.ctl.order.VendorUpdateOrderMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.admin.view.product.Format;
import ru.imagebook.client.admin.view.user.AddressFieldSet;
import ru.imagebook.client.admin.view.user.UserConstants;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.common.service.admin.Actions;
import ru.imagebook.client.common.service.order.OrderService;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.client.common.view.order.OrderViewer;
import ru.imagebook.shared.model.*;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.format.Formatter;
import ru.minogin.core.client.gwt.DateFormat;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.SearchField;
import ru.minogin.core.client.gxt.SearchHandler;
import ru.minogin.core.client.gxt.form.DateTimeField;
import ru.minogin.core.client.gxt.form.MultiSelectField;
import ru.minogin.core.client.gxt.form.ObjectField;
import ru.minogin.core.client.gxt.form.ObjectFieldCallback;
import ru.minogin.core.client.gxt.form.ObjectModel;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.SelectValue;
import ru.minogin.core.client.gxt.form.XTextArea;
import ru.minogin.core.client.gxt.grid.FixedLiveGridView;
import ru.minogin.core.client.gxt.grid.IntegerColumnConfig;
import ru.minogin.core.client.i18n.MultiString;
import ru.minogin.core.client.i18n.locale.Locales;

@Singleton
public class OrderViewImpl extends View implements OrderView {
	private static final String ARTICLE = "article";
	private static final String USER_NAME_TEXT = "userNameText";
	private static final String USER_TEXT = "userText";
	private static final String PRODUCT_TEXT = "productText";
	private static final String STATE_TEXT = "stateText";
	private static final String DATE_TEXT = "dateText";
	private static final String PRICE = "price";
	private static final String COST = "cost";
	private static final String VENDOR_COST = "vendorCost";
	private static final String BONUS_CODE_CONTENT = "bonusCodeContent";
	private static final String REQUEST_NUMBER = "requestNumber";
	private static final String BILL_ID = "billId";
	private static final String LEVEL_TEXT = "levelText";
	private static final String TYPE_TEXT = "typeText";
	private static final String DELIVERY_TYPE_TEXT = "deliveryTypeText";
	private static final String VENDOR_TEXT = "agentText";
	private static final String DISCOUNT = "discount";

	private final Widgets widgets;
	private final OrderConstants constants;
	private final OrderViewer orderViewer;
	private final CommonConstants appConstants;
	private final Format format;
	private final CoreFactory coreFactory;
	private final OrderService orderService;
	private final UserConstants userConstants;
	private final SecurityService securityService;
	private final UserService userService;

	protected AsyncCallback<PagingLoadResult<BeanModel<Order<?>>>> callback;
	private BasePagingLoader<PagingLoadResult<BeanModel<Order<?>>>> loader;
	private Grid<BeanModel<Order<?>>> grid;
	private SelectField<Integer> typeField;
	private SelectField<Product> productField;
	private Window window;
	private CheckBox trialField;
	private TextField<String> numberField;
	protected ObjectFieldCallback<User> userCallback;
	private ObjectField<User> userField;
	private SelectField<Integer> stateField;
	private LabelField rejectCommentLabel;
	private TextArea rejectCommentField;
	private DateTimeField dateField;
	private NumberField quantityField;
	private TextField<String> flashField;
	private SelectField<Integer> pageCountField;
	protected ObjectFieldCallback<Color> colorCallback;
	private SelectField<Integer> coverLaminationField;
	private SelectField<Integer> pageLaminationField;
	private SelectField<Color> colorField;
	private AddressFieldSet addressFieldSet;
	private LabelField itemWeightField;
	private LabelField totalWeightField;
	private Status filterStatus;
	private ToolBar filterToolBar;
	private ContentPanel panel;
	private Button addSaveButton;
	private Button editSaveButton;
	private FormButtonBinding addFormBinding;
	private TextField<String> deliveryCodeField;
	private TextArea commentField;
	private XTextArea deliveryCommentField;
	private CheckBox urgentField;

	private OrderPresenter presenter;
	private SelectField<Integer> deliveryTypeField;
	private SelectField<Flyleaf> flyleafField;
	private SelectField<Vellum> vellumField;

	@Inject
	public OrderViewImpl(Dispatcher dispatcher, Widgets widgets, OrderConstants constants, CommonConstants appConstants,
                         Format format, CoreFactory coreFactory, OrderService orderService, UserConstants userConstants,
                         OrderViewer orderViewer, SecurityService securityService, UserService userService) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
		this.format = format;
		this.coreFactory = coreFactory;
		this.orderService = orderService;
		this.userConstants = userConstants;
		this.orderViewer = orderViewer;
		this.securityService = securityService;
		this.userService = userService;
	}

	@Override
	public void setPresenter(OrderPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showOrdersSection(boolean colorize, boolean canDelete, final String sessionId) {
		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
		desktop.removeAll();

		panel = new ContentPanel(new RowLayout());
		panel.setHeading(constants.ordersHeading());

		ToolBar toolBar = new ToolBar();

		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)
				|| securityService.isAllowed(Actions.ORDERS_VENDOR_OPEN)) {
			Button ordersButton = new Button(constants.orders());

			Menu menu = new Menu();
			if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
				menu.add(new MenuItem(appConstants.add(), new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						send(OrderMessages.SHOW_ADD_FORM);
					}
				}));
			}
			if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
				menu.add(new MenuItem(appConstants.edit(), new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						edit();
					}
				}));
			}
			else {
				menu.add(new MenuItem(appConstants.open(), new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						edit();
					}
				}));
			}
			if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
				menu.add(new MenuItem(constants.copy(), new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
						if (item != null)
							send(new CopyOrderMessage(item.getBean()));
					}
				}));
				if (canDelete) {
					menu.add(new MenuItem(appConstants.delete(), new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							List<BeanModel<Order<?>>> selectedItems = grid.getSelectionModel().getSelectedItems();
							List<Order<?>> orders = new ArrayList<Order<?>>();
							for (BeanModel<Order<?>> item : selectedItems) {
								orders.add(item.getBean());
							}
							send(new DeleteOrdersRequestMessage(orders));
						}
					}));
				}
			}
			ordersButton.setMenu(menu);
			toolBar.add(ordersButton);

			if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
				toolBar.add(new Button(constants.filterButton(), new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(OrderMessages.SHOW_FILTER);
					}
				}));

				Button messagesButton = new Button(constants.messagesButton());
				menu = new Menu();
				menu.add(new MenuItem(constants.newOrderMessageButton(),
						new SelectionListener<MenuEvent>() {
							@Override
							public void componentSelected(MenuEvent ce) {
								List<BeanModel<Order<?>>> selectedItems = grid.getSelectionModel()
										.getSelectedItems();
								if (!selectedItems.isEmpty()) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<Order<?>> item : selectedItems) {
										ids.add(item.getBean().getId());
									}
									send(new NotifyNewOrdersMessage(ids));
								}
							}
						}));
				menu.add(new MenuItem(constants.orderAcceptedMessageButton(),
						new SelectionListener<MenuEvent>() {
							@Override
							public void componentSelected(MenuEvent ce) {
								List<BeanModel<Order<?>>> selectedItems = grid.getSelectionModel()
										.getSelectedItems();
								if (!selectedItems.isEmpty()) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<Order<?>> item : selectedItems) {
										ids.add(item.getBean().getId());
									}
									send(new NotifyOrdersAcceptedMessage(ids));
								}
							}
						}));
				messagesButton.setMenu(menu);
				toolBar.add(messagesButton);

				toolBar.add(new Button(constants.exportButton(), new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.exportButtonClicked();
					}
				}));

				toolBar.add(new Button(constants.showFlashButton(), new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
                        if (item != null) {
                            Order<?> order = item.getBean();
                            orderViewer.showOrderPreview(order, sessionId);
                        }
					}
				}));

                if (securityService.hasRole(Role.OPERATOR)) {
                    toolBar.add(new Button(constants.publishOrderButton(), new SelectionListener<ButtonEvent>() {
                        @Override
                        public void componentSelected(ButtonEvent buttonEvent) {
                            final BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
                            if (item != null) {
                                Order<?> order = item.getBean();
                                presenter.onPublishOrderButtonClicked(order.getId());
                            }
                        }
                    }));
                }

				toolBar.add(new Button(constants.regenerateButton(), new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
						if (item != null) {
							new ConfirmMessageBox(appConstants.warning(), constants.confirmRegenerate(),
									new Listener<BaseEvent>() {
										@Override
										public void handleEvent(BaseEvent be) {
											Order<?> order = item.getBean();
											send(new RegenerateOrderRequestMessage(order.getId()));
										}
									});
						}
					}
				}));

				Button testButton = new Button(constants.testButton());
				menu = new Menu();
				menu.add(new MenuItem(constants.generatePdfButton(), new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						final BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
						if (item != null) {
							new ConfirmMessageBox(appConstants.warning(), constants.confirmGeneratePdf(),
									new Listener<BaseEvent>() {
										@Override
										public void handleEvent(BaseEvent be) {
											Order<?> order = item.getBean();
											send(new GeneratePdfMessage(order.getId()));
										}
									});
						}
					}
				}));
				testButton.setMenu(menu);
				toolBar.add(testButton);

				Button siteButton = new Button(constants.siteButton());
				menu = new Menu();
				menu.add(new MenuItem(constants.publishWebFlashButton(),
						new SelectionListener<MenuEvent>() {
							@Override
							public void componentSelected(MenuEvent ce) {
								final BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
								if (item != null) {
									new ConfirmMessageBox(appConstants.warning(), constants.confirmPublishWebFlash(),
											new Listener<BaseEvent>() {
												@Override
												public void handleEvent(BaseEvent be) {
													Order<?> order = item.getBean();
													send(new PublishWebFlashMessage(order.getId()));
												}
											});
								}
							}
						}));
				//Deprecated
//				menu.add(new MenuItem(constants.publishSmallWebFlashButton(),
//						new SelectionListener<MenuEvent>() {
//							@Override
//							public void componentSelected(MenuEvent ce) {
//								final BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
//								if (item != null) {
//									new ConfirmMessageBox(appConstants.warning(), constants.confirmPublishWebFlash(),
//											new Listener<BaseEvent>() {
//												@Override
//												public void handleEvent(BaseEvent be) {
//													Order<?> order = item.getBean();
//													send(new PublishWebFlashMessage(order.getId(), true));
//												}
//											});
//								}
//							}
//						}));
				siteButton.setMenu(menu);
				toolBar.add(siteButton);

				// toolBar.add(new Button(constants.generateFlashButton(), new
				// SelectionListener<ButtonEvent>() {
				// @Override
				// public void componentSelected(ButtonEvent ce) {
				// final BeanModel<Order<?>> item =
				// grid.getSelectionModel().getSelectedItem();
				// if (item != null) {
				// new ConfirmMessageBox(appConstants.warning(),
// constants.confirmFlash(),
				// new Listener<BaseEvent>() {
				// @Override
				// public void handleEvent(BaseEvent be) {
				// Order<?> order = item.getBean();
				// send(new GenerateFlashMessage(order.getId()));
				// }
				// });
				// }
				// }
				// }));
				//
				// toolBar.add(new Button(constants.generatePdfButton(), new
				// SelectionListener<ButtonEvent>() {
				// @Override
				// public void componentSelected(ButtonEvent ce) {
				// final BeanModel<Order<?>> item =
				// grid.getSelectionModel().getSelectedItem();
				// if (item != null) {
				// new ConfirmMessageBox(appConstants.warning(), constants.confirmPdf(),
				// new Listener<BaseEvent>() {
				// @Override
				// public void handleEvent(BaseEvent be) {
				// Order<?> order = item.getBean();
				// send(new GeneratePdfMessage(order.getId()));
				// }
				// });
				// }
				// }
				// }));
			}
		}

		toolBar.add(new FillToolItem());

		final SearchField searchField = new SearchField(new SearchHandler() {
			@Override
			public void onSearch(String query) {
				send(new SearchOrderMessage(query));
			}
		});
		toolBar.add(searchField);

		panel.setTopComponent(toolBar);

		filterToolBar = new ToolBar();
		filterToolBar.hide();
		filterToolBar.addStyleName("filter-status");
		filterStatus = new Status();
		filterToolBar.add(filterStatus);
		panel.add(filterToolBar, new RowData(1, -1));

		DataProxy<PagingLoadResult<BeanModel<Order<?>>>> proxy = new DataProxy<PagingLoadResult<BeanModel<Order<?>>>>() {
			@Override
			public void load(DataReader<PagingLoadResult<BeanModel<Order<?>>>> reader, Object loadConfig,
					AsyncCallback<PagingLoadResult<BeanModel<Order<?>>>> callback) {
				PagingLoadConfig config = (PagingLoadConfig) loadConfig;
				OrderViewImpl.this.callback = callback;
				send(new LoadOrdersMessage(config.getOffset(), config.getLimit()));
			}
		};
		loader = new BasePagingLoader<PagingLoadResult<BeanModel<Order<?>>>>(proxy);
		ListStore<BeanModel<Order<?>>> store = new ListStore<BeanModel<Order<?>>>(loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new IntegerColumnConfig(Order.ID, constants.id(), 40));
		columns.add(new ColumnConfig(Order.NUMBER, constants.number(), 80));
		columns.add(new ColumnConfig(ARTICLE, constants.article(), 120));
		columns.add(new ColumnConfig(USER_TEXT, constants.user(), 150));
		columns.add(new ColumnConfig(USER_NAME_TEXT, constants.userName(), 150));
		columns.add(new ColumnConfig(PRODUCT_TEXT, constants.product(), 200));
		columns.add(new ColumnConfig(STATE_TEXT, constants.state(), 170));
		columns.add(new ColumnConfig(DATE_TEXT, constants.date(), 100));
		columns.add(new ColumnConfig(BONUS_CODE_CONTENT, constants.bonusCode(), 150));

		ColumnConfig config;
		config = new ColumnConfig(Order.QUANTITY, constants.quantity(), 80);
		config.setAlignment(HorizontalAlignment.RIGHT);
		columns.add(config);

		config = new ColumnConfig(PRICE, constants.price(), 50);
		config.setAlignment(HorizontalAlignment.RIGHT);
		columns.add(config);

		config = new ColumnConfig(COST, constants.cost(), 70);
		config.setAlignment(HorizontalAlignment.RIGHT);
		columns.add(config);

		config = new ColumnConfig(DISCOUNT, constants.discount(), 50);
		config.setAlignment(HorizontalAlignment.RIGHT);
		columns.add(config);

		if (securityService.isAllowed(Actions.ORDERS_VIEW_PRINTING_PRICE)) {
			config = new ColumnConfig(Order.PH_PRICE, constants.phPrice(), 80);
			config.setAlignment(HorizontalAlignment.RIGHT);
			columns.add(config);

			config = new ColumnConfig(Order.PH_COST, constants.phCost(), 120);
			config.setAlignment(HorizontalAlignment.RIGHT);
			columns.add(config);
			
			config = new ColumnConfig(VENDOR_COST, constants.vendorCost(), 120);
			config.setAlignment(HorizontalAlignment.RIGHT);
			columns.add(config);
		}

		columns.add(new IntegerColumnConfig(Order.ITEM_WEIGHT, constants.itemWeight(), 150));
		columns.add(new IntegerColumnConfig(Order.TOTAL_WEIGHT, constants.totalWeight(), 150));

		config = new ColumnConfig(BILL_ID, constants.billColumn(), 80);
		config.setAlignment(HorizontalAlignment.RIGHT);
		columns.add(config);

		config = new ColumnConfig(REQUEST_NUMBER, constants.requestColumn(), 80);
		config.setAlignment(HorizontalAlignment.RIGHT);
		columns.add(config);

		columns.add(new ColumnConfig(Order.DELIVERY_CODE, constants.deliveryCodeColumn(), 100));

		config = new ColumnConfig(LEVEL_TEXT, constants.levelColumn(), 100);
		config.setAlignment(HorizontalAlignment.RIGHT);
		columns.add(config);

		columns.add(new ColumnConfig(Order.COMMENT, constants.commentColumn(), 100));
		columns.add(new ColumnConfig(TYPE_TEXT, constants.typeColumn(), 100));
		columns.add(new ColumnConfig(DELIVERY_TYPE_TEXT, constants.deliveryTypeColumn(), 150));
		columns.add(new ColumnConfig(VENDOR_TEXT, constants.vendorColumn(), 150));

		ColumnConfig columnConfig = new ColumnConfig(Order.STORAGE_STATE,
				constants.storageStateColumn(), 150);
		columnConfig.setRenderer(new GridCellRenderer<BeanModel<Order<?>>>() {
			@Override
			public Object render(BeanModel<Order<?>> model, String property, ColumnData config,
					int rowIndex, int colIndex,
					ListStore<BeanModel<Order<?>>> store, Grid<BeanModel<Order<?>>> grid) {
				int storageState = (Integer) model.get(property);
				return StorageState.values.get(storageState).get(Locales.RU);
			}
		});
		columns.add(columnConfig);

		grid = new Grid<BeanModel<Order<?>>>(store, new ColumnModel(columns));
		LiveGridView liveView = new FixedLiveGridView();
		liveView.setEmptyText(constants.emptyGrid());
		grid.setView(liveView);
		grid.getView().setSortingEnabled(false);
		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
			grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
				@Override
				public void handleEvent(BaseEvent be) {
					edit();
				}
			});
		}
		if (colorize) {
			grid.getView().setViewConfig(new GridViewConfig() {
				@Override
				public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds) {
					User user = model.get(Order.USER);
					Vendor vendor = user.getVendor();
					if (vendor.getType() == VendorType.IMAGEBOOK) {
						return "";
					}
					else {
						String color = vendor.getColor();
						return "color-" + color;
					}
				}
			});
		}
		panel.add(grid, new RowData(1, 1));

		ToolBar gridToolBar = new ToolBar();
		LiveToolItem item = new LiveToolItem();
		item.bindGrid(grid);
		gridToolBar.add(item);
		panel.setBottomComponent(gridToolBar);

		desktop.add(panel);

		desktop.layout();
	}

	@Override
	public void showOrders(List<Order<?>> orders, int offset, int total, String locale,
			OrderFilter filter) {
		List<BeanModel<Order<?>>> rows = new ArrayList<BeanModel<Order<?>>>();
		for (Order<?> order : orders) {
			order.setTransient(ARTICLE, orderService.getOrderArticle(order));
			User user = order.getUser();
			order.setTransient(USER_TEXT, user.getFullName());
			order.setTransient(USER_NAME_TEXT, user.getUserName());
			order.setTransient(PRODUCT_TEXT, order.getProduct().getName().getNonEmptyValue(locale));
			order.setTransient(STATE_TEXT,
					OrderState.values.get(order.getState()).getNonEmptyValue(locale));
			order.setTransient(DATE_TEXT, DateFormat.formatDateTime(order.getDate()));
			order.setTransient(PRICE, order.getPrice());
			order.setTransient(COST, order.getCost());
			order.setTransient(DISCOUNT, order.getDiscount());

			BonusCode code = order.getBonusCode();
			if (code != null) {
				if (order.getDeactivationCode() != null)
					order.setTransient(
							BONUS_CODE_CONTENT,
							code.getAction().getName() + " - " + order.getBonusCodeText() + " - "
									+ order.getDeactivationCode());
				else if (order.getBonusCodeText() != null)
					order.setTransient(BONUS_CODE_CONTENT,
							code.getAction().getName() + " - " + order.getBonusCodeText());
				else
					order.setTransient(BONUS_CODE_CONTENT, code.getName());
			}

			Request request = order.getRequest();
			if (request != null)
				order.setTransient(REQUEST_NUMBER, DateFormat.formatDate(request.getDate()));

			Bill bill = order.getBill();
			if (bill != null)
				order.setTransient(BILL_ID, bill.getId());

			int level = user.getLevel();
			if (order.getLevel() != null && order.getLevel() > level)
				level = order.getLevel();
			order.setTransient(LEVEL_TEXT, level);

			order.setTransient(TYPE_TEXT, OrderType.values.get(order.getType()).get(locale));

			Integer deliveryType = order.getDeliveryType();
			if (deliveryType != null)
				order.setTransient(DELIVERY_TYPE_TEXT, DeliveryType.values.get(deliveryType));

			Vendor vendor = user.getVendor();
			if (vendor != null) {
				order.setTransient(VENDOR_TEXT, vendor.getName());
			}
			
			order.setTransient(VENDOR_COST, order.getVendorCost());
			
			rows.add(new BeanModel<Order<?>>(order));
		}
		PagingLoadResult<BeanModel<Order<?>>> result = new BasePagingLoadResult<BeanModel<Order<?>>>(
				rows, offset, total);
		callback.onSuccess(result);

		if (!filter.getStates().isEmpty() || (filter.getBonusCode() != null && !"".equals(filter.getBonusCode().trim()))) {
			filterStatus.setText(constants.filterStatus());
			filterToolBar.show();
			panel.layout(true);
		}
		else {
			filterToolBar.hide();
			panel.layout(true);
		}
	}

	@Override
	public void showProductSelectForm(final Map<Integer, List<Product>> products, final String locale) {
		window = new Window();
		window.setHeading(constants.addWindowHeading());
		window.setLayout(new FitLayout());
		window.setSize(900, 600);
		window.setModal(true);
		window.setOnEsc(false);

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);

		typeField = new SelectField<Integer>();
		typeField.setFieldLabel(constants.type() + " *");
		typeField.setAllowBlank(false);
		for (int type : ProductType.values.keySet()) {
			typeField.add(type, format.formatType(type, locale));
		}
		typeField.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Integer>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SelectValue<Integer>> se) {
				productField.clear();
				productField.getStore().removeAll();

				Formatter formatter = coreFactory.createFormatter();
				Integer type = typeField.getXValue();
				List<Product> typeProducts = products.get(type);
				if (typeProducts != null) {
					for (Product product : typeProducts) {
						MultiString name = product.getName();
						String text = formatter.n2(product.getNumber()) + " - " + name.getNonEmptyValue(locale);
						productField.add(product, text);
					}
				}
			}
		});
		formPanel.add(typeField, new FormData(300, -1));

		productField = new SelectField<Product>();
		productField.setFieldLabel(constants.product() + " *");
		productField.setAllowBlank(false);
		formPanel.add(productField, new FormData(300, -1));

		Button nextButton = new Button(appConstants.next(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(new ProductSelectedMessage(productField.getXValue()));
			}
		});
		formPanel.addButton(nextButton);

		FormButtonBinding binding = new FormButtonBinding(formPanel);
		binding.addButton(nextButton);

		window.add(formPanel);

		window.show();
	}

	@Override
	public void showAddForm(final Order<?> order, String locale, List<Color> colors,
			List<BonusAction> actions, List<Flyleaf> flyleafs, List<Vellum> vellums) {
		if (window != null && window.isVisible())
			window.removeAll();
		else {
			window = new Window();
			window.setHeading(constants.addWindowHeading());
			window.setLayout(new FitLayout());
			window.setSize(900, 600);
			window.setModal(true);
			window.setOnEsc(false);
			window.show();
		}

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(200);
		formPanel.setScrollMode(Scroll.AUTO);
		addFields(true, formPanel, order, locale, colors, actions, userService.getUser(), flyleafs, vellums);

		addSaveButton = new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				addFormBinding.stopMonitoring();
				addSaveButton.disable();
				fetch(order);
				send(new AddOrderMessage(order));
			}
		});
		formPanel.addButton(addSaveButton);

		addFormBinding = new FormButtonBinding(formPanel);
		addFormBinding.addButton(addSaveButton);

		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				window.hide();
			}
		}));

		window.add(formPanel);
		window.layout();
	}

	@Override
	public void showEditForm(final Order<?> order, String locale, List<Color> colors,
			List<BonusAction> actions, List<Flyleaf> flyleafs, List<Vellum> vellums) {
		final User user = userService.getUser();

		window = new Window();
		window.setHeading(constants.editWindowHeading());
		window.setLayout(new FitLayout());
		window.setSize(900, 600);
		window.setModal(true);
		window.setOnEsc(false);

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(200);
		formPanel.setScrollMode(Scroll.AUTO);
		addFields(false, formPanel, order, locale, colors, actions, user, flyleafs, vellums);

		final Vendor vendor = user.getVendor();
		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT) || vendor.isPrinter()) {
			editSaveButton = new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					editSaveButton.disable();
					fetch(order);
					if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
						send(new UpdateOrderMessage(order));
					}
					else {
						send(new VendorUpdateOrderMessage(order.getId(), order.getState()));
					}
				}
			});
			formPanel.addButton(editSaveButton);
			FormButtonBinding binding = new FormButtonBinding(formPanel);
			binding.addButton(editSaveButton);
		}

		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				window.hide();
			}
		}));

		window.add(formPanel);
		window.show();
	}

	private boolean isValid(Integer state) {
		return state == OrderState.PRINTING || state == OrderState.FINISHING
				|| state == OrderState.PRINTED
				|| state == OrderState.DELIVERY || state == OrderState.SENT;
	}

	private void addFields(final boolean isCreateForm, final FormPanel formPanel, Order<?> order,
			final String locale,
			List<Color> colors, List<BonusAction> actions, User user, List<Flyleaf> flyleafs, List<Vellum> vellums) {
		final boolean readOnly = !securityService.isAllowed(Actions.ORDERS_MANAGEMENT);
		final Formatter formatter = coreFactory.createFormatter();

		LabelField productLabelField = new LabelField();
		productLabelField.setFieldLabel(constants.product() + ":");
		Product product = order.getProduct();
		String text = format.formatType(product.getType(), locale) + "<br/>";
		text += formatter.n2(product.getNumber()) + " - " + product.getName().getNonEmptyValue(locale);
		productLabelField.setValue(text);
		productLabelField.setReadOnly(readOnly);
		formPanel.add(productLabelField, new FormData(300, -1));

		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
			if (product.isTrialAlbum()) {
				trialField = new CheckBox();
				trialField.setFieldLabel(constants.trialField());
				trialField.setBoxLabel("");
				if (isCreateForm)
					trialField.setValue(true);
				else
					trialField.setValue(order.isTrial());
				formPanel.add(trialField);
			}
		}

		numberField = new TextField<String>();
		numberField.setFieldLabel(constants.number() + " *");
		numberField.setAllowBlank(false);
		numberField.setValue(order.getNumber());
		numberField.setReadOnly(readOnly);
		formPanel.add(numberField);

		userField = new ObjectField<User>() {
			@Override
			protected String render(User user) {
				if (user == null)
					return null;
				return user.getUserName() + " - " + user.getFullName();
			}

			@Override
			protected void load(int offset, int limit, String query, ObjectFieldCallback<User> callback) {
				userCallback = callback;
				send(new LoadUsersMessage(offset, limit, query));
			}
		};
		userField.setFieldLabel(constants.user() + " *");
		userField.setXValue(order.getUser());
		userField.setAllowBlank(false);
		userField.setReadOnly(readOnly);
		formPanel.add(userField, new FormData(400, -1));

		userField.addSelectionChangedListener(new SelectionChangedListener<ObjectModel<User>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<ObjectModel<User>> se) {
				boolean urgentFlag = userField.getXValue() != null
						&& userField.getXValue().isUrgentOrders();
				urgentField.setValue(urgentFlag);
			}
		});

		urgentField = new CheckBox();
		urgentField.setFieldLabel(constants.urgentField());
		urgentField.setBoxLabel("");
		urgentField.setValue(order.isUrgent());
		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)
				&& user.getVendor().getType() == VendorType.IMAGEBOOK) {
			formPanel.add(urgentField);
		}

		boolean stateReadOnly = false;
		stateField = new SelectField<Integer>();
		Set<Integer> keySet = OrderState.values.keySet();
		if (!securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
			if (!isValid(order.getState())) {
				stateReadOnly = true;
			}
			else if (user.getVendor().isPrinter()) {
				stateReadOnly = false;
				keySet = new TreeSet<Integer>();
				keySet.add(OrderState.PRINTING);
				keySet.add(OrderState.FINISHING);
				keySet.add(OrderState.PRINTED);
				keySet.add(OrderState.DELIVERY);
				keySet.add(OrderState.SENT);
			}
			else {
				stateReadOnly = true;
			}
		}
		for (int state : keySet) {
			stateField.add(state, OrderState.values.get(state).getNonEmptyValue(locale));
		}
		stateField.setFieldLabel(constants.state());
		stateField.setAllowBlank(false);
		stateField.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Integer>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SelectValue<Integer>> se) {
				Integer state = stateField.getXValue();
				if (state == OrderState.REJECTED) {
					rejectCommentLabel.show();
					rejectCommentField.show();
					rejectCommentField.setAllowBlank(false);
				}
				else {
					rejectCommentLabel.hide();
					rejectCommentField.hide();
					rejectCommentField.setAllowBlank(true);
				}
			}
		});
		stateField.setReadOnly(stateReadOnly);

		formPanel.add(stateField);

		rejectCommentLabel = new LabelField();
		rejectCommentLabel.setFieldLabel(constants.rejectComment() + ":");
		formPanel.add(rejectCommentLabel);

		rejectCommentField = new TextArea();
		rejectCommentField.setHideLabel(true);
		rejectCommentField.setValue(order.getRejectComment());
		formPanel.add(rejectCommentField, new FormData(400, 100));
		stateField.setXValue(order.getState());

		dateField = new DateTimeField();
		dateField.setFieldLabel(constants.date());
		dateField.setValue(order.getDate());
		dateField.setReadOnly(readOnly);
		formPanel.add(dateField);

		quantityField = new NumberField();
		quantityField.setFieldLabel(constants.quantity());
		quantityField.setPropertyEditorType(Integer.class);
		quantityField.setValue(order.getQuantity());
		quantityField.setReadOnly(readOnly);
		formPanel.add(quantityField);

		flashField = new TextField<String>();
		flashField.setFieldLabel(constants.flash());
		flashField.setValue(order.getFlash());
		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
			formPanel.add(flashField);
		}

		pageCountField = new SelectField<Integer>();
		pageCountField.setFieldLabel(constants.pageCount() + " *");
		pageCountField.setAllowBlank(false);
		for (int i = product.getMinPageCount(); i <= product.getMaxPageCount(); i += product
				.getMultiplicity()) {
			pageCountField.add(i, i + "");
		}
		pageCountField.setReadOnly(readOnly);
		pageCountField.setXValue(order.getPageCount());
		formPanel.add(pageCountField);

		colorField = new SelectField<Color>();
		List<Integer> colorRange = product.getColorRange();
		for (Color color : colors) {
			if (!colorRange.contains(color.getNumber()))
				continue;

			String textValue = formatter.n2(color.getNumber()) + " - "
					+ color.getName().getNonEmptyValue(locale);
			colorField.add(color, textValue);
		}
		colorField.setFieldLabel(constants.color() + " *");
		colorField.setAllowBlank(false);
		colorField.setXValue(order.getColor());
		colorField.setReadOnly(readOnly);
		formPanel.add(colorField, new FormData(300, -1));

		Format format = new Format(coreFactory);

		coverLaminationField = new SelectField<Integer>();
		coverLaminationField.setFieldLabel(constants.coverLamination() + " *");
		coverLaminationField.setAllowBlank(false);
		List<Integer> coverLamRange = product.getCoverLamRange();
		for (int lam : CoverLamination.values.keySet()) {
			if (!coverLamRange.contains(lam))
				continue;

			coverLaminationField.add(lam, format.formatCoverLamination(lam, locale));
		}
		if (order.getCoverLamination() != null)
			coverLaminationField.setXValue(order.getCoverLamination());
		else
			coverLaminationField.selectIfOne();
		coverLaminationField.setReadOnly(readOnly);
		formPanel.add(coverLaminationField);

		pageLaminationField = new SelectField<Integer>();
		pageLaminationField.setFieldLabel(constants.pageLamination() + " *");
		pageLaminationField.setAllowBlank(false);
		List<Integer> pageLamRange = product.getPageLamRange();
		for (int lam : PageLamination.values.keySet()) {
			if (!pageLamRange.contains(lam))
				continue;

			pageLaminationField.add(lam, format.formatPageLamination(lam, locale));
		}
		if (order.getPageLamination() != null)
			pageLaminationField.setXValue(order.getPageLamination());
		else
			pageLaminationField.selectIfOne();
		pageLaminationField.setReadOnly(readOnly);
		formPanel.add(pageLaminationField);

		Album album = (Album) order.getProduct();
		if (album.isFlyleafs()) {
			flyleafField = new SelectField<Flyleaf>();
			for (Flyleaf flyleaf: flyleafs) {
				flyleafField.add(flyleaf, flyleaf.getInnerName());
			}
			flyleafField.setFieldLabel("Цветной форзац");
			flyleafField.setXValue(order.getFlyleaf());
			formPanel.add(flyleafField);
		}

		if (album.isSupportsVellum()) {
			vellumField = new SelectField<Vellum>();
            vellumField.add(null, "Нет");
			for (Vellum vellum : vellums) {
				vellumField.add(vellum, vellum.getInnerName());
			}
			vellumField.setFieldLabel("Калька");
			vellumField.setXValue(order.getVellum());
			formPanel.add(vellumField);
		}

		LabelField bonusCodeField = new LabelField((String) order.get(BONUS_CODE_CONTENT));
		bonusCodeField.setFieldLabel(constants.bonusCode() + ":");
		bonusCodeField.setReadOnly(readOnly);
		formPanel.add(bonusCodeField);

		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
			itemWeightField = new LabelField();
			itemWeightField.setFieldLabel(constants.itemWeight() + ":");
			itemWeightField.setValue(order.getItemWeight());
			formPanel.add(itemWeightField);

			totalWeightField = new LabelField();
			totalWeightField.setFieldLabel(constants.totalWeight() + ":");
			totalWeightField.setValue(order.getTotalWeight());
			formPanel.add(totalWeightField);
		}

		deliveryTypeField = new SelectField<Integer>(constants.deliveryTypeField(), true, formPanel);
		deliveryTypeField.add(null, "-");
		for (int deliveryType : DeliveryType.values.keySet()) {
			deliveryTypeField.add(deliveryType, DeliveryType.values.get(deliveryType));
		}
		deliveryTypeField.setReadOnly(readOnly);
		deliveryTypeField.setXValue(order.getDeliveryType());

		deliveryCodeField = new TextField<String>();
		deliveryCodeField.setFieldLabel(constants.deliveryCodeField());
		deliveryCodeField.setValue(order.getDeliveryCode());
		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
			formPanel.add(deliveryCodeField);
		}

		commentField = new TextArea();
		commentField.setFieldLabel(constants.commentField());
		commentField.setValue(order.getComment());
		commentField.setReadOnly(readOnly);
		formPanel.add(commentField);

		deliveryCommentField = new XTextArea(constants.deliveryCommentField(), true,
				order.getDeliveryComment(), formPanel);
		if (!securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
			deliveryCommentField.hide();
		}

		addressFieldSet = new AddressFieldSet(userConstants);
		addressFieldSet.setCheckboxToggle(true);
		addressFieldSet.setValue(order.getAddress());
		formPanel.add(addressFieldSet);
		addressFieldSet.setExpanded(order.getAddress() != null);

		userField.addSelectionChangedListener(new SelectionChangedListener<ObjectModel<User>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<ObjectModel<User>> se) {
				User user = userField.getXValue();
				if (user != null) {
					List<Address> addresses = user.getAddresses();
					if (!addresses.isEmpty()) {
						Address address = addresses.get(0);
						addressFieldSet.setValue(address);
					}
				}
			}
		});
	}

	@Override
	public void showUsers(List<User> users, int offset, int total) {
		userCallback.onLoaded(users, offset, total);
	}

	private void fetch(Order<?> order) {
		Product product = order.getProduct();
		if (product.isTrialAlbum())
			order.setTrial(trialField.getValue());

		order.setUser(userField.getXValue());
		order.setUrgent(urgentField.getValue());
		order.setNumber(numberField.getValue());
		order.setState(stateField.getXValue());
		order.setDate(dateField.getValue());
		order.setQuantity((Integer) quantityField.getValue());
		order.setFlash(flashField.getValue());
		order.setRejectComment(rejectCommentField.getValue());
		order.setPageCount(pageCountField.getXValue());
		order.setColor(colorField.getXValue());

		if (flyleafField != null) {
			order.setFlyleaf(flyleafField.getXValue());
		}

		if (vellumField != null) {
			order.setVellum(vellumField.getXValue());
		}

		order.setCoverLamination(coverLaminationField.getXValue());
		order.setPageLamination(pageLaminationField.getXValue());
		order.setDeliveryCode(deliveryCodeField.getValue());
		order.setComment(commentField.getValue());
		order.setDeliveryComment(deliveryCommentField.getValue());
		order.setDeliveryType(deliveryTypeField.getXValue());
		// order.setBonusCode(bonusCodeFieldSet.getValue());
		if (addressFieldSet.isExpanded()) {
			Address address = addressFieldSet.fetch();
			if (order.getAddress() == null)
				order.setAddress(address);
		}
		else {
			order.setAddress(null);
		}
	}

	@Override
	public void hideAddForm() {
		window.hide();
	}

	@Override
	public void reload() {
		loader.load();
	}

	@Override
	public void alertNoOrdersToDelete() {
		MessageBox.alert(appConstants.error(), constants.noOrdersToDelete(), null);
	}

	@Override
	public void confirmDeleteOrders(final List<Order<?>> orders) {
		new ConfirmMessageBox(appConstants.warning(), constants.deleteOrdersConfirmation(),
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						send(new DeleteOrdersMessage(orders));
					}
				});
	}

	private void edit() {
		List<Order<?>> selectedOrders = getSelectedOrders();
		if (selectedOrders.isEmpty()) {
			alertSelectEditOrders();
		} else {
			if (selectedOrders.size() == 1) {
				Order<?> order = selectedOrders.get(0);
				send(new ShowEditFormMessage(order));
			} else {
				presenter.bulkEditButtonClicked();
			}
		}
	}

	@Override
	public void hideEditForm() {
		window.hide();
	}

	@Override
	public void showFilter(final OrderFilter filter, String locale, Collection<Vendor> vendors) {
		final Window filterWindow = new Window();
		filterWindow.setHeading(constants.filterHeading());
		filterWindow.setLayout(new FitLayout());
		filterWindow.setModal(true);
		filterWindow.setSize(600, 500);

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(150);
		formPanel.setFieldWidth(300);

		final MultiSelectField<Integer> stateField = new MultiSelectField<Integer>();
		stateField.setFieldLabel(constants.stateField());
		for (int state : OrderState.values.keySet()) {
			stateField.add(state, OrderState.values.get(state).get(locale));
		}
		stateField.setXValue(filter.getStates());
		formPanel.add(stateField);

		final SelectField<Vendor> vendorField = new SelectField<Vendor>();
		vendorField.setAllowBlank(true);
		vendorField.setFieldLabel(constants.vendorField());
		vendorField.add(null, "-");
		for (Vendor vendor : vendors) {
			vendorField.add(vendor, vendor.getName());
		}
		vendorField.setXValue(filter.getVendor());
		formPanel.add(vendorField);

		final TextField<String> bonusCodeField = new TextField<String>();
		bonusCodeField.setAllowBlank(true);
		bonusCodeField.setFieldLabel(constants.bonusCodeField());
		bonusCodeField.setValue(filter.getBonusCode());
		formPanel.add(bonusCodeField);

		formPanel.addButton(new Button(constants.applyFilter(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<Integer> states = stateField.getXValue();
				filter.setStates(new HashSet<Integer>(states));
				filter.setVendor(vendorField.getXValue());
				filter.setBonusCode(bonusCodeField.getValue() != null ? bonusCodeField.getValue().trim() : bonusCodeField.getValue());
				send(new ApplyFilterMessage(filter));
				filterWindow.hide();
			}
		}));
		formPanel.addButton(new Button(appConstants.close(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				filterWindow.hide();
			}
		}));

		filterWindow.add(formPanel);

		filterWindow.show();
	}

	@Override
	public void infoNotifyNewOrderResult() {
		MessageBox.info(appConstants.info(), constants.notifyNewOrderResult(), null);
	}

	@Override
	public void alertOrderStateIsNotModeration() {
		MessageBox.alert(appConstants.error(), constants.orderStateIsNotModeration(), null);
	}

	@Override
	public void infoNotifyOrdersAcceptedResult() {
		MessageBox.info(appConstants.info(), constants.notifyOrdersAcceptedResult(), null);
	}

	@Override
	public void alertOrderStateIsNotAccepted() {
		MessageBox.alert(appConstants.error(), constants.orderStateIsNotAccepted(), null);
	}

	@Override
	public void alertTrialOrderExists() {
		MessageBox.alert(appConstants.error(), constants.trialOrderExists(), null);
		if (addFormBinding != null)
			addFormBinding.startMonitoring();
		if (addSaveButton != null)
			addSaveButton.enable();
	}

	@Override
	public void alertOrderNumberExists() {
		MessageBox.alert(appConstants.error(), constants.orderNumberExists(), null);
		if (addFormBinding != null)
			addFormBinding.startMonitoring();
		if (addSaveButton != null)
			addSaveButton.enable();
	}

	@Override
	public void infoGenerateFlashResult() {
		MessageBox.info(appConstants.info(), constants.generateFlashResult(), null);
	}

	@Override
	public void alertGenerateFlashFailed() {
		MessageBox.alert(appConstants.error(), constants.generateFlashFailed(), null);
	}

    @Override
	public void infoGeneratePdfResult() {
		MessageBox.info(appConstants.info(), constants.generatePdfResult(), null);
	}

	@Override
	public void infoPublishWebFlashResult() {
		MessageBox.info(appConstants.info(), constants.publishWebFlashResult(), null);
	}

	@Override
	public void informExportStarted() {
		MessageBox.info(appConstants.info(), "Процесс экспорта запущен. Он займет некоторое время. "
				+ "Когда экспорт будет завершен, мы пришлем вам письмо "
				+ "и вы сможете загрузить файл с заказами по FTP.",
				null);
	}

	@Override
	public List<Order<?>> getSelectedOrders() {
		List<Order<?>> orders = new ArrayList<Order<?>>();
		List<BeanModel<Order<?>>> items = grid.getSelectionModel().getSelectedItems();
		for (BeanModel<Order<?>> item : items) {
			orders.add(item.getBean());
		}
		return orders;
	}

	@Override
	public void showBulkEditForm(String locale) {
		window = new Window();
		window.setHeading(constants.bulkEditWindowHeading());
		window.setLayout(new FitLayout());
		window.setSize(500, 200);
		window.setModal(true);
		window.setOnEsc(false);

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(200);
		formPanel.setScrollMode(Scroll.AUTO);

		HTML bulkEditOrdersTitle = new HTML("Изменение " + getSelectedOrders().size() + " заказов");
		bulkEditOrdersTitle.setStyleName("orderTitle");
		formPanel.add(bulkEditOrdersTitle);

		stateField = new SelectField<Integer>();
		Set<Integer> keySet = OrderState.values.keySet();
		for (int state : keySet) {
			if (state == OrderState.REJECTED) {
				continue;
			}
			stateField.add(state, OrderState.values.get(state).getNonEmptyValue(locale));
		}
		stateField.setFieldLabel(constants.state());
		stateField.setAllowBlank(false);
		formPanel.add(stateField);

		stateField.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Integer>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SelectValue<Integer>> se) {
				Integer state = stateField.getXValue();
				if (state == OrderState.SENT) {
					deliveryCodeField.show();
				} else {
					deliveryCodeField.hide();
				}
			}
		});

		deliveryCodeField = new TextField<String>();
		deliveryCodeField.setFieldLabel(constants.deliveryCodeField());
		deliveryCodeField.setVisible(false);
		formPanel.add(deliveryCodeField);

		if (securityService.isAllowed(Actions.ORDERS_MANAGEMENT)) {
			editSaveButton = new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					editSaveButton.disable();
					presenter.saveButtonClickedOnBulkEditForm();
				}
			});
			formPanel.addButton(editSaveButton);
			FormButtonBinding binding = new FormButtonBinding(formPanel);
			binding.addButton(editSaveButton);
		}

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
	public void fetchBulk(List<Order<?>> orders) {
		for (Order<?> order : orders) {
			order.setState(stateField.getXValue());
			order.setDeliveryCode(deliveryCodeField.getValue());
		}
	}

	@Override
	public void hideBulkEditForm() {
		if (window != null) {
			window.hide();
		}
	}

	private void alertSelectEditOrders() {
		MessageBox.alert(appConstants.warning(), constants.selectEditOrders(), null);
	}

    @Override
    public void alertPublishOrderFailed() {
        MessageBox.alert(appConstants.error(), constants.publishOrderFailed(), null);
    }
}
