package ru.imagebook.client.app.view.order;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.AlertCallback;
import ru.imagebook.client.app.ctl.order.OrderAction;
import ru.imagebook.client.app.ctl.order.OrderActionPresenter;
import ru.imagebook.client.app.ctl.order.OrderActionPresenterFactory;
import ru.imagebook.client.app.ctl.order.OrderPresenter;
import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.client.app.view.common.OrderViewer;
import ru.imagebook.client.app.view.common.PopoverAnchor;
import ru.imagebook.client.app.view.common.XCheckBox;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.client.common.view.order.OrderConstants;
import ru.imagebook.shared.model.*;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gwt.DateFormat;
import ru.minogin.core.client.text.StringUtil;

import java.util.*;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_RIGHT;
import static com.google.gwt.user.client.ui.HasVerticalAlignment.ALIGN_MIDDLE;
import static ru.imagebook.shared.model.OrderState.*;


@Singleton
public class OrderViewImpl implements OrderView {
    interface OrderingUiBinder extends UiBinder<Widget, OrderViewImpl> {
    }

    private static OrderingUiBinder uiBinder = GWT.create(OrderingUiBinder.class);
    private static final int KEY_UP_DELAY_MS = 500;

//    @UiField
//    Button createAlbumButton;
    @UiField
    Button orderSelectedOrdersButton;
    @UiField
    Button deleteSelectedOrdersButton;
    @UiField
    HTMLPanel incomingOrdersPanelBody;
    @UiField
    FlexTable incomingOrdersTable;
    @UiField
    Button deleteBasketSelectedOrdersButton;
    @UiField
    HTMLPanel basketOrdersPanelBody;
    @UiField
    FlexTable basketOrdersTable;
    @UiField
    Button submitOrderButton;

    @UiField
    OrderEditModalForm orderEditModalForm;
    @UiField
    OrderBonusCodeModalForm orderBonusCodeModalForm;
    @UiField
    OrderSelectedOrdersModalForm orderSelectedOrdersModalForm;
    @UiField
    CreateAlbumForm createAlbumForm;

    private final OrderConstants orderConstants;
    private final CommonConstants appConstants;
    private final OrderViewer orderViewer;
    private final OrderActionPresenterFactory orderActionPresenterFactory;
    private final VendorService vendorService;

    private OrderPresenter presenter;

    private final Set<Order<?>> selectedIncomingOrders;
    private final Set<Order<?>> selectedBasketOrders;


    @Inject
    public OrderViewImpl(OrderConstants orderConstants, CommonConstants appConstants, OrderViewer orderViewer,
                         OrderActionPresenterFactory orderActionPresenterFactory, VendorService vendorService) {
        this.orderConstants = orderConstants;
        this.appConstants = appConstants;
        this.orderViewer = orderViewer;
        this.orderActionPresenterFactory = orderActionPresenterFactory;
        this.vendorService = vendorService;
        this.selectedIncomingOrders = new HashSet<Order<?>>();
        this.selectedBasketOrders = new HashSet<Order<?>>();
    }

    @Override
    public void setPresenter(OrderPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        Widget ui = uiBinder.createAndBindUi(this);
        return ui;
    }

    private void showGroupOperationsButtons(boolean value) {
        orderSelectedOrdersButton.setVisible(value);
        deleteSelectedOrdersButton.setVisible(value);
    }

    private void hideGroupOperationsButtons() {
        selectedIncomingOrders.clear();
        showGroupOperationsButtons(false);
    }

//    @Override
//    public void showCreateAlbumButton(boolean show) {
//        createAlbumButton.setVisible(show);
//    }

    @Override
    public void showIncomingOrders(final List<Order<?>> incomingOrders, String locale) {
        hideGroupOperationsButtons();

        if (!incomingOrders.isEmpty()) {
            incomingOrdersTable.removeAllRows();

            // columns
            int col = 0;

            final XCheckBox checkAllCheckBox = new XCheckBox();
            incomingOrdersTable.getCellFormatter().setWidth(0, col, "40");
            incomingOrdersTable.getCellFormatter().setHorizontalAlignment(0, col, ALIGN_CENTER);
            incomingOrdersTable.getCellFormatter().setVerticalAlignment(0, col, ALIGN_MIDDLE);
            incomingOrdersTable.setWidget(0, col++, checkAllCheckBox);
            checkAllCheckBox.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    for (int i = 1; i < incomingOrdersTable.getRowCount(); i++) {
                        XCheckBox xCheckBox = (XCheckBox) incomingOrdersTable.getWidget(i, 0);
                        xCheckBox.setValue(checkAllCheckBox.getValue());
                    }
                    if (checkAllCheckBox.getValue()) {
                        selectedIncomingOrders.addAll(incomingOrders);
                        showGroupOperationsButtons(true);
                    } else {
                        hideGroupOperationsButtons();
                    }
                }
            });


            incomingOrdersTable.setText(0, col++, orderConstants.order());
            incomingOrdersTable.setText(0, col++, orderConstants.format());
            incomingOrdersTable.setText(0, col++, orderConstants.priceWoDiscount());
            incomingOrdersTable.setText(0, col++, orderConstants.date());
            incomingOrdersTable.setText(0, col, orderConstants.action());

            for (int i = 0; i <= col; i++) {
                incomingOrdersTable.getCellFormatter().addStyleName(0, i, "th");
            }

            // data
            int row = 1;
            for (final Order<?> order : incomingOrders) {
                Integer state = order.getState();

                col = 0;

                final XCheckBox checkBox = new XCheckBox();
                checkBox.addStyleName("order-info-table");
                incomingOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_CENTER);
                incomingOrdersTable.getCellFormatter().setVerticalAlignment(row, col, ALIGN_MIDDLE);
                incomingOrdersTable.setWidget(row, col, checkBox);
                checkBox.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        if (checkBox.getValue()) {
                            selectedIncomingOrders.add(order);
                        } else {
                            selectedIncomingOrders.remove(order);
                        }
                        if (selectedIncomingOrders.size() > 0) {
                            showGroupOperationsButtons(true);
                        } else {
                            showGroupOperationsButtons(false);
                        }
                        if (selectedIncomingOrders.size() == incomingOrders.size()) {
                            checkAllCheckBox.setValue(true);
                        } else {
                            checkAllCheckBox.setValue(false);
                        }
                    }
                });

                col++;

                VerticalPanel orderPanel = new VerticalPanel();
                orderPanel.addStyleName("order-info-table");
                incomingOrdersTable.setWidget(row, col, orderPanel);

                Label numberLabel = new Label(order.getNumber());
                numberLabel.addStyleName("number-label");
                orderPanel.add(numberLabel);

                if (state == FLASH_GENERATION || state == JPEG_GENERATION || state == JPEG_ONLINE_GENERATION
                        || state == JPEG_BOOK_GENERATION || state == OLD_VERSION) {
                    incomingOrdersTable.getRowFormatter().addStyleName(row, "order-moderation");
                    orderPanel.add(new HTML(orderConstants.flashGeneration()));
                    addOrderPreviewWidget(orderPanel, order);
                } else if (state == FLASH_GENERATED) {
                    incomingOrdersTable.getRowFormatter().addStyleName(row, "order-accepted");
                    addOrderPreviewWidget(orderPanel, order);
                    addMoveToBasketForm(orderPanel, order);
                } else if (state == JPEG_GENERATION_ERROR || state == FLASH_REGENERATION_ERROR) {
                    orderPanel.add(new HTML(orderConstants.flashGenerationError()));
                    incomingOrdersTable.getRowFormatter().addStyleName(row, "order-error");
                    addOrderPreviewWidget(orderPanel, order);
                } else if (state == NEW) {
                    orderPanel.add(new HTML(orderConstants.newState()));
                    incomingOrdersTable.getRowFormatter().addStyleName(row, "order-new");
                    addOrderPreviewWidget(orderPanel, order);
                } else if (state == REJECTED) {
                    addOrderPreviewWidget(orderPanel, order);

                    PopoverAnchor anchor = new PopoverAnchor(orderConstants.rejected());
                    anchor.setTitle(orderConstants.rejectCommentBoxTitle());
                    anchor.setContent(Strings.nullToEmpty(StringUtil.nlToBr(order.getRejectComment())));
                    incomingOrdersTable.getRowFormatter().addStyleName(row, "order-rejected");
                    orderPanel.add(anchor);
                }
                col++;

                incomingOrdersTable.setText(row, col++, order.getProduct().getName().getNonEmptyValue(locale));

                String priceText = order.isTrial() ? orderConstants.trialPriceText() : order.getPrice() + "";
                incomingOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
                incomingOrdersTable.setText(row, col++, priceText);

                incomingOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
                incomingOrdersTable.setText(row, col++, DateFormat.formatDate(order.getDate()));

                incomingOrdersTable.setWidget(row, col, createOrderActionsPanel(order));

                row++;
            }

            incomingOrdersPanelBody.setVisible(false);
            incomingOrdersTable.setVisible(true);
        } else {
            incomingOrdersPanelBody.clear();
            incomingOrdersPanelBody.add(new HTML(orderConstants.noOrders()));
            incomingOrdersPanelBody.setVisible(true);
            incomingOrdersTable.setVisible(false);
        }
    }

    private void addMoveToBasketForm(VerticalPanel orderPanel, final Order<?> order) {
        final XCheckBox acceptCheckBox = new XCheckBox(orderConstants.acceptCheckBox());
        orderPanel.add(acceptCheckBox);

        String orderButtonText = order.isTrial() ? orderConstants.orderTrialButton() : orderConstants.moveToBasket();
        final Button orderButton = new Button(orderButtonText);
        orderButton.setStyleName("btn btn-success btn-sm");
        orderButton.setEnabled(false);
        orderPanel.add(orderButton);

        acceptCheckBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                orderButton.setEnabled(acceptCheckBox.getValue());
            }
        });

        orderButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.onOrderButtonClicked(order);
            }
        });
    }

    private Widget createOrderActionsPanel(Order<?> order) {
        OrderActionPresenter orderActionPresenter = orderActionPresenterFactory.create(order);

        // add actions
        if (showProcessOrderButton(order)) {
            orderActionPresenter.addOrderAction(OrderAction.PROCESS);
        }
        if (showEditOrderButton(order)) {
            orderActionPresenter.addOrderAction(OrderAction.EDIT);
        }
        if (order.isExternalOrder() || order.isOnlineEditorOrder()) {
            orderActionPresenter.addOrderAction(OrderAction.COPY);
        }
        Vendor vendor = vendorService.getVendor();
        if (vendor.getType() == VendorType.IMAGEBOOK
                && order.getState() >= FLASH_GENERATED
                && !FLASH_JPEG_GENERATION_STATES.contains(order.getState())
                && order.getState() != JPEG_GENERATION_ERROR) {
            orderActionPresenter.addOrderAction(OrderAction.PUBLISH);
        }
        orderActionPresenter.addOrderAction(OrderAction.DELETE);

        // add listener for all actions
        orderActionPresenter.addOrderActionListener(EnumSet.allOf(OrderAction.class),
                new OrderActionPresenter.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        presenter.loadIncomingOrders();
                    }
                });

        return orderActionPresenter.show();
    }

    private boolean showProcessOrderButton(Order<?> order) {
        boolean pickbook = order.isExternalOrder() &&
                (order.getState() == NEW
                        || order.getState() == JPEG_GENERATION_ERROR
                        || order.getState() == FLASH_REGENERATION_ERROR);

        boolean book = order.getType() == OrderType.BOOK &&
                (order.getState() == NEW
                        || order.getState() == JPEG_GENERATION_ERROR
                        || order.getState() == FLASH_REGENERATION_ERROR
                );

        return pickbook || book;
    }

    private boolean showEditOrderButton(Order<?> order) {
        return (order.isExternalOrder() || order.isEditorOrder() || order.getType() == OrderType.BOOK)
                && (order.getState() == NEW || order.getState() == FLASH_GENERATED);
    }

    private void addOrderPreviewWidget(VerticalPanel orderPanel, Order<?> order) {
        if (order.isExternalOrder()
                || order.getState() == FLASH_GENERATED || order.getState() == OrderState.BASKET) {
            orderPanel.add(orderViewer.createOrderPreviewWidget(order));
        }
    }

//    @UiHandler("createAlbumButton")
//    public void onCreateAlbumButtonClicked(ClickEvent event) {
//        presenter.createAlbumButtonClicked();
//    }

    @UiHandler("orderSelectedOrdersButton")
    public void onOrderSelectedOrdersButtonClicked(ClickEvent event) {
        orderSelectedOrdersModalForm.show();
    }

    @UiHandler("deleteSelectedOrdersButton")
    public void onDeleteSelectedOrdersButtonClicked(ClickEvent event) {
        Bootbox.Dialog.create()
                .setMessage(orderConstants.confirmOrdersDeletion())
                .setCloseButton(false)
                .setTitle(appConstants.deletionConfirmation())
                .addButton(appConstants.ok(), "btn-primary", new AlertCallback() {
                    @Override
                    public void callback() {
                        presenter.deleteOrders(selectedIncomingOrders);
                    }
                })
                .addButton(appConstants.cancel(), "btn-default", new AlertCallback() {
                    @Override
                    public void callback() {
                    }
                })
                .show();
    }

    @UiHandler("orderSelectedOrdersModalForm")
    public void onOrderSelectedOrders(OrderSelectedOrdersEvent event) {
        presenter.moveOrdersToBasket(selectedIncomingOrders);
        orderSelectedOrdersModalForm.hide();
    }

    private void hideDeleteBasketSelectedOrdersButton() {
        selectedBasketOrders.clear();
        deleteBasketSelectedOrdersButton.setVisible(false);
    }

    @Override
    public void showBasketOrders(final List<Order<?>> basketOrders, String locale,
                                 final List<Flyleaf> flyleafs, final List<Vellum> vellums) {
        hideDeleteBasketSelectedOrdersButton();

        if (!basketOrders.isEmpty()) {
            basketOrdersTable.removeAllRows();

            // columns
            int col = 0;

            final XCheckBox checkAllCheckBox = new XCheckBox();
            basketOrdersTable.getCellFormatter().setWidth(0, col, "40");
            basketOrdersTable.getCellFormatter().setHorizontalAlignment(0, col, ALIGN_CENTER);
            basketOrdersTable.getCellFormatter().setVerticalAlignment(0, col, ALIGN_MIDDLE);
            basketOrdersTable.setWidget(0, col++, checkAllCheckBox);
            checkAllCheckBox.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    for (int i = 1; i < basketOrdersTable.getRowCount() - 1; i++) {
                        XCheckBox xCheckBox = (XCheckBox) basketOrdersTable.getWidget(i, 0);
                        xCheckBox.setValue(checkAllCheckBox.getValue());
                    }
                    if (checkAllCheckBox.getValue()) {
                        selectedBasketOrders.addAll(basketOrders);
                        deleteBasketSelectedOrdersButton.setVisible(true);
                    } else {
                        hideDeleteBasketSelectedOrdersButton();
                    }
                }
            });

            basketOrdersTable.setText(0, col++, orderConstants.order());
            basketOrdersTable.setText(0, col++, orderConstants.format());
            basketOrdersTable.setText(0, col++, orderConstants.code());
            basketOrdersTable.setText(0, col++, orderConstants.price());
            basketOrdersTable.setText(0, col++, orderConstants.quantity());
            basketOrdersTable.setText(0, col++, orderConstants.costWoDiscount());
            basketOrdersTable.setText(0, col++, orderConstants.discount());
            basketOrdersTable.setText(0, col++, orderConstants.cost());
            basketOrdersTable.setText(0, col++, orderConstants.date());
            basketOrdersTable.setText(0, col, orderConstants.action());

            for (int i = 0; i <= col; i++) {
                basketOrdersTable.getCellFormatter().addStyleName(0, i, "th");
            }

            // data
            int total = 0;
            int totalDiscount = 0;
            int totalWoDiscount = 0;
            int row = 1;
            for (final Order<?> order : basketOrders) {
                basketOrdersTable.getRowFormatter().addStyleName(row, "order-accepted");

                col = 0;

                final XCheckBox checkBox = new XCheckBox();
                checkBox.addStyleName("order-info-table");
                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_CENTER);
                basketOrdersTable.getCellFormatter().setVerticalAlignment(row, col, ALIGN_MIDDLE);
                basketOrdersTable.setWidget(row, col, checkBox);
                checkBox.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        if (checkBox.getValue()) {
                            selectedBasketOrders.add(order);
                        } else {
                            selectedBasketOrders.remove(order);
                        }
                        if (selectedBasketOrders.size() > 0) {
                            deleteBasketSelectedOrdersButton.setVisible(true);
                        } else {
                            deleteBasketSelectedOrdersButton.setVisible(false);
                        }
                        if (selectedBasketOrders.size() == basketOrders.size()) {
                            checkAllCheckBox.setValue(true);
                        } else {
                            checkAllCheckBox.setValue(false);
                        }
                    }
                });
                col++;

                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_CENTER);

                VerticalPanel orderPanel = new VerticalPanel();
                orderPanel.addStyleName("order-info-table");
                basketOrdersTable.setWidget(row, col, orderPanel);

                Label numberLabel = new Label(order.getNumber());
                numberLabel.addStyleName("number-label");
                orderPanel.add(numberLabel);

                addOrderPreviewWidget(orderPanel, order);
                col++;

                final VerticalPanel namePanel = new VerticalPanel();
                namePanel.addStyleName("order-name");
                namePanel.add(new HTML(getAlbumName(order, locale)));
                Product product = order.getProduct();
                if (product.getColorRange().size() > 1 || product.getCoverLamRange().size() > 1
                        || product.getPageLamRange().size() > 1) {
                    Anchor anchor = new Anchor(orderConstants.editParamsAnchor());
                    anchor.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            presenter.editOrder(order);
                        }
                    });
                    namePanel.add(anchor);
                }


                Album album = (Album) product;
                if (album.isFlyleafs()) {
                    //TODO move to separate component
                    final Flyleaf orderFlyleaf = order.getFlyleaf();

                    final VerticalPanel flyleafPanel = new VerticalPanel();
                    namePanel.add(flyleafPanel);

                    final VerticalPanel flyleafInfoPanel = new VerticalPanel();
                    Widget info = getFlyleafInfo(orderFlyleaf);
                    flyleafInfoPanel.add(info);

                    Anchor anchor = new Anchor("Изменить форзац");
                    anchor.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final VerticalPanel flyleafEditPanel = new VerticalPanel();

                            final Map<Flyleaf, RadioButton> buttons = new HashMap<Flyleaf, RadioButton>();

                            Map<Flyleaf, Integer> flyleafPrices = presenter.computeFlyleafsPrices(order, flyleafs);
                            for (Map.Entry<Flyleaf, Integer> entry : flyleafPrices.entrySet()) {
                                Flyleaf flyleaf = entry.getKey();

                                HorizontalPanel hPanel = new HorizontalPanel();

                                RadioButton button = new RadioButton("flyleaf");
                                buttons.put(flyleaf, button);
                                if (flyleaf.equals(order.getFlyleaf()))
                                    button.setValue(true);
                                hPanel.add(button);

                                hPanel.add(getFlyleafBox(flyleaf));
                                hPanel.add(new Label(flyleaf.getName()));
                                if (!flyleaf.getId().equals(Flyleaf.DEFAULT_ID))
                                    hPanel.add(new Label(" - " + entry.getValue() + " руб."));
                                flyleafEditPanel.add(hPanel);
                            }

                            HorizontalPanel hPanel = new HorizontalPanel();
                            hPanel.add(new Button("Сохранить", new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event1) {
                                    for (Flyleaf flyleaf : flyleafs) {
                                        if (buttons.get(flyleaf).getValue()) {
                                            presenter.setFlyleaf(order, flyleaf);
                                            break;
                                        }
                                    }
                                }
                            }));
                            hPanel.add(new Button("Отменить", new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    flyleafInfoPanel.setVisible(true);
                                    flyleafEditPanel.removeFromParent();
                                }
                            }));
                            flyleafEditPanel.add(hPanel);

                            flyleafInfoPanel.setVisible(false);
                            flyleafPanel.add(flyleafEditPanel);

                        }
                    });
                    flyleafInfoPanel.add(anchor);

                    flyleafPanel.add(flyleafInfoPanel);
                }

                if (album.isSupportsVellum()) {
                    //TODO move to separate component
                    final Vellum orderVellum = order.getVellum();

                    final VerticalPanel vellumPanel = new VerticalPanel();
                    namePanel.add(vellumPanel);

                    final VerticalPanel vellumInfoPanel = new VerticalPanel();
                    Widget info = getVellumInfo(orderVellum);
                    vellumInfoPanel.add(info);

                    Anchor anchor = new Anchor("Выбрать кальку");
                    anchor.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final VerticalPanel vellumEditPanel = new VerticalPanel();

                            final Map<Vellum, RadioButton> buttons = new HashMap<Vellum, RadioButton>();

                            HorizontalPanel hPanel = new HorizontalPanel();
                            RadioButton button = new RadioButton("vellum");
                            if (order.getVellum() == null) {
                                button.setValue(true);
                            }
                            hPanel.add(button);
                            hPanel.add(new Label("Без кальки"));
                            vellumEditPanel.add(hPanel);

                            Map<Vellum, Integer> vellumsPrices = presenter.computeVellumsPrices(order, vellums);
                            for (Map.Entry<Vellum, Integer> entry : vellumsPrices.entrySet()) {
                                Vellum vellum = entry.getKey();

                                hPanel = new HorizontalPanel();

                                button = new RadioButton("vellum");
                                buttons.put(vellum, button);
                                if (vellum.equals(order.getVellum())) {
                                    button.setValue(true);
                                }
                                hPanel.add(button);

                                hPanel.add(getVellumBox(vellum));
                                hPanel.add(new Label(vellum.getName()));
                                hPanel.add(new Label(" - " + entry.getValue() + " руб."));
                                vellumEditPanel.add(hPanel);
                            }

                            hPanel = new HorizontalPanel();
                            hPanel.add(new Button("Сохранить", new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event1) {
                                    Vellum selectedVellum = null;
                                    for (Vellum vellum: vellums) {
                                        if (buttons.get(vellum).getValue()) {
                                            selectedVellum = vellum;
                                            break;
                                        }
                                    }
                                    presenter.setVellum(order, selectedVellum);
                                }
                            }));
                            hPanel.add(new Button("Отменить", new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    vellumInfoPanel.setVisible(true);
                                    vellumEditPanel.removeFromParent();
                                }
                            }));
                            vellumEditPanel.add(hPanel);

                            vellumInfoPanel.setVisible(false);
                            vellumPanel.add(vellumEditPanel);
                        }
                    });
                    vellumInfoPanel.add(anchor);

                    vellumPanel.add(vellumInfoPanel);
                }

                basketOrdersTable.setWidget(row, col++, namePanel);

                if (order.getBonusCode() == null) {
                    Anchor anchor = new Anchor(orderConstants.codeLink());
                    anchor.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            presenter.enterBonusCode(order);
                        }
                    });
                    basketOrdersTable.setWidget(row, col++, anchor);
                } else {
                    String text = orderConstants.bonusCode() + ": " + order.getBonusCode().getCode();
                    if (order.getLevel() != 0) {
                        text += "<br/>" + orderConstants.bonusDiscount() + ": " + order.getLevel();
                    }
                    Anchor anchor = new Anchor(SafeHtmlUtils.fromTrustedString(text));
                    anchor.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            presenter.showActionCode(order.getBonusCode().getAction());
                        }
                    });
                    basketOrdersTable.setWidget(row, col++, anchor);
                }

                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
                basketOrdersTable.setText(row, col++, order.getPrice() + "");

                final TextBox quantityField = new TextBox();
                quantityField.setStyleName("form-control");
                quantityField.setWidth("50px");
                quantityField.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                quantityField.setValue(order.getQuantity() + "");
                quantityField.addChangeHandler(new ChangeHandler() {
                    @Override
                    public void onChange(ChangeEvent event) {
                        setOrderQuantity(order, quantityField);
                    }
                });
                quantityField.addKeyUpHandler(new KeyUpHandler() {
                    private Timer quantityTimer;

                    @Override
                    public void onKeyUp(KeyUpEvent event) {
                        if (quantityTimer != null) {
                            quantityTimer.cancel();
                        }

                        quantityTimer = new Timer() {
                            @Override
                            public void run() {
                                setOrderQuantity(order, quantityField);
                            }
                        };
                        quantityTimer.schedule(KEY_UP_DELAY_MS);
                    }
                });
                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
                basketOrdersTable.setWidget(row, col++, quantityField);

                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
                int costWoDiscount = order.getPrice() * order.getQuantity();
                totalWoDiscount += costWoDiscount;
                basketOrdersTable.setText(row, col++, costWoDiscount + "");

                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
                basketOrdersTable.setText(row, col++, order.getDiscount() + "");
                totalDiscount += order.getDiscount();

                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
                basketOrdersTable.setText(row, col++, order.getCost() + "");
                total += order.getCost();

                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
                basketOrdersTable.setText(row, col++, DateFormat.formatDate(order.getDate()));

                Button removeButton = new Button(orderConstants.removeFromBasket(), new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        presenter.removeOrderFromBasket(order);
                    }
                });
                removeButton.setStyleName("btn btn-danger btn-sm");
                basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_CENTER);
                basketOrdersTable.setWidget(row, col, removeButton);

                row++;
            }

            basketOrdersTable.getRowFormatter().addStyleName(row, "table-bottom");
            col = 0;
            col++;
            col++;
            col++;
            col++;
            col++;
            col++;
            basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
            basketOrdersTable.setText(row, col, totalWoDiscount + "");
            col++;
            basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
            basketOrdersTable.setText(row, col, totalDiscount + "");
            col++;
            basketOrdersTable.getCellFormatter().setHorizontalAlignment(row, col, ALIGN_RIGHT);
            basketOrdersTable.setText(row, col, total + "");
            col++;
            basketOrdersTable.setText(row, col, "");
            col++;
            basketOrdersTable.setText(row, col, "");
            basketOrdersPanelBody.setVisible(false);
            basketOrdersTable.setVisible(true);
        } else {
            basketOrdersPanelBody.clear();
            basketOrdersPanelBody.add(new HTML(orderConstants.basketIsEmpty()));
            basketOrdersPanelBody.setVisible(true);
            basketOrdersTable.setVisible(false);
        }
    }

    private Widget getFlyleafInfo(Flyleaf flyleaf) {
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(new Label("Цвет форзаца: "));
        hPanel.add(getFlyleafBox(flyleaf));
        hPanel.add(new Label(flyleaf.getName()));
        return hPanel;
    }

    private HTML getFlyleafBox(final Flyleaf flyleaf) {
        HTML html = new HTML("<div style='background-color: #" + flyleaf.getColorRGB() + "; border: 1px solid black; width: 20px; height: 20px;'></div>");
        html.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                final PopupPanel popup = new PopupPanel();
                popup.setAutoHideEnabled(true);
                popup.setGlassEnabled(true);
                String url = GWT.getHostPageBaseURL() + "flyleafAppImage?flyleafId=" + flyleaf.getId();
                Image image = new Image(url);
                image.getElement().getStyle().setZIndex(10000);
                image.addLoadHandler(new LoadHandler() {
                    @Override
                    public void onLoad(LoadEvent event) {
                        popup.center();
                        popup.setVisible(true);
                    }
                });
                image.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        popup.hide();
                    }
                });
                popup.setWidget(image);
                popup.setVisible(false);
                popup.show();
                popup.getElement().getStyle().setZIndex(10000);
            }
        });
        html.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        return html;
    }

    private Widget getVellumInfo(Vellum vellum) {
        HorizontalPanel hPanel = new HorizontalPanel();
        if (vellum == null) {
            hPanel.add(new Label("Калька: Без кальки"));
        } else {
            hPanel.add(new Label("Калька: "));
            hPanel.add(getVellumBox(vellum));
            hPanel.add(new Label(vellum.getName()));
        }
        return hPanel;
    }

    private HTML getVellumBox(final Vellum vellum) {
        HTML html = new HTML("<div style='background-color: #" + vellum.getColorRGB() + "; border: 1px solid black; width: 20px; height: 20px;'></div>");
        html.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                final PopupPanel popup = new PopupPanel();
                popup.setAutoHideEnabled(true);
                popup.setGlassEnabled(true);
                String url = GWT.getHostPageBaseURL() + "vellumAppImage?vellumId=" + vellum.getId();
                Image image = new Image(url);
                image.getElement().getStyle().setZIndex(10000);
                image.addLoadHandler(new LoadHandler() {
                    @Override
                    public void onLoad(LoadEvent event) {
                        popup.center();
                        popup.setVisible(true);
                    }
                });
                image.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        popup.hide();
                    }
                });
                popup.setWidget(image);
                popup.setVisible(false);
                popup.show();
                popup.getElement().getStyle().setZIndex(10000);
            }
        });
        html.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        return html;
    }

    @UiHandler("deleteBasketSelectedOrdersButton")
    public void onSubmitBasketDeleteSelectedButtonClicked(ClickEvent event) {
        presenter.removeOrdersFromBasket(selectedBasketOrders);
    }

    private String getAlbumName(Order<?> order, String locale) {
        Product product = order.getProduct();
        String name = product.getName().getNonEmptyValue(locale);
        if (product.getColorRange().size() > 1) {
            name += "<br/>" + orderConstants.color() + ": " + order.getColor().getName().get(locale);
        }
        if (product.getCoverLamRange().size() > 1) {
            name += "<br/>" + orderConstants.coverLam() + ": "
                    + CoverLamination.values.get(order.getCoverLamination()).get(locale);
        }
        if (product.getPageLamRange().size() > 1) {
            name += "<br/>" + orderConstants.pageLam() + ": "
                    + PageLamination.values.get(order.getPageLamination()).get(locale);
        }
        return name;
    }

    private void setOrderQuantity(final Order<?> order, final TextBox quantityField) {
        try {
            order.setQuantity(new Integer(quantityField.getValue()));
        }
        catch (Exception e) {
            order.setQuantity(1);
        }
    }

    @Override
    public Button getSubmitOrderButton() {
        return this.submitOrderButton;
    }

    @UiHandler("submitOrderButton")
    public void onSubmitOrderButtonClicked(ClickEvent event) {
        presenter.onSubmitOrderButtonClicked();
    }

    @Override
    public void showSubmitOrderConfirmation() {
        Bootbox.Dialog.create()
                .setTitle(orderConstants.bonusCodeNotSpecified())
                .setMessage(orderConstants.bonusCodeNotSpecifiedConfirm())
                .setCloseButton(false)
                .addButton(appConstants.yes(), "btn-primary", new AlertCallback() {
                    @Override
                    public void callback() {
                        presenter.submitOrder();
                    }
                })
                .addButton(appConstants.no(), "btn-default")
                .show();
    }

    @Override
    public void showOrderEditForm(Order<?> order, List<Color> colors, String locale) {
        orderEditModalForm.show(order, colors, locale);
    }

    @Override
    public void hideOrderEditForm() {
        orderEditModalForm.hide();
    }

    @UiHandler("orderEditModalForm")
    public void onSetOrderParams(SetOrderParamsEvent event) {
        presenter.setOrderParams(event.getOrderId(), event.getColorId(), event.getCoverLam(), event.getPageLam());
    }

    @UiHandler("createAlbumForm")
    public void onCreateAlbum(CreateAlbumEvent event) {
        presenter.createAlbum(event.getProductId(), event.getPageCount());
    }

    @Override
    public void showOrderBonusCodeForm(Order<?> order) {
        orderBonusCodeModalForm.show(order.getId(), vendorService.getVendor());
    }

    @Override
    public void hideOrderBonusCodeForm() {
        orderBonusCodeModalForm.hide();
    }

    @UiHandler("orderBonusCodeModalForm")
    public void onPrepareToApplyCode(PrepareToApplyCodeEvent event) {
        presenter.prepareToApplyBonusCode(event.getOrderId(), event.getCode(), event.getDeactivationCode());
    }

    /**
     * Показать диалог подтверждения применения кода
     */
    @Override
    public void showConfirmActionCodeDialog(final BonusAction action, final Integer orderId, final String code,
                                            final String deactivationCode) {
        buildConfirmActionCodeDialog(action)
                .addButton(orderConstants.confirmActionCodeButton(), "btn-primary", new AlertCallback() {
                    @Override
                    public void callback() {
                        presenter.applyBonusCode(orderId, code, deactivationCode);
                    }
                })
                .addButton(appConstants.cancel(), "btn-default", new AlertCallback() {
                    @Override
                    public void callback() {
                        orderBonusCodeModalForm.setOkButtonEnabled(true);
                    }
                })
                .show();
    }

    /**
     * Показать инфо диалог об установленном коде
     */
    @Override
    public void showConfirmActionCodeDialog(final BonusAction action) {
        buildConfirmActionCodeDialog(action)
                .addButton(appConstants.ok(), "btn-primary")
                .show();
    }

    private Bootbox.Dialog buildConfirmActionCodeDialog(final BonusAction action) {
        Bootbox.Dialog dialog = Bootbox.Dialog.create()
                .setTitle(orderConstants.confirmActionCodeBoxHeading())
                .setCloseButton(false);

        String actionEndDateString = DateFormat.formatDate(action.getDateEnd());
        if (action.getDiscount1() > 0) {
            dialog.setMessage(action.getDateEnd() != null
                    ? orderConstants.activationCodePaymentTypePercentWithDateEnd(action.getDiscount1(), actionEndDateString)
                    : orderConstants.activationCodePaymentTypePercent(action.getDiscount1()));
        } else {
            dialog.setMessage(action.getDateEnd() != null
                    ? orderConstants.activationCodePaymentTypeMoneyWithDateEnd(action.getDiscountSum(), actionEndDateString)
                    : orderConstants.activationCodePaymentTypeMoney(action.getDiscountSum()));
        }

        return dialog;
    }

    @Override
    public void alertIncorrectCode() {
        Notify.error(orderConstants.incorrectCode());
    }

    @Override
    public void alertIncorrectCodePeriod(final Date dateStart, final Date dateEnd) {
        String message = (dateEnd != null
                ? orderConstants.incorrectCodePeriodFromTo(DateFormat.formatDate(dateStart), DateFormat.formatDate(dateEnd))
                : orderConstants.incorrectCodePeriodFrom(DateFormat.formatDate(dateStart)));
        Notify.error(message);
    }

    @Override
    public void alertProductNotAvailable() {
        Notify.error(orderConstants.codeNotAvailableForProduct());
    }

    @Override
    public void alertCodeAlreadyUsed() {
        Notify.error(orderConstants.codeAlreadyUsed());
    }

    @Override
    public void alertOrdersDeleted() {
        Notify.notify(orderConstants.ordersDeletedHeading(), orderConstants.ordersDeletedText());
    }

    @Override
    public void showCreateAlbumForm(Map<Integer, List<Product>> products) {
        createAlbumForm.show(products);
    }
}
