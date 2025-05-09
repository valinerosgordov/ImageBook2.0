package ru.imagebook.client.admin.view.finishing;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.ctl.finishing.FinishOrderMessage;
import ru.imagebook.client.admin.ctl.finishing.FinishingController;
import ru.imagebook.client.admin.ctl.finishing.FinishingMessages;
import ru.imagebook.client.admin.ctl.finishing.FinishingView;
import ru.imagebook.client.admin.ctl.finishing.ShowOrderMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.common.view.order.OrderViewer;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Binding;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.Flyleaf;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Paper;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.Vellum;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.grid.DateColumnConfig;
import ru.minogin.core.client.lang.template.Compiler;

@Singleton
public class FinishingViewImpl extends View implements FinishingView {
    private static final String STATE_TEXT = "stateText";
    private static final String PRODUCT_NAME = "productName";
    private static final String PRODUCT_FORMAT = "productFormat";
    private static final String BINDING_TEXT = "bindingText";
    private static final String COVER_TEXT = "coverText";
    private static final String PAPER_TEXT = "paperText";
    private static final String COVER_LAM_TEXT = "coverLamText";
    private static final String PAGE_LAM_TEXT = "pageLamText";
    private static final String FLYLEAF_HTML = "flyleafHtml";
    private static final String VELLUM_HTML = "vellumHtml";

    private final Widgets widgets;
    private final FinishingConstants constants;
    private final FinishingBundle bundle;
    private final CoreFactory coreFactory;
    private final ru.imagebook.client.admin.view.finishing.FinishingMessages messages;
    private final OrderViewer orderViewer;

    private ListStore<BeanModel<Order<?>>> store;
    private TextField<String> numberField;
    private ContentPanel orderPanel;
    private LabelToolItem ordersLabel;

    @Inject
    public FinishingViewImpl(Dispatcher dispatcher, Widgets widgets, FinishingConstants constants,
                             FinishingBundle bundle, CoreFactory coreFactory, OrderViewer orderViewer,
                             ru.imagebook.client.admin.view.finishing.FinishingMessages messages) {
        super(dispatcher);
        this.widgets = widgets;
        this.constants = constants;
        this.bundle = bundle;
        this.coreFactory = coreFactory;
        this.messages = messages;
        this.orderViewer = orderViewer;
    }

    @Override
    public void showSection() {
        LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
        desktop.removeAll();

        LayoutContainer container = new LayoutContainer(new BorderLayout());

        container.addListener(Events.Orphan, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                send(FinishingMessages.SECTION_CLOSED);
            }
        });

        ContentPanel ordersPanel = new ContentPanel(new FitLayout());
        ordersPanel.setHeading(constants.ordersHeading());

        ToolBar toolBar = new ToolBar();
        LabelToolItem numberLabel = new LabelToolItem(constants.numberField() + ": ");
        toolBar.add(numberLabel);
        numberField = new TextField<String>();
        numberField.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER)
                    send(new ShowOrderMessage(numberField.getValue(), true));
            }
        });
        toolBar.add(numberField);
        ordersPanel.setTopComponent(toolBar);

        store = new ListStore<BeanModel<Order<?>>>();

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new DateColumnConfig(Order.PRINT_DATE, constants.dateColumn(), 80));
        columns.add(new ColumnConfig(Order.NUMBER, constants.numberColumn(), 80));
        columns.add(new ColumnConfig(STATE_TEXT, constants.stateColumn(), 60));
        columns.add(new ColumnConfig(PRODUCT_NAME, constants.productNameColumn(), 200));

        ColumnConfig quantityColumn = new ColumnConfig(Order.QUANTITY, constants.quantityColumn(), 80);
        quantityColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(quantityColumn);

        columns.add(new ColumnConfig(PRODUCT_FORMAT, constants.productFormatColumn(), 150));
        columns.add(new ColumnConfig(BINDING_TEXT, constants.bindingColumn(), 100));
        columns.add(new ColumnConfig(COVER_TEXT, constants.coverColumn(), 150));
        columns.add(new ColumnConfig(PAPER_TEXT, constants.paperColumn(), 80));

        ColumnConfig pageCountColumn = new ColumnConfig(Order.PAGE_COUNT, constants.pageCountColumn(), 100);
        pageCountColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(pageCountColumn);

        columns.add(new ColumnConfig(COVER_LAM_TEXT, constants.coverLamColumn(), 120));
        columns.add(new ColumnConfig(PAGE_LAM_TEXT, constants.pageLamColumn(), 120));
        ColumnConfig flyleafConfig = new ColumnConfig(FLYLEAF_HTML, constants.flyleafColumn(), 120);
        flyleafConfig.setRenderer(new GridCellRenderer<BeanModel<Order<?>>>() {
            @Override
            public Object render(BeanModel<Order<?>> model, String property, ColumnData columnData, int rowIndex,
                                 int colIndex, ListStore<BeanModel<Order<?>>> store, Grid<BeanModel<Order<?>>> grid) {
                String flyleafHtml = model.get(property);
                return new Html(flyleafHtml);
            }
        });
        columns.add(flyleafConfig);

        ColumnConfig vellumConfig = new ColumnConfig(VELLUM_HTML, constants.vellumColumn(), 120);
        vellumConfig.setRenderer(new GridCellRenderer<BeanModel<Order<?>>>() {
            @Override
            public Object render(BeanModel<Order<?>> model, String property, ColumnData columnData, int rowIndex,
                                 int colIndex, ListStore<BeanModel<Order<?>>> store, Grid<BeanModel<Order<?>>> grid) {
                String vellumHtml = model.get(property);
                return new Html(vellumHtml);
            }
        });
        columns.add(vellumConfig);

        final Grid<BeanModel<Order<?>>> grid = new Grid<BeanModel<Order<?>>>(store, new ColumnModel(columns));
        grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                BeanModel<Order<?>> item = grid.getSelectionModel().getSelectedItem();
                if (item != null) {
                    Order<?> order = item.getBean();
                    send(new ShowOrderMessage(order.getNumber(), false));
                }
            }
        });
        grid.getView().setViewConfig(new GridViewConfig() {
            @Override
            public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds) {
                return model.get(FinishingController.STYLE);
            }
        });
        ordersPanel.add(grid);

        ToolBar ordersToolBar = new ToolBar();
        ordersLabel = new LabelToolItem();
        ordersToolBar.add(ordersLabel);
        ordersPanel.setBottomComponent(ordersToolBar);

        container.add(ordersPanel, new BorderLayoutData(LayoutRegion.CENTER, 0.4f));

        orderPanel = new ContentPanel(new RowLayout());
        orderPanel.setScrollMode(Scroll.AUTO);
        orderPanel.setHeading(constants.orderHeading());

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.SOUTH, 0.6f);
        data.setSplit(true);
        container.add(orderPanel, data);

        desktop.add(container);
        desktop.layout();
    }

    @Override
    public void showOrders(List<Order<?>> orders, String locale) {
        store.removeAll();

        for (Order<?> order : orders) {
            Product product = order.getProduct();
            Album album = (Album) product;

            BeanModel<Order<?>> model = new BeanModel<Order<?>>(order);
            model.setTransient(STATE_TEXT, OrderState.values.get(order.getState()).get(locale));
            model.setTransient(PRODUCT_NAME, product.getName().get(locale));
            model.setTransient(PRODUCT_FORMAT, album.getSize());
            model.setTransient(BINDING_TEXT, Binding.values.get(product.getBinding()).get(locale));
            model.setTransient(COVER_TEXT, CoverUtil.getCoverName((AlbumOrder) order, locale));
            model.setTransient(PAPER_TEXT, Paper.values.get(product.getPaper()).get(locale));
            model.setTransient(COVER_LAM_TEXT, CoverLamination.values.get(order.getCoverLamination()).get(locale));
            model.setTransient(PAGE_LAM_TEXT, PageLamination.values.get(order.getPageLamination()).get(locale));
            model.setTransient(FLYLEAF_HTML, getFlyleafHtml(order, album));
            model.setTransient(VELLUM_HTML, getVellumHtml(order, album));

            store.add(model);
        }

        ordersLabel.setLabel(messages.ordersStatus(orders.size()));
    }

    @Override
    public void showOrder(final Order<?> order, String locale, final String sessionId) {
        orderPanel.removeAll();

        HorizontalPanel hPanel = new HorizontalPanel();

        Html orderHtml = new Html();
        orderHtml.setStyleAttribute("margin", "5px");
        hPanel.add(orderHtml);

        String imageUrl = getOrderImageUrl(order, sessionId);
        Image image = new Image(imageUrl);
        image.setStyleName("finishing-preview");
        image.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                orderViewer.showOrderPreview(order, sessionId);
            }
        });

        TableData data = new TableData();
        data.setPadding(10);
        hPanel.add(image, data);

        orderPanel.add(hPanel);

        Button button = new Button(constants.doneButton(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                send(new FinishOrderMessage(order.getId()));
            }
        });
        button.setSize(200, 50);
        button.addStyleName("large-button");
        orderPanel.add(button, new RowData(-1, -1, new Margins(10)));

        String template = bundle.order().getText();
        Compiler compiler = coreFactory.createCompiler();
        Bean context = new BaseBean();

        Product product = order.getProduct();
        Album album = (Album) product;

        order.setTransient(PRODUCT_NAME, product.getName().get(locale));
        order.setTransient(PRODUCT_FORMAT, album.getSize());
        order.setTransient(BINDING_TEXT, Binding.values.get(product.getBinding()).get(locale));
        order.setTransient(COVER_TEXT, CoverUtil.getCoverName((AlbumOrder) order, locale));
        order.setTransient(PAPER_TEXT, Paper.values.get(product.getPaper()).get(locale));
        order.setTransient(COVER_LAM_TEXT, CoverLamination.values.get(order.getCoverLamination()).get(locale));
        order.setTransient(PAGE_LAM_TEXT, PageLamination.values.get(order.getPageLamination()).get(locale));
        order.setTransient(FLYLEAF_HTML, getFlyleafHtml(order, album));
        order.setTransient(VELLUM_HTML, getVellumHtml(order, album));

        context.set("order", order);
        String html = compiler.compile(template, context);
        orderHtml.setHtml(html);

        orderPanel.layout();
    }

    @NotNull
    private String getFlyleafHtml(Order<?> order, Album album) {
        String flyleafHtml = "";
        if (album.isFlyleafs()) {
            Flyleaf flyleaf = order.getFlyleaf();
            if (flyleaf != null) {
                flyleafHtml =
                    "<div style=\"display: inline-block; vertical-align: middle; width: 20px; height: 20px; border: 1px solid gray; background-color: #" + flyleaf.getColorRGB() + "\"></div> " + flyleaf.getInnerName();
            }
        }
        return flyleafHtml;
    }

    @NotNull
    private String getVellumHtml(Order<?> order, Album album) {
        String vellumHtml = "";
        if (album.isSupportsVellum()) {
            Vellum vellum = order.getVellum();
            if (vellum != null) {
                vellumHtml =
                    "<div style=\"display: inline-block; vertical-align: middle; width: 20px; height: 20px; border: 1px solid gray; background-color: #" + vellum.getColorRGB() + "\"></div> " + vellum.getInnerName();
            } else {
                vellumHtml = "Без кальки";
            }
        }
        return vellumHtml;
    }

    private String getOrderImageUrl(Order<?> order, String sessionId) {
        return GWT.getHostPageBaseURL() + "finishingPreview?a=" + order.getId() + "&b=" + sessionId;
    }

    @Override
    public void resetOrder() {
        orderPanel.removeAll();
    }

    @Override
    public void focusNumberField() {
        numberField.focus();
    }

    @Override
    public void resetNumberField() {
        numberField.clear();
    }
}
