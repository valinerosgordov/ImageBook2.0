package ru.imagebook.client.admin.view.bill;

import java.util.*;

import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import ru.imagebook.client.admin.ctl.action.LoadVendorActionsMessage;
import ru.imagebook.client.admin.ctl.bill.BillSelectedMessage;
import ru.imagebook.client.admin.ctl.bill.BillView;
import ru.imagebook.client.admin.ctl.bill.DeleteBillsMessage;
import ru.imagebook.client.admin.ctl.bill.LoadBillsMessage;
import ru.imagebook.client.admin.ctl.bill.MarkPaidMessage;
import ru.imagebook.client.admin.ctl.bill.UpdateBillMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.common.service.BillCalculator;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.admin.Actions;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.shared.model.*;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gwt.DateFormat;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.*;
import ru.minogin.core.client.gxt.grid.BooleanColumnConfig;
import ru.minogin.core.client.gxt.grid.FixedLiveGridView;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.LiveGridView;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.minogin.core.client.i18n.MultiString;

@Singleton
public class BillViewImpl extends View implements BillView {
	public static final String DELIVERY_TYPE_TEXT = "deliveryTypeText";
	public static final String DELIVERY_COST_TEXT = "deliveryCostText";
	public static final String VENDOR_TEXT = "vendorText";
	public static final String DATE_TEXT = "dateText";
	public static final String USER_TEXT = "userText";
	public static final String USER_NAME_TEXT = "userNameText";
	public static final String STATE_TEXT = "stateText";
	public static final String DS_SEND_STATE_TEXT = "dsSendStateText";

	private final Widgets widgets;
	private final BillConstants constants;
	private AsyncCallback<PagingLoadResult<BeanModel<Bill>>> callback;
	private BasePagingLoader<PagingLoadResult<BeanModel<Bill>>> loader;
	private Grid<BeanModel<Bill>> grid;
	private final CommonConstants appConstants;
	private ListStore<BeanModel<Order<?>>> ordersStore;
	private Window editWindow;
	private SecurityService securityService;

    private BooleanField advField;
    private IntegerField multishipOrderIdField;
    private XTextField multishipDeliveryServiceField;
    private XDateField orientDeliveryDateField;
    private IntegerField deliveryTimeField;
	private XTextField pickpointPostamateIdField;
	private XTextField pickpointPostamateAddressField;
	private BillFilter adapter;
	private final I18nService i18nService;
	private Map.Entry<Integer, MultiString> selectedBillState;
	private int limitPage = 50;
	private Status filterStatus;
	private ToolBar filterToolBar;
	private ContentPanel panel;

	@Inject
	public BillViewImpl(Dispatcher dispatcher, Widgets widgets,
			BillConstants constants, CommonConstants appConstants,
			SecurityService securityService, I18nService i18nService) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
		this.securityService = securityService;
		this.i18nService = i18nService;
	}

	@Override
	public void showSection() {
		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);

		LayoutContainer container = new LayoutContainer(new BorderLayout());

		panel = new ContentPanel(new RowLayout());
		panel.setHeading(constants.heading());

		ToolBar toolBar = new ToolBar();
		if (securityService.isAllowed(Actions.BILLS_UPDATE)) {
			toolBar.add(new Button(appConstants.edit(),
					new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							BeanModel<Bill> item = grid.getSelectionModel().getSelectedItem();
							if (item != null) {
								Bill bill = item.getBean();
								showEditForm(bill);
							}
						}
					}));
		}
		toolBar.add(new Button(constants.setPaidButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<BeanModel<Bill>> items = grid.getSelectionModel()
								.getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(), constants
									.confirmSetPaid(), new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<Bill> model : items) {
										ids.add(model.getBean().getId());
									}
									send(new MarkPaidMessage(ids));
								}
							});
						}
					}
				}));
		toolBar.add(new Button(appConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<BeanModel<Bill>> items = grid.getSelectionModel()
								.getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(), constants
									.confirmDelete(), new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<Bill> model : items) {
										ids.add(model.getBean().getId());
									}
									send(new DeleteBillsMessage(ids));
								}
							});
						}
					}
				}));
		toolBar.add(new Button(constants.searchButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						showFilter();
					}
				}));
		panel.setTopComponent(toolBar);

		filterToolBar = new ToolBar();
		filterToolBar.hide();
		filterToolBar.addStyleName("filter-status");
		filterStatus = new Status();
		filterToolBar.add(filterStatus);
		filterToolBar.add(new Label(" | "));
		filterToolBar.add(new Button(constants.filterCancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						adapter = null;
						send(new LoadBillsMessage(0, limitPage, adapter));
					}
				}));
		panel.add(filterToolBar, new RowData(1, -1));

		DataProxy<PagingLoadResult<BeanModel<Bill>>> proxy = new DataProxy<PagingLoadResult<BeanModel<Bill>>>() {
			@Override
			public void load(DataReader<PagingLoadResult<BeanModel<Bill>>> reader,
					Object loadConfig,
					AsyncCallback<PagingLoadResult<BeanModel<Bill>>> callback) {
				PagingLoadConfig config = (PagingLoadConfig) loadConfig;
				BillViewImpl.this.callback = callback;
				limitPage = config.getLimit();
				send(new LoadBillsMessage(config.getOffset(), config.getLimit(), adapter));
			}
		};
		loader = new BasePagingLoader<PagingLoadResult<BeanModel<Bill>>>(proxy);
		ListStore<BeanModel<Bill>> store = new ListStore<BeanModel<Bill>>(loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Bill.ID, constants.idColumn(), 70));
		columns.add(new ColumnConfig(DATE_TEXT, constants.dateColumn(), 150));
		columns.add(new ColumnConfig(USER_TEXT, constants.userColumn(), 150));
		columns.add(new ColumnConfig(USER_NAME_TEXT, constants.userNameColumn(), 150));
		ColumnConfig totalColumn = new ColumnConfig(Bill.TOTAL, constants.totalColumn(), 100);
		totalColumn.setAlignment(HorizontalAlignment.RIGHT);
		columns.add(totalColumn);
        ColumnConfig phTotalColumn = new ColumnConfig(Bill.PH_TOTAL, constants.phTotalColumn(), 155);
        phTotalColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(phTotalColumn);
		columns.add(new ColumnConfig(STATE_TEXT, constants.stateColumn(), 150));
		if (securityService.isAllowed(Actions.BILLS_UPDATE)) {
			columns.add(new BooleanColumnConfig(Bill.ADV, constants.advColumn(), 100));
		}
		columns.add(new ColumnConfig(DELIVERY_TYPE_TEXT, constants.deliveryTypeColumn(), 150));
		ColumnConfig deliveryCostColumn = new ColumnConfig(DELIVERY_COST_TEXT, constants.deliveryCostColumn(), 150);
		deliveryCostColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(deliveryCostColumn);
		if (securityService.isAllowed(Actions.VIEW_ALL_VENDORS)) {
			columns.add(new ColumnConfig(VENDOR_TEXT, constants.vendorColumn(), 150));
		}
		columns.add(new ColumnConfig(DS_SEND_STATE_TEXT, constants.dsSendStateColumn(), 150));
		columns.add(new ColumnConfig(Bill.DS_ERROR_MESSAGE,
				constants.dsErrorMessageColumn(), 300));
		columns.add(new ColumnConfig(Bill.SENDING_ID,
				constants.sendingIdColumn(), 150));
		columns.add(new ColumnConfig(Bill.DS_SENDING_ID, constants.dsSendingIdColumn(), 150));

		grid = new Grid<BeanModel<Bill>>(store, new ColumnModel(columns));
		LiveGridView liveView = new FixedLiveGridView();
		liveView.setEmptyText(constants.emptyGrid());
		grid.setView(liveView);
		grid.getView().setSortingEnabled(false);
		grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				// edit();
			}
		});
		grid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<BeanModel<Bill>>() {
					@Override
					public void selectionChanged(SelectionChangedEvent<BeanModel<Bill>> se) {
						BeanModel<Bill> selectedItem = se.getSelectedItem();
						if (selectedItem != null) {
							Bill bill = selectedItem.getBean();
							send(new BillSelectedMessage(bill));
						}
					}
				});
		panel.add(grid, new RowData(1, 1));

		container.add(panel, new BorderLayoutData(LayoutRegion.CENTER, 0.8f));

		ContentPanel ordersPanel = new ContentPanel(new FitLayout());
		ordersPanel.setHeading(constants.ordersPanel());
		ordersStore = new ListStore<BeanModel<Order<?>>>();
		columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Order.NUMBER, constants.orderNumberColumn(),
				100));
		Grid<BeanModel<Order<?>>> ordersGrid = new Grid<BeanModel<Order<?>>>(
				ordersStore, new ColumnModel(columns));
		ordersPanel.add(ordersGrid);
		container.add(ordersPanel, new BorderLayoutData(LayoutRegion.EAST, 0.2f));

		desktop.removeAll();
		desktop.add(container);
		desktop.layout();
	}

	private void showEditForm(final Bill bill) {
		editWindow = new XWindow(constants.editWindowHeading());

		FormPanel formPanel = new XFormPanel();

		LabelField numberField = new LabelField();
		numberField.setFieldLabel(constants.numberField());
		numberField.setValue(bill.getId());
		formPanel.add(numberField);

		advField = new BooleanField(constants.advField(), bill.isAdv(), formPanel);

		LabelField weightField = new LabelField();
		weightField.setFieldLabel(constants.weight() + ":");
		weightField.setValue(bill.getWeight());
		formPanel.add(weightField);

        deliveryTimeField = new IntegerField();
        deliveryTimeField.setFieldLabel(constants.deliveryTimeField());
        deliveryTimeField.setValue(bill.getDeliveryTime());

        multishipOrderIdField = new IntegerField();
        multishipOrderIdField.setFieldLabel(constants.multishipOrderIdField());
        multishipOrderIdField.setValue(bill.getMultishipOrderId());

        multishipDeliveryServiceField = new XTextField();
        multishipDeliveryServiceField.setFieldLabel(constants.multishipDeliveryServiceField());
        multishipDeliveryServiceField.setValue(bill.getMshDeliveryService());

        orientDeliveryDateField = new XDateField();
        orientDeliveryDateField.setFieldLabel(constants.orientDeliveryDateField());
        orientDeliveryDateField.setValue(bill.getOrientDeliveryDate());

		pickpointPostamateIdField = new XTextField();
		pickpointPostamateIdField.setFieldLabel(constants.pickpointPostamateIdField());
		pickpointPostamateIdField.setValue(bill.getPickpointPostamateID());
		pickpointPostamateIdField.setReadOnly(true);
		pickpointPostamateAddressField = new XTextField();
		pickpointPostamateAddressField.setFieldLabel(constants.pickpointPostamateAddressField());
		pickpointPostamateAddressField.setValue(bill.getPickpointAddress());
		pickpointPostamateAddressField.setReadOnly(true);

		boolean isShowDeliveryFieldSet = bill.getDeliveryType() != null
				&& (bill.getDeliveryType() == DeliveryType.MAJOR || bill.getDeliveryType() == DeliveryType.MULTISHIP
				|| bill.getDeliveryType() == DeliveryType.POSTAMATE);

		if (isShowDeliveryFieldSet) {
			FieldSet deliveryFieldSet = new FieldSet();
			FormLayout deliveryParamsLayout = new FormLayout();
			deliveryParamsLayout.setLabelWidth(150);
			deliveryFieldSet.setLayout(deliveryParamsLayout);

			if (bill.getDeliveryType() == DeliveryType.MAJOR) {
				deliveryFieldSet.setHeading(constants.majorFieldSet());

				deliveryTimeField.setAllowBlank(false);
				deliveryFieldSet.add(deliveryTimeField);
			} else if (bill.getDeliveryType() == DeliveryType.MULTISHIP) {
				deliveryFieldSet.setHeading(constants.multishipFieldSet());

				multishipOrderIdField.setAllowBlank(false);
				deliveryFieldSet.add(multishipOrderIdField);

				multishipDeliveryServiceField.setAllowBlank(false);
				deliveryFieldSet.add(multishipDeliveryServiceField);

				orientDeliveryDateField.setAllowBlank(false);
				deliveryFieldSet.add(orientDeliveryDateField);
			} else if (bill.getDeliveryType() == DeliveryType.POSTAMATE) {
				deliveryFieldSet.setHeading(constants.pickPointFieldSet());
				deliveryFieldSet.add(pickpointPostamateIdField);
				deliveryFieldSet.add(pickpointPostamateAddressField);
			}

			formPanel.add(deliveryFieldSet);
		}

        Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						fetchBill(bill);
						send(new UpdateBillMessage(bill));
					}
				});
		formPanel.addButton(saveButton);
		formPanel.addButton(new Button(appConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						editWindow.hide();
					}
				}));
		new FormButtonBinding(formPanel).addButton(saveButton);

		editWindow.add(formPanel);

		editWindow.show();
	}

    private void fetchBill(Bill bill) {
        bill.setAdv(advField.getValue());
        bill.setMultishipOrderId(multishipOrderIdField.getValue());
        bill.setMshDeliveryService(multishipDeliveryServiceField.getValue());
        bill.setOrientDeliveryDate(orientDeliveryDateField.getValue());
        bill.setDeliveryTime(deliveryTimeField.getValue());
    }

	@Override
	public void hideEditForm() {
		editWindow.hide();
	}

	@Override
	public void showBills(List<Bill> bills, int offset, int total, String locale) {
		List<BeanModel<Bill>> rows = new ArrayList<BeanModel<Bill>>();
		for (Bill bill : bills) {
			bill.setTransient(USER_TEXT, bill.getUser().getFullName());
			bill.setTransient(USER_NAME_TEXT, bill.getUser().getUserName());
			bill.setTransient(DATE_TEXT, DateFormat.formatDate(bill.getDate()));
			String stateText = BillState.values.get(bill.getState()).get(locale);
			bill.setTransient(STATE_TEXT, stateText);
			if (Integer.valueOf(DeliveryType.POSTAMATE).equals(bill.getDeliveryType())
                    || Integer.valueOf(DeliveryType.DDELIVERY).equals(bill.getDeliveryType())) {
				String dsSendState = bill.getDsSendState() != null
						? DsSendState.values.get(bill.getDsSendState()).get(locale)
						: "";
				bill.setTransient(DS_SEND_STATE_TEXT, dsSendState);
			}

			Integer deliveryType = bill.getDeliveryType();
			if (deliveryType != null) {
				bill.setTransient(DELIVERY_TYPE_TEXT, DeliveryType.values.get(deliveryType));
			}
			bill.setTransient(DELIVERY_COST_TEXT, BillCalculator.computeDeliveryCost(bill));
			bill.setTransient(VENDOR_TEXT, bill.getUser().getVendor().getName());
			rows.add(new BeanModel<Bill>(bill));
		}
		if (adapter != null) {
			filterStatus.setText(constants.filterStatus());
			filterToolBar.show();
			panel.layout(true);
		}
		else {
			filterToolBar.hide();
			panel.layout(true);
		}
		callback.onSuccess(new BasePagingLoadResult<BeanModel<Bill>>(rows, offset,
				total));
	}

	@Override
	public void reload() {
		loader.load();
	}

	@Override
	public void showBill(Bill bill) {
		ordersStore.removeAll();
		for (Order<?> order : bill.getOrders()) {
			ordersStore.add(new BeanModel<Order<?>>(order));
		}
	}

	@Override
	public void showFilter() {
		final Window filterWindow = new Window();
		filterWindow.setHeading(constants.searchButton());
		filterWindow.setLayout(new FitLayout());
		filterWindow.setModal(true);
		filterWindow.setSize(600, 300);
		populateFilter(filterWindow);
		filterWindow.show();
	}

	private void populateFilter(final Window filterWindow) {
		filterWindow.removeAll();
		FormPanel formPanel = new XFormPanel();
		final IntegerField numBill = new IntegerField(constants.numberField(), true, adapter != null ? adapter.getNumBill() : null, formPanel);

		Label totalFromLbl =  new Label(constants.totalLimitFrom());
		final IntegerField totalLimitFrom = new IntegerField();
		totalLimitFrom.setValue(adapter != null ? adapter.getTotalLimitFrom() : null);
		final IntegerField totalLimitTo = new IntegerField();
		totalLimitTo.setValue(adapter != null ? adapter.getTotalLimitTo() : null);
		final MultiField<Integer> totalLimits = new MultiField<Integer>(constants.totalLimitFrom(), new LabelField(constants.limitFrom()), totalLimitFrom, new LabelField(constants.limitTo()), totalLimitTo);
		totalLimits.setSpacing(20);
		formPanel.add(totalLimits);
		final XTextField customer = new XTextField(constants.customer(), true, adapter != null ? adapter.getCustomer() : null, formPanel);
		final IntegerField albumNumber = new IntegerField(constants.albumNumber(), true, adapter != null ? adapter.getAlbumNumber() : null, formPanel);
		final XDateField dateFrom = new XDateField();
		dateFrom.setValue(adapter != null ? adapter.getDateFrom() : null);
		final XDateField dateTo = new XDateField();
		dateTo.setValue(adapter != null ? adapter.getDateTo() : null);

		final MultiField<Integer> dateLimits = new MultiField<Integer>(constants.billDateFrom(), new LabelField(constants.limitFrom()), dateFrom, new LabelField(constants.limitTo()), dateTo);
		dateLimits.setSpacing(20);
		formPanel.add(dateLimits);

		final SelectField<Map.Entry<Integer, MultiString>> shownBillState =  new SelectField<Map.Entry<Integer, MultiString>>();
		Integer state = adapter != null ? adapter.getState() : null;
		shownBillState.add(null, "-");
		for (Map.Entry<Integer, MultiString> item : BillState.values.entrySet()) {
			shownBillState.add(item, item.getValue().get(i18nService.getLocale()));
		}
		shownBillState.setAllowBlank(true);
		shownBillState.setFieldLabel(constants.status());
		shownBillState.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Map.Entry<Integer, MultiString>>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SelectValue<Map.Entry<Integer, MultiString>>> se) {
				selectedBillState = se.getSelectedItem().getValue();
			}
		});
		if (selectedBillState != null) {
			shownBillState.setValue(new SelectValue<Map.Entry<Integer, MultiString>>(selectedBillState, selectedBillState.getValue().getNonEmptyValue(i18nService.getLocale())));
		}
		formPanel.add(shownBillState);
		final BooleanField isAdvField = new BooleanField(constants.hasAdvertise(), adapter != null ? adapter.isAdv() : null, formPanel);

		formPanel.addButton(new Button(constants.searchButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				adapter = new BillFilter(numBill.getValue(), totalLimitFrom.getValue(), totalLimitTo.getValue(),
						selectedBillState != null ? selectedBillState.getKey() : null, dateFrom.getValue(), dateTo.getValue(), customer.getValue(), isAdvField.getValue(), albumNumber.getValue());
				send(new LoadBillsMessage(0, limitPage, adapter));
				filterWindow.hide();
			}
		}));
		formPanel.addButton(new Button(constants.clearButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				adapter = null;
				selectedBillState = null;
				numBill.clear();
				totalLimitFrom.clear();
				totalLimitTo.clear();
				shownBillState.clearSelections();
				dateFrom.clear();
				dateTo.clear();
				customer.clear();
				isAdvField.clear();
				albumNumber.clear();
			}
		}));
		formPanel.addButton(new Button(appConstants.close(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				filterWindow.hide();
			}
		}));
		filterWindow.add(formPanel);
	}


}
