package ru.imagebook.client.admin.view.request;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.ctl.request.ActMessage;
import ru.imagebook.client.admin.ctl.request.BookRequestMessage;
import ru.imagebook.client.admin.ctl.request.CloseRequestsMessage;
import ru.imagebook.client.admin.ctl.request.CreateRequestMessage;
import ru.imagebook.client.admin.ctl.request.DeleteRequestRequestMessage;
import ru.imagebook.client.admin.ctl.request.DeliveryMessage;
import ru.imagebook.client.admin.ctl.request.EditRequestMessage;
import ru.imagebook.client.admin.ctl.request.ExportRequestToXmlMessage;
import ru.imagebook.client.admin.ctl.request.LoadRequestsMessage;
import ru.imagebook.client.admin.ctl.request.PrintRequestMessage;
import ru.imagebook.client.admin.ctl.request.PutToBasketMessage;
import ru.imagebook.client.admin.ctl.request.RemoveFromBasketMessage;
import ru.imagebook.client.admin.ctl.request.RequestMessages;
import ru.imagebook.client.admin.ctl.request.RequestSelectedMessage;
import ru.imagebook.client.admin.ctl.request.RequestView;
import ru.imagebook.client.admin.ctl.request.SendDailyRequestMessage;
import ru.imagebook.client.admin.ctl.request.SendWeeklyBookRequestMessage;
import ru.imagebook.client.admin.ctl.request.UpdateRequestMessage;
import ru.imagebook.client.admin.ctl.request.UrgentRequestMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Request;
import ru.imagebook.shared.model.RequestState;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gwt.DateFormat;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.form.BooleanField;
import ru.minogin.core.client.gxt.form.XDateField;
import ru.minogin.core.client.gxt.grid.BooleanColumnConfig;
import ru.minogin.core.client.gxt.grid.IntegerColumnConfig;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
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
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.LiveGridView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.LiveToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RequestViewImpl extends View implements RequestView {
	public static final String REQUEST_DATE = "requestDate";
	public static final String STATE_TEXT = "stateText";
	public static final String BILL_DATE_TEXT = "billDateText";
	private static final String SUM = "sum";

	private final Widgets widgets;
	private final RequestConstants constants;
	protected AsyncCallback<PagingLoadResult<BeanModel<Request>>> callback;
	private BasePagingLoader<PagingLoadResult<BeanModel<Request>>> loader;
	private Grid<BeanModel<Request>> grid;
	protected AsyncCallback<PagingLoadResult<BeanModel<Order<?>>>> fromCallback;
	private Grid<BeanModel<Order<?>>> fromGrid;
	private Grid<BeanModel<Order<?>>> toGrid;
	private final CommonConstants appConstants;
	private Window addWindow;
	private ListStore<BeanModel<Order<?>>> ordersStore;
	private Window editWindow;
	private ListStore<BeanModel<Order<?>>> toStore;
	private ListStore<BeanModel<Order<?>>> fromStore;

	@Inject
	public RequestViewImpl(Dispatcher dispatcher, Widgets widgets,
			RequestConstants constants, CommonConstants appConstants) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
	}

	@Override
	public void showSection() {
		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);

		LayoutContainer container = new LayoutContainer(new BorderLayout());

		ContentPanel panel = new ContentPanel(new FitLayout());
		panel.setHeading(constants.panelHeading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(constants.addRequest(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(RequestMessages.ADD_REQUEST);
					}
				}));
		toolBar.add(new Button(constants.editRequest(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						BeanModel<Request> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Request request = item.getBean();
							send(new EditRequestMessage(request));
						}
					}
				}));

		Button printFormsButton = new Button(constants.printFormsButton());
		Menu menu = new Menu();
		menu.add(new MenuItem(constants.printRequestButton(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						BeanModel<Request> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Request request = item.getBean();
							send(new PrintRequestMessage(request.getId()));
						}
					}
				}));
		menu.add(new MenuItem(constants.bookRequestButton(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						BeanModel<Request> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Request request = item.getBean();
							send(new BookRequestMessage(request.getId()));
						}
					}
				}));
		menu.add(new MenuItem(constants.actButton(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						BeanModel<Request> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Request request = item.getBean();
							send(new ActMessage(request.getId()));
						}
					}
				}));
		menu.add(new MenuItem(constants.deliveryButton(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						BeanModel<Request> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Request request = item.getBean();
							send(new DeliveryMessage(request.getId()));
						}
					}
				}));
		menu.add(new MenuItem(constants.urgentRequestButton(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						BeanModel<Request> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Request request = item.getBean();
							if (!request.isUrgent()) {
								MessageBox.info(appConstants.info(), constants.infoUrgentRequest(), null); 
							} else {	
								send(new UrgentRequestMessage(request.getId()));								
							}	
						}	
					}
				}));
		printFormsButton.setMenu(menu);
		toolBar.add(printFormsButton);

		toolBar.add(new Button(constants.closeRequest(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						new ConfirmMessageBox(appConstants.warning(), constants
								.confirmCloseRequest(), new Listener<BaseEvent>() {
							@Override
							public void handleEvent(BaseEvent be) {
								List<BeanModel<Request>> selectedItems = grid
										.getSelectionModel().getSelectedItems();
								if (!selectedItems.isEmpty()) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<Request> row : selectedItems) {
										Request request = row.getBean();
										ids.add(request.getId());
									}
									send(new CloseRequestsMessage(ids));
								}
							}
						});
					}
				}));

		toolBar.add(new Button(constants.deleteRequest(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						List<Request> requests = new ArrayList<Request>();
						List<BeanModel<Request>> selectedItems = grid.getSelectionModel()
								.getSelectedItems();
						for (BeanModel<Request> row : selectedItems) {
							requests.add(row.getBean());
						}
						send(new DeleteRequestRequestMessage(requests));
					}
				}));
		
		Button testButton = new Button(constants.testButton());
		Menu testMenu = new Menu();
		testMenu.add(new MenuItem(constants.generateAndSendDailyRequests(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						send(new SendDailyRequestMessage());
					}
				}));
		testMenu.add(new MenuItem(constants.sendWeeklyBookRequest(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {						
						send(new SendWeeklyBookRequestMessage());
					}
				}));
        testMenu.add(new MenuItem(constants.exportToXml(),
                new SelectionListener<MenuEvent>() {
                    @Override
                    public void componentSelected(MenuEvent ce) {
                        send(new ExportRequestToXmlMessage());
                    }
                }));
		testButton.setMenu(testMenu);
		toolBar.add(testButton);

		panel.setTopComponent(toolBar);

		DataProxy<PagingLoadResult<BeanModel<Request>>> proxy = new DataProxy<PagingLoadResult<BeanModel<Request>>>() {
			@Override
			public void load(DataReader<PagingLoadResult<BeanModel<Request>>> reader,
					Object loadConfig,
					AsyncCallback<PagingLoadResult<BeanModel<Request>>> callback) {
				PagingLoadConfig config = (PagingLoadConfig) loadConfig;
				RequestViewImpl.this.callback = callback;
				send(new LoadRequestsMessage(config.getOffset(), config.getLimit()));
			}
		};
		loader = new BasePagingLoader<PagingLoadResult<BeanModel<Request>>>(proxy);
		ListStore<BeanModel<Request>> store = new ListStore<BeanModel<Request>>(
				loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(REQUEST_DATE, constants.dateColumn(), 100));
		columns.add(new BooleanColumnConfig(Request.URGENT, constants.urgentColumn(),
				100));
		columns.add(new ColumnConfig(STATE_TEXT, constants.stateColumn(), 100));
		columns.add(new ColumnConfig(Request.BILL_NUMBER, constants
				.billNumberColumn(), 100));
		columns.add(new ColumnConfig(BILL_DATE_TEXT, constants.billDateColumn(),
				100));
		columns.add(new BooleanColumnConfig(Request.PAID, constants.paidColumn(),
				100));
		columns.add(new BooleanColumnConfig(Request.CONFIRMED, constants
				.confirmedColumn(), 100));
		columns.add(new IntegerColumnConfig(Request.TOTAL, constants.totalColumn(),
				100));
		columns.add(new IntegerColumnConfig(Request.TOTAL2, constants
				.total2Column(), 100));
		columns.add(new IntegerColumnConfig(SUM, constants.sumColumn(), 100));

		grid = new Grid<BeanModel<Request>>(store, new ColumnModel(columns));
		LiveGridView liveView = new LiveGridView();
		liveView.setEmptyText(constants.emptyGrid());
		grid.setView(liveView);
		grid.getView().setSortingEnabled(false);
		grid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<BeanModel<Request>>() {
					@Override
					public void selectionChanged(
							SelectionChangedEvent<BeanModel<Request>> se) {
						BeanModel<Request> selectedItem = se.getSelectedItem();
						Request request = null;
						if (selectedItem != null)
							request = selectedItem.getBean();
						send(new RequestSelectedMessage(request));
					}
				});
		panel.add(grid, new RowData(1, 1));

		ToolBar gridToolBar = new ToolBar();
		LiveToolItem item = new LiveToolItem();
		item.bindGrid(grid);
		gridToolBar.add(item);
		panel.setBottomComponent(gridToolBar);

		container.add(panel, new BorderLayoutData(LayoutRegion.CENTER, 0.8f));

		ContentPanel ordersPanel = new ContentPanel(new FitLayout());
		ordersPanel.setHeading(constants.ordersPanel());

		ordersStore = new ListStore<BeanModel<Order<?>>>();

		List<ColumnConfig> orderColumns = new ArrayList<ColumnConfig>();
		orderColumns.add(new ColumnConfig(Order.NUMBER, constants.orderColumn(),
				100));

		Grid<BeanModel<Order<?>>> orderGrid = new Grid<BeanModel<Order<?>>>(
				ordersStore, new ColumnModel(orderColumns));
		orderGrid.setAutoExpandColumn(Order.NUMBER);
		ordersPanel.add(orderGrid);

		container.add(ordersPanel, new BorderLayoutData(LayoutRegion.EAST, 0.2f));

		desktop.removeAll();
		desktop.add(container);
		desktop.layout();
	}

	@Override
	public void showRequests(List<Request> requests, int offset, int total) {
		List<BeanModel<Request>> rows = new ArrayList<BeanModel<Request>>();
		for (Request request : requests) {
			BeanModel<Request> row = new BeanModel<Request>(request);
			row.setTransient(REQUEST_DATE, DateFormat.formatDateTime(request.getDate()));
			String stateText = RequestState.values.get(request.getState());
			row.setTransient(STATE_TEXT, stateText);
			row.setTransient(BILL_DATE_TEXT,
					DateFormat.formatDate(request.getBillDate()));
			row.setTransient(SUM, request.getTotal() + request.getTotal2());
			rows.add(row);
		}
		PagingLoadResult<BeanModel<Request>> result = new BasePagingLoadResult<BeanModel<Request>>(
				rows, offset, total);
		callback.onSuccess(result);
	}

	@Override
	public void showAddForm() {
		addWindow = new Window();
		addWindow.setHeading(constants.addWindow());
		addWindow.setLayout(new RowLayout());
		addWindow.setModal(true);
		addWindow.setOnEsc(false);
		addWindow.setSize(800, 600);

		FormPanel formPanel = new FormPanel();
		formPanel.setLabelWidth(150);
		formPanel.setHeaderVisible(false);

		final BooleanField urgentField = new BooleanField(constants.urgentField(),
				false, formPanel);

		addWindow.add(formPanel, new RowData(1, -1));		
		
		LayoutContainer gridsContainer = new LayoutContainer(new RowLayout(
				Orientation.HORIZONTAL));

		ContentPanel fromPanel = new ContentPanel(new FitLayout());
		fromPanel.setHeading(constants.fromPanelHeading());

		fromStore = new ListStore<BeanModel<Order<?>>>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Order.NUMBER, constants.orderNumberColumn(),
				100));

		fromGrid = new Grid<BeanModel<Order<?>>>(fromStore,
				new ColumnModel(columns));
		fromGrid.setAutoExpandColumn(Order.NUMBER);
		fromGrid.getView().setSortingEnabled(false);
		fromPanel.add(fromGrid, new RowData(1, 1));

		gridsContainer.add(fromPanel, new RowData(0.45, 1));

		gridsContainer.add(new Html(), new RowData(0.1, 1));

		ContentPanel toPanel = new ContentPanel(new FitLayout());
		toPanel.setHeading(constants.toPanelHeading());

		toStore = new ListStore<BeanModel<Order<?>>>();

		columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Order.NUMBER, constants.orderNumberColumn(),
				100));

		toGrid = new Grid<BeanModel<Order<?>>>(toStore, new ColumnModel(columns));
		toGrid.setAutoExpandColumn(Order.NUMBER);
		toGrid.getView().setSortingEnabled(false);
		toPanel.add(toGrid, new RowData(1, 1));

		gridsContainer.add(toPanel, new RowData(0.45, 1));

		new GridDragSource(fromGrid);

		GridDropTarget target = new GridDropTarget(toGrid);
		target.setAllowSelfAsSource(false);
		target.addDNDListener(new DNDListener() {
			@Override
			public void dragDrop(DNDEvent e) {
				List<BeanModel<Order<?>>> items = e.getData();
				List<Integer> orderIds = new ArrayList<Integer>();
				for (BeanModel<Order<?>> item : items) {
					orderIds.add(item.getBean().getId());
				}
				send(new PutToBasketMessage(orderIds));
			}
		});

		new GridDragSource(toGrid);

		target = new GridDropTarget(fromGrid);
		target.setAllowSelfAsSource(false);
		target.addDNDListener(new DNDListener() {
			@Override
			public void dragDrop(DNDEvent e) {
				List<BeanModel<Order<?>>> items = e.getData();
				List<Integer> orderIds = new ArrayList<Integer>();
				for (BeanModel<Order<?>> item : items) {
					orderIds.add(item.getBean().getId());
				}
				send(new RemoveFromBasketMessage(orderIds));
			}
		});

		addWindow.add(gridsContainer, new RowData(1, 1));

		addWindow.addButton(new Button(constants.createRequestButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {						
						Boolean urgentFlag = urgentField.getValue();
						send(new CreateRequestMessage(urgentFlag));
					}
				}));
		addWindow.addButton(new Button(appConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						addWindow.hide();
					}
				}));

		addWindow.show();
	}

	@Override
	public void showNonBasketOrders(List<Order<?>> orders) {
		fromStore.removeAll();
		for (Order<?> order : orders) {
			fromStore.add(new BeanModel<Order<?>>(order));
		}
	}

	@Override
	public void showBasketOrders(List<Order<?>> orders) {
		toStore.removeAll();
		for (Order<?> order : orders) {
			toStore.add(new BeanModel<Order<?>>(order));
		}
	}

	@Override
	public void closeAddForm() {
		addWindow.hide();
	}

	@Override
	public void reloadGrid() {
		loader.load();
	}

	@Override
	public void showRequest(Request request) {
		ordersStore.removeAll();
		if (request != null) {
			for (Order<?> order : request.getOrders()) {
				BeanModel<Order<?>> model = new BeanModel<Order<?>>(order);
				ordersStore.add(model);
			}
		}
	}

	@Override
	public void confirmDelete() {
		new ConfirmMessageBox(appConstants.warning(), constants.confirmDelete(),
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						send(RequestMessages.DELETE_CONFIRMED);
					}
				});
	}

	@Override
	public void showEditForm(final Request request) {
		editWindow = new Window();
		editWindow.setHeading(constants.editWindow());
		editWindow.setLayout(new RowLayout());
		editWindow.setModal(true);
		editWindow.setOnEsc(false);
		editWindow.setSize(800, 600);

		FormPanel formPanel = new FormPanel();
		formPanel.setLabelWidth(150);
		formPanel.setHeaderVisible(false);

		final BooleanField urgentField = new BooleanField(constants.urgentField(),
				request.isUrgent(), formPanel);
		final BooleanField paidField = new BooleanField(constants.paidField(),
				request.isPaid(), formPanel);
		final BooleanField confirmedField = new BooleanField(
				constants.confirmedField(), request.isConfirmed(), formPanel);

		final TextField<String> billNumberField = new TextField<String>();
		billNumberField.setFieldLabel(constants.billNumberField());
		billNumberField.setValue(request.getBillNumber());
		formPanel.add(billNumberField);

		final XDateField billDateField = new XDateField();
		billDateField.setFieldLabel(constants.billDateField());
		billDateField.setValue(request.getBillDate());
		formPanel.add(billDateField);

		editWindow.add(formPanel, new RowData(1, -1));

		LayoutContainer gridsContainer = new LayoutContainer(new RowLayout(
				Orientation.HORIZONTAL));

		ContentPanel fromPanel = new ContentPanel(new FitLayout());
		fromPanel.setHeading(constants.fromPanelHeading());

		fromStore = new ListStore<BeanModel<Order<?>>>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Order.NUMBER, constants.orderNumberColumn(),
				100));

		fromGrid = new Grid<BeanModel<Order<?>>>(fromStore,
				new ColumnModel(columns));
		fromGrid.setAutoExpandColumn(Order.NUMBER);
		fromGrid.getView().setSortingEnabled(false);
		fromPanel.add(fromGrid, new RowData(1, 1));

		gridsContainer.add(fromPanel, new RowData(0.45, 1));

		gridsContainer.add(new Html(), new RowData(0.1, 1));

		ContentPanel toPanel = new ContentPanel(new FitLayout());
		toPanel.setHeading(constants.toPanelHeading());

		toStore = new ListStore<BeanModel<Order<?>>>();
		for (Order<?> order : request.getOrders()) {
			toStore.add(new BeanModel<Order<?>>(order));
		}

		columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Order.NUMBER, constants.orderNumberColumn(),
				100));

		toGrid = new Grid<BeanModel<Order<?>>>(toStore, new ColumnModel(columns));
		toGrid.setAutoExpandColumn(Order.NUMBER);
		toGrid.getView().setSortingEnabled(false);
		toPanel.add(toGrid, new RowData(1, 1));

		gridsContainer.add(toPanel, new RowData(0.45, 1));

		new GridDragSource(fromGrid);

		GridDropTarget target = new GridDropTarget(toGrid);
		target.setAllowSelfAsSource(false);
		// target.addDNDListener(new DNDListener() {
		// @Override
		// public void dragDrop(DNDEvent e) {
		// List<BeanModel<Order<?>>> items = e.getData();
		// List<Integer> orderIds = new ArrayList<Integer>();
		// for (BeanModel<Order<?>> item : items) {
		// orderIds.add(item.getBean().getId());
		// }
		// send(new PutToBasketMessage(orderIds));
		// }
		// });

		new GridDragSource(toGrid);

		target = new GridDropTarget(fromGrid);
		target.setAllowSelfAsSource(false);
		// target.addDNDListener(new DNDListener() {
		// @Override
		// public void dragDrop(DNDEvent e) {
		// List<BeanModel<Order<?>>> items = e.getData();
		// List<Integer> orderIds = new ArrayList<Integer>();
		// for (BeanModel<Order<?>> item : items) {
		// orderIds.add(item.getBean().getId());
		// }
		// send(new RemoveFromBasketMessage(orderIds));
		// }
		// });

		editWindow.add(gridsContainer, new RowData(1, 1));

		editWindow.addButton(new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						request.setUrgent(urgentField.getValue());
						request.setPaid(paidField.getValue());
						request.setConfirmed(confirmedField.getValue());
						request.setBillNumber(billNumberField.getValue());
						request.setBillDate(billDateField.getValue());

						request.getOrders().clear();
						for (BeanModel<Order<?>> model : toStore.getModels()) {
							request.addOrder(model.getBean());
						}
						send(new UpdateRequestMessage(request));
					}
				}));
		editWindow.addButton(new Button(appConstants.close(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						editWindow.hide();
					}
				}));

		editWindow.show();
	}

	@Override
	public void closeEditForm() {
		editWindow.hide();
	}
	
    @Override
    public void notifyTestRequestDone() {
        MessageBox.info(appConstants.info(), constants.testRequestDone(), null);
    }
}
