package ru.imagebook.client.admin.view.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.ctl.delivery.AddOrderMessage;
import ru.imagebook.client.admin.ctl.delivery.DeliverMessage;
import ru.imagebook.client.admin.ctl.delivery.DeliveryMessages;
import ru.imagebook.client.admin.ctl.delivery.DeliveryPresenter;
import ru.imagebook.client.admin.ctl.delivery.DeliveryView;
import ru.imagebook.client.admin.ctl.delivery.FindOrderMessage;
import ru.imagebook.client.admin.ctl.delivery.RemoveOrdersMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.common.service.delivery.DDeliveryType;
import ru.imagebook.client.common.view.order.OrderViewer;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gwt.DateFormat;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.i18n.lang.ImplodeFunction;
import ru.minogin.core.client.i18n.lang.PrefixFunction;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.client.text.StringUtil;

@Singleton
public class DeliveryViewImpl extends View implements DeliveryView {
    private static final String BILL_ID = "billId";
    private static final String ADDRESS = "addressText";
    private static final String TYPE = "type";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String PRODUCT_NAME = "productName";
    private static final String DELIVERY_TYPE_TEXT = "deliveryTypeText";
    private static final String BILL_QUANTITY = "billQuantity";

    private static final int SCAN_DELAY_MS = 1000;

    private final Widgets widgets;
    private final DeliveryConstants constants;
    private final CoreFactory coreFactory;

    private ListStore<BeanModel<Order<?>>> ordersStore;
    private Map<Integer, ListStore<BeanModel<Order<?>>>> typeStores = new HashMap<Integer, ListStore<BeanModel<Order<?>>>>();
    private Map<Integer, Grid<BeanModel<Order<?>>>> typeGrids = new HashMap<Integer, Grid<BeanModel<Order<?>>>>();
    private TextField<String> numberField;
    private Timer scanTimer;
    private TabPanel buffersTabPanel;
    private ToolBar deliveryToolBar;
    private TextField<String> deliveryField;
    private ToolBar addToolBar;
    private ContentPanel ordersPanel;
    private LayoutContainer container;
    private TextField<String> codeField;
    private ToolBar codeToolBar;
    private final CommonConstants appConstants;
    private LabelToolItem orderNumberTool;
    private Grid<BeanModel<Order<?>>> grid;
    private final OrderViewer orderViewer;
    private DeliveryPresenter presenter;

    @Override
    public void setPresenter(DeliveryPresenter presenter) {
        this.presenter = presenter;
    }

    @Inject
    public DeliveryViewImpl(Dispatcher dispatcher, Widgets widgets, DeliveryConstants constants,
                            CoreFactory coreFactory, CommonConstants appConstants, OrderViewer orderViewer) {
        super(dispatcher);
        this.widgets = widgets;
        this.constants = constants;
        this.coreFactory = coreFactory;
        this.appConstants = appConstants;
        this.orderViewer = orderViewer;
    }

    @Override
    public void showSection(final String sessionId) {
        LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
        desktop.removeAll();

        container = new LayoutContainer(new BorderLayout());

        ordersPanel = new ContentPanel(new FitLayout());
        ordersPanel.setHeading(constants.ordersPanel());
        ordersPanel.setScrollMode(Scroll.AUTO);

        addToolBar = new ToolBar();
        addToolBar.add(new LabelToolItem(constants.numberField() + ":"));
        numberField = new TextField<String>();
        numberField.addListener(Events.KeyPress, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (scanTimer != null)
                    scanTimer.cancel();

                scanTimer = new Timer() {
                    @Override
                    public void run() {
                        String number = numberField.getValue();
                        number = StringUtil.trim(number);

                        send(new AddOrderMessage(number));
                    }
                };
                scanTimer.schedule(SCAN_DELAY_MS);
            }
        });
        addToolBar.add(numberField);
        addToolBar.add(new Button(constants.showFlashButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        final BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
                        if (item != null) {
                            Order<?> order = item.getBean();
                            orderViewer.showOrderPreview(order, sessionId);
                        }
                    }
                }));
        ordersPanel.setTopComponent(addToolBar);

        ordersStore = new ListStore<BeanModel<Order<?>>>();

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig(BILL_ID, constants.billNumberColumn(), 50));
        columns.add(new ColumnConfig(BILL_QUANTITY, constants.billQuantityColumn(),
                50));
        columns.add(new ColumnConfig(Order.NUMBER, constants.numberColumn(), 100));
        columns.add(new ColumnConfig(PRODUCT_NAME, constants.productColumn(), 300));

        ColumnConfig columnConfig = new ColumnConfig(Order.QUANTITY,
                constants.quantityColumn(), 50);
        columnConfig.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(columnConfig);

        columns.add(new ColumnConfig(DELIVERY_TYPE_TEXT, constants
                .deliveryTypeColumn(), 150));

        columns.add(new ColumnConfig(ADDRESS, constants.addressColumn(), 300));
        columns.add(new ColumnConfig(NAME, constants.nameColumn(), 100));
        columns.add(new ColumnConfig(PHONE, constants.phoneColumn(), 100));
        columns.add(new ColumnConfig(Order.DELIVERY_COMMENT, constants
                .deliveryCommentColumn(), 200));

        grid = new Grid<BeanModel<Order<?>>>(ordersStore, new ColumnModel(columns));
        grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
                if (item != null) {
                    Order<?> order = item.getBean();
                    send(new AddOrderMessage(order.getNumber()));
                }
            }
        });
        ordersPanel.add(grid);

        container.add(ordersPanel, new BorderLayoutData(LayoutRegion.CENTER, 0.5f));

        ContentPanel buffersPanel = new ContentPanel(new RowLayout());
        buffersPanel.setHeading(constants.buffersPanel());

        ToolBar toolBar = new ToolBar();
        buffersPanel.add(toolBar, new RowData(1, -1));

        toolBar.add(new Button(constants.print(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        TabItem selectedItem = buffersTabPanel.getSelectedItem();
                        Integer type = selectedItem.getData(TYPE);
                        Window.open(GWT.getHostPageBaseURL() + "delivery?a=" + sessionId + "&b=" + type, null, null);
                    }
                }));

        toolBar.add(new Button(constants.createExcel(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        presenter.onCreateExcelButtonClick();
                    }
                }));

        toolBar.add(new Button(constants.createBarcodes(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        presenter.onCreateBarcodesButtonClick();
                    }
                }));

        ToggleButton deliveryButton = new ToggleButton(constants.deliveryButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        send(DeliveryMessages.TOGGLE_MODE);
                    }
                });
        toolBar.add(deliveryButton);

        toolBar.add(new Button(constants.removeButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        TabItem selectedItem = buffersTabPanel.getSelectedItem();
                        Integer type = (Integer) selectedItem.getData(TYPE);
                        Grid<BeanModel<Order<?>>> typeGrid = typeGrids.get(type);
                        List<BeanModel<Order<?>>> selectedItems = typeGrid
                                .getSelectionModel().getSelectedItems();
                        List<Integer> orderIds = new ArrayList<Integer>();
                        for (BeanModel<Order<?>> model : selectedItems) {
                            orderIds.add(model.getBean().getId());
                        }
                        send(new RemoveOrdersMessage(orderIds));
                    }
                }));

        deliveryToolBar = new ToolBar();
        deliveryToolBar.add(new LabelToolItem(constants.deliveryField() + ":"));
        deliveryField = new TextField<String>();
        deliveryField.addListener(Events.KeyPress, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (scanTimer != null)
                    scanTimer.cancel();

                scanTimer = new Timer() {
                    @Override
                    public void run() {
                        String number = deliveryField.getValue();
                        number = StringUtil.trim(number);
                        send(new FindOrderMessage(number));
                    }
                };
                scanTimer.schedule(SCAN_DELAY_MS);
            }
        });
        deliveryToolBar.add(deliveryField);
        deliveryToolBar.hide();
        buffersPanel.add(deliveryToolBar, new RowData(1, -1));

        codeToolBar = new ToolBar();
        orderNumberTool = new LabelToolItem();
        codeToolBar.add(orderNumberTool);
        codeToolBar.add(new LabelToolItem(" - " + constants.codeField() + ":"));

        codeField = new TextField<String>();
        codeField.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == 13)
                    send(new DeliverMessage(codeField.getValue()));
            }
        });

        codeToolBar.add(codeField);
        codeToolBar.add(new Button(constants.deliverButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        send(new DeliverMessage(codeField.getValue()));
                    }
                }));
        codeToolBar.add(new Button(appConstants.cancel(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        send(DeliveryMessages.CANCEL_DELIVERY);
                    }
                }));
        codeToolBar.hide();
        buffersPanel.add(codeToolBar, new RowData(1, -1));

        buffersTabPanel = new TabPanel();

        List<Integer> types = new ArrayList<Integer>(DeliveryType.values.keySet());
        types.add(null);
        for (final Integer type : types) {
            String name;
            if (type != null)
                name = DeliveryType.values.get(type);
            else
                name = "Тип доставки не указан";
            TabItem tabItem = new TabItem(name);
            tabItem.setData(TYPE, type);
            tabItem.setLayout(new FitLayout());
            tabItem.setScrollMode(Scroll.AUTO);

            List<ColumnConfig> typeColumns = new ArrayList<ColumnConfig>();

            typeColumns.add(new ColumnConfig(BILL_ID, constants.billNumberColumn(), 100));
            typeColumns.add(new ColumnConfig(BILL_QUANTITY, constants.billQuantityColumn(), 170));
            typeColumns.add(new ColumnConfig(Order.NUMBER, constants.numberColumn(), 100));

            if (type != null) {
                if (type == DeliveryType.MULTISHIP) {
                    typeColumns.add(new ColumnConfig(Bill.MULTISHIP_ORDER_ID, constants.multishipOrderIdColumn(), 100));
                    typeColumns.add(new ColumnConfig(Bill.MULTISHIP_DELIVERY_SERVICE,
                            constants.multishipDeliveryServiceColumn(), 200));
                    typeColumns.add(
                            new ColumnConfig(Bill.ORIENT_DELIVERY_DATE, constants.orientDeliveryDateColumn(), 200));
                }

                if (type == DeliveryType.MAJOR) {
                    typeColumns.add(new ColumnConfig(Bill.DELIVERY_TIME, constants.deliveryTimeColumn(), 150));
                }

                if (type == DeliveryType.DDELIVERY) {
                    typeColumns.add(new ColumnConfig(Bill.DS_SENDING_ID, constants.ddeliveryOrderIdColumn(), 150));
                    typeColumns.add(new ColumnConfig(Bill.DDELIVERY_TYPE, constants.ddeliveryTypeColumn(), 100));
                    typeColumns.add(new ColumnConfig(Bill.DDELIVERY_COMPANY_NAME, constants.ddeliveryServiceColumn(), 200));
                    typeColumns.add(new ColumnConfig(Bill.DELIVERY_TIME, constants.deliveryTimeColumn(), 150));
                }

                if (type == DeliveryType.SDEK) {
                    // TODO add SDEK Columns
                }
            }

            typeColumns.add(new ColumnConfig(PRODUCT_NAME, constants.productColumn(), 300));

            columnConfig = new ColumnConfig(Order.QUANTITY, constants.quantityColumn(), 90);
            columnConfig.setAlignment(HorizontalAlignment.RIGHT);
            typeColumns.add(columnConfig);

            typeColumns.add(new ColumnConfig(ADDRESS, constants.addressColumn(), 300));
            typeColumns.add(new ColumnConfig(NAME, constants.nameColumn(), 100));
            typeColumns.add(new ColumnConfig(PHONE, constants.phoneColumn(), 100));
            typeColumns.add(new ColumnConfig(Order.DELIVERY_COMMENT, constants.deliveryCommentColumn(), 200));

            ListStore<BeanModel<Order<?>>> typeStore = new ListStore<BeanModel<Order<?>>>();
            typeStores.put(type, typeStore);

            final Grid<BeanModel<Order<?>>> typeGrid = new Grid<BeanModel<Order<?>>>(
                    typeStore, new ColumnModel(typeColumns));
            typeGrid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    BeanModel<Order<?>> item = typeGrid.getSelectionModel()
                            .getSelectedItem();
                    if (item != null) {
                        Order<?> order = item.getBean();
                        send(new FindOrderMessage(order.getNumber()));
                    }
                }
            });
            tabItem.add(typeGrid, new RowData(1, 1));
            typeGrids.put(type, typeGrid);

            buffersTabPanel.add(tabItem);
        }

        buffersPanel.add(buffersTabPanel, new RowData(1, 1));

        container.add(buffersPanel, new BorderLayoutData(LayoutRegion.SOUTH, 0.5f));

        container.addListener(Events.Orphan, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                send(DeliveryMessages.SECTION_CLOSED);
            }
        });

        desktop.add(container);
        desktop.layout();
    }

    @Override
    public void showOrders(List<Order<?>> orders, String locale) {
        ordersStore.removeAll();

        Compiler compiler = coreFactory.createCompiler();
        compiler.registerFunction("implode", new ImplodeFunction());
        compiler.registerFunction("prefix", new PrefixFunction());
        String addressTemplate = constants.addressTemplate();

        for (Order<?> order : orders) {
            BeanModel<Order<?>> model = new BeanModel<Order<?>>(order);

            Bill bill = order.getBill();
            if (bill != null) {
                model.set(BILL_ID, bill.getId());

                int billQuantity = 0;
                for (Order<?> billOrder : bill.getOrders()) {
                    billQuantity += billOrder.getQuantity();
                }
                model.set(BILL_QUANTITY, billQuantity);
            }

            model.set(PRODUCT_NAME,
                    order.getProduct().getName().getNonEmptyValue(locale));

            Integer deliveryType = order.getDeliveryType();
            if (deliveryType != null)
                order.setTransient(DELIVERY_TYPE_TEXT,
                        DeliveryType.values.get(deliveryType));

            Address address = order.getAddress();
            if (address != null) {
                model.set(ADDRESS, compiler.compile(addressTemplate, address));
                model.set(NAME, address.getFullName());
                model.set(PHONE, address.getPhone());
            }
            ordersStore.add(model);
        }

        numberField.clear();
    }

    @Override
    public void showTypeOrders(Integer deliveryType, List<Order<?>> orders,
                               String locale) {
        ListStore<BeanModel<Order<?>>> store = typeStores.get(deliveryType);
        store.removeAll();

        Compiler compiler = coreFactory.createCompiler();
        compiler.registerFunction("implode", new ImplodeFunction());
        compiler.registerFunction("prefix", new PrefixFunction());
        String addressTemplate = constants.addressTemplate();

        for (Order<?> order : orders) {
            BeanModel<Order<?>> model = new BeanModel<Order<?>>(order);

            Bill bill = order.getBill();
            if (bill != null) {
                model.set(BILL_ID, bill.getId());

                int billQuantity = 0;
                for (Order<?> billOrder : bill.getOrders()) {
                    billQuantity += billOrder.getQuantity();
                }
                model.set(BILL_QUANTITY, billQuantity);
                model.set(Bill.MULTISHIP_ORDER_ID, bill.getMultishipOrderId());
                model.set(Bill.MULTISHIP_DELIVERY_SERVICE, bill.getMshDeliveryService());
                model.set(Bill.DS_SENDING_ID, bill.getDsSendingId());
                model.set(Bill.DDELIVERY_TYPE,
                        (DDeliveryType.DDELIVERY_COURIER.name().equals(bill.getDdeliveryType())
                                ? constants.ddeliveryTypeCourier()
                                : constants.ddeliveryTypePickup()));
                model.set(Bill.DDELIVERY_COMPANY_NAME, bill.getDdeliveryCompanyName());
                model.set(Bill.ORIENT_DELIVERY_DATE, DateFormat.formatDate(bill.getOrientDeliveryDate()));
                model.set(Bill.DELIVERY_TIME, bill.getDeliveryTime());
            }

            model.set(PRODUCT_NAME,
                    order.getProduct().getName().getNonEmptyValue(locale));

            Address address = order.getAddress();
            if (address != null) {
                model.set(ADDRESS, compiler.compile(addressTemplate, address));
                model.set(NAME, address.getFullName());
                model.set(PHONE, address.getPhone());
            }
            store.add(model);
        }
    }

    @Override
    public void focusAddField() {
        numberField.focus();
    }

    @Override
    public void showDeliveryMode() {
        addToolBar.hide();
        deliveryToolBar.show();
        codeToolBar.hide();
        deliveryField.clear();
    }

    @Override
    public void focusDeliveryField() {
        deliveryField.focus();
    }

    @Override
    public void showAddMode() {
        deliveryToolBar.hide();
        codeToolBar.hide();
        addToolBar.show();
    }

    @Override
    public void showCodeForm(Order<?> order) {
        orderNumberTool.setLabel(order.getNumber());
        deliveryToolBar.hide();
        codeToolBar.show();
        codeField.clear();
        codeField.focus();
    }

    @Override
    public void download(String url) {
        Window.open(url, null, null);
    }
}
