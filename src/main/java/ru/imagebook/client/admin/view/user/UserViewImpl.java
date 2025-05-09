package ru.imagebook.client.admin.view.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.LiveGridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LiveToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.ctl.user.AddUserMessage;
import ru.imagebook.client.admin.ctl.user.DeleteUsersMessage;
import ru.imagebook.client.admin.ctl.user.DeleteUsersRequestMessage;
import ru.imagebook.client.admin.ctl.user.LoadUsersMessage;
import ru.imagebook.client.admin.ctl.user.NoVendorNoUsernameUpdateUserMessage;
import ru.imagebook.client.admin.ctl.user.SearchUserMessage;
import ru.imagebook.client.admin.ctl.user.SendInvitationMessage;
import ru.imagebook.client.admin.ctl.user.ShowEditFormMessage;
import ru.imagebook.client.admin.ctl.user.UpdateUserMessage;
import ru.imagebook.client.admin.ctl.user.UserMessages;
import ru.imagebook.client.admin.ctl.user.UserView;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.admin.view.product.ProductConstants;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.common.service.admin.Actions;
import ru.imagebook.client.common.service.order.OrderService;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.client.common.util.i18n.I18n;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.AlbumImpl;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.InvitationState;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAlbumDiscount;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.GxtConstants;
import ru.minogin.core.client.gxt.GxtMessages;
import ru.minogin.core.client.gxt.HintPlugin;
import ru.minogin.core.client.gxt.ListFieldSet;
import ru.minogin.core.client.gxt.SearchField;
import ru.minogin.core.client.gxt.SearchHandler;
import ru.minogin.core.client.gxt.form.EmailField;
import ru.minogin.core.client.gxt.form.IntegerField;
import ru.minogin.core.client.gxt.form.ObjectField;
import ru.minogin.core.client.gxt.form.ObjectFieldCallback;
import ru.minogin.core.client.gxt.form.ObjectModel;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.grid.DateTimeColumnConfig;
import ru.minogin.core.client.gxt.grid.FixedLiveGridView;
import ru.minogin.core.client.gxt.grid.IntegerColumnConfig;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.text.StringUtil;

@Singleton
public class UserViewImpl extends View implements UserView {
    private static final String ACTIVE_TEXT = "activeText";
    private static final String INV_TEXT = "invText";
    private static final String LEVEL = "levelText";
    private static final String VENDOR_TEXT = "vendorText";
    private static final String EMAILS_TEXT = "emailsText";
    private static final String PHONES_TEXT = "phonesText";

    private static final String ARTICLE = "article";
    private static final String NAME_TEXT = "nameText";

    private final CommonConstants appConstants;
    private final UserConstants constants;
    private final ProductConstants productConstants;
    private final Widgets widgets;
    private final GxtConstants xgxtConstants;
    private final GxtMessages xgxtMessages;
    private final SecurityService securityService;
    private final UserService userService;
    private final OrderService orderService;

    protected AsyncCallback<PagingLoadResult<BeanModel<User>>> callback;
    private PagingLoader<PagingLoadResult<BeanModel<User>>> loader;
    private CheckBox activeField;
    private TextField<String> userNameField;
    private TextField<String> passwordField;
    private TextField<String> nameField;
    private TextField<String> lastNameField;
    private TextField<String> surnameField;
    private SelectField<Integer> levelField;
    private CheckBox skipMailingField;
    private ListFieldSet<Email, XEmailField> emailsFieldSet;
    private ListFieldSet<Phone, XPhoneField> phonesFieldSet;
    private ListFieldSet<Address, AddressFieldSet> addressesFieldSet;
    private SelectField<String> localeField;
    private SelectField<Integer> invitationStateField;
    private TextArea infoField;
    private IntegerField editorSourcesStoragePeriodField;
    private Window addUserWindow;
    private Grid<BeanModel<User>> grid;
    private Window editUserWindow;
    private SelectField<Vendor> vendorField;
    private CheckBox urgentOrdersField;
    private CheckBox advOrdersField;
    private CheckBox photographerField;
    private ObjectFieldCallback<Product> productAccessCallback;
    private ObjectFieldCallback<Product> productDiscountCallback;
    private ListStore<BeanModel<Product>> productAccessStore;
    private ListStore<UserAlbumDiscountModel> albumDiscountStore;
    private EditorGrid<UserAlbumDiscountModel> albumDiscountGrid;
    private UserPresenter presenter;

    @Inject
    public UserViewImpl(Dispatcher dispatcher, CommonConstants appConstants, UserConstants constants,
                        ProductConstants productConstants, Widgets widgets, GxtConstants xgxtConstants,
                        GxtMessages xgxtMessages, SecurityService securityService,
                        UserService userService, OrderService orderService) {
        super(dispatcher);
        this.appConstants = appConstants;
        this.constants = constants;
        this.productConstants = productConstants;
        this.widgets = widgets;
        this.xgxtConstants = xgxtConstants;
        this.xgxtMessages = xgxtMessages;
        this.securityService = securityService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public void setPresenter(UserPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void alertNoUsersSelected() {
        MessageBox.alert(appConstants.error(), constants.noUsersSelected(), null);
    }

    @Override
    public void alertInvitationAlreadySent() {
        MessageBox.alert(appConstants.error(), constants.invitationAlreadySent(), null);
    }

    @Override
    public void showUsersSection() {
        LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
        desktop.removeAll();

        ContentPanel panel = new ContentPanel(new FitLayout());
        panel.setHeading(constants.users());

        if (securityService.isAllowed(Actions.USERS_MANAGEMENT)) {
            ToolBar toolBar = new ToolBar();
            if (securityService.isAllowed(Actions.USERS_ADD)) {
                toolBar.add(new Button(appConstants.add(),
                        new SelectionListener<ButtonEvent>() {
                            @Override
                            public void componentSelected(ButtonEvent ce) {
                                send(UserMessages.SHOW_ADD_FORM);
                            }
                        }));
            }
            toolBar.add(new Button(appConstants.edit(),
                    new SelectionListener<ButtonEvent>() {
                        @Override
                        public void componentSelected(ButtonEvent ce) {
                            edit();
                        }
                    }));
            if (securityService.isAllowed(Actions.USERS_DELETE)) {
                toolBar.add(new Button(appConstants.delete(),
                        new SelectionListener<ButtonEvent>() {
                            @Override
                            public void componentSelected(ButtonEvent ce) {
                                List<BeanModel<User>> selectedItems = grid.getSelectionModel()
                                        .getSelectedItems();
                                List<User> users = new ArrayList<User>();
                                for (BeanModel<User> item : selectedItems) {
                                    users.add(item.getBean());
                                }
                                send(new DeleteUsersRequestMessage(users));
                            }
                        }));
            }
            toolBar.add(new Button(constants.sendInvitationButton(),
                    new SelectionListener<ButtonEvent>() {
                        @Override
                        public void componentSelected(ButtonEvent ce) {
                            final List<BeanModel<User>> items = grid.getSelectionModel()
                                    .getSelectedItems();
                            if (!items.isEmpty()) {
                                new ConfirmMessageBox(appConstants.warning(), constants
                                        .sendInvitationConfirmation(), new Listener<BaseEvent>() {
                                    @Override
                                    public void handleEvent(BaseEvent be) {
                                        List<Integer> ids = new ArrayList<Integer>();
                                        for (BeanModel<User> model : items) {
                                            ids.add(model.getBean().getId());
                                        }
                                        send(new SendInvitationMessage(ids));
                                    }
                                });
                            }
                        }
                    }));

            toolBar.add(new FillToolItem());

            final SearchField searchField = new SearchField(new SearchHandler() {
                @Override
                public void onSearch(String query) {
                    send(new SearchUserMessage(query));
                }
            });
            toolBar.add(searchField);

            panel.setTopComponent(toolBar);
        }

        DataProxy<PagingLoadResult<BeanModel<User>>> proxy = new DataProxy<PagingLoadResult<BeanModel<User>>>() {
            @Override
            public void load(DataReader<PagingLoadResult<BeanModel<User>>> reader,
                             Object loadConfig,
                             AsyncCallback<PagingLoadResult<BeanModel<User>>> callback) {
                PagingLoadConfig config = (PagingLoadConfig) loadConfig;
                UserViewImpl.this.callback = callback;
                send(new LoadUsersMessage(config.getOffset(), config.getLimit()));
            }
        };
        loader = new BasePagingLoader<PagingLoadResult<BeanModel<User>>>(proxy);
        ListStore<BeanModel<User>> store = new ListStore<BeanModel<User>>(loader);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new IntegerColumnConfig(User.ID, appConstants.id(), 50));
        columns.add(new DateTimeColumnConfig(User.DATE, constants.dateColumn(), 100));
        columns.add(new ColumnConfig(User.USER_NAME, constants.userName(), 150));
        columns.add(new ColumnConfig(User.LAST_NAME, constants.lastName(), 120));
        columns.add(new ColumnConfig(User.NAME, constants.name(), 120));
        columns.add(new ColumnConfig(User.SURNAME, constants.surname(), 120));
        columns.add(new ColumnConfig(EMAILS_TEXT, constants.emailsColumn(), 200));
        columns.add(new ColumnConfig(PHONES_TEXT, constants.phonesColumn(), 200));

        ColumnConfig activeColumn = new ColumnConfig(ACTIVE_TEXT, constants.active(), 60);
        activeColumn.setAlignment(HorizontalAlignment.CENTER);
        columns.add(activeColumn);

        columns.add(new ColumnConfig(INV_TEXT, constants.invitationState(), 100));

        ColumnConfig levelColumn = new ColumnConfig(LEVEL, constants.levelColumn(),
                100);
        levelColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(levelColumn);

        columns.add(new ColumnConfig(VENDOR_TEXT, constants.vendorColumn(), 150));

        // columns.add(new ColumnConfig(User.EMAIL, constants.email(), 150));
        // columns.add(new ColumnConfig(ROLE_NAME, constants.role(), 150));

        grid = new Grid<BeanModel<User>>(store, new ColumnModel(columns));
        LiveGridView liveView = new FixedLiveGridView();
        liveView.setEmptyText(constants.emptyGrid());
        grid.setView(liveView);
        grid.getView().setSortingEnabled(false);
        // grid.disableTextSelection(false);

        if (securityService.isAllowed(Actions.USERS_MANAGEMENT)) {
            grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    edit();
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

    private void edit() {
        BeanModel<User> selectedItem = grid.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            User user = selectedItem.getBean();
            send(new ShowEditFormMessage(user));
        }
    }

    @Override
    public void showUsers(List<User> users, int offset, int total, String locale) {
        List<BeanModel<User>> rows = new ArrayList<BeanModel<User>>();
        for (User user : users) {
            user.setTransient(ACTIVE_TEXT, user.isActive() ? "v" : "");
            // user.setTransient(FULL_NAME, user.getFullName());
            int invState = user.getInvitationState();
            user.setTransient(INV_TEXT,
                    InvitationState.values.get(invState).get(locale));
            user.setTransient(LEVEL, user.getLevel() != 0 ? user.getLevel() : "-");

            List<String> emails = new ArrayList<String>();
            for (Email email : user.getEmails()) {
                emails.add(email.getEmail());
            }
            user.setTransient(EMAILS_TEXT, StringUtil.implode("; ", emails));

            List<String> phones = new ArrayList<String>();
            for (Phone phone : user.getPhones()) {
                phones.add(phone.getPhone());
            }
            user.setTransient(PHONES_TEXT, StringUtil.implode("; ", phones));

            Vendor vendor = user.getVendor();
            if (vendor != null)
                user.setTransient(VENDOR_TEXT, vendor.getName());

            rows.add(new BeanModel<User>(user));
        }
        PagingLoadResult<BeanModel<User>> result = new BasePagingLoadResult<BeanModel<User>>(
                rows, offset, total);
        callback.onSuccess(result);
    }

    @Override
    public void showAddForm(final User user, String locale, List<Vendor> vendors) {
        productAccessStore = null;

        addUserWindow = new Window();
        addUserWindow.setHeading(constants.addWindowHeading());
        addUserWindow.setSize(800, 600);
        addUserWindow.setModal(true);
        addUserWindow.setScrollMode(Scroll.AUTO);
        addUserWindow.setOnEsc(false);

        TabPanel tabPanel = new TabPanel();
        tabPanel.setHeight(532);
        tabPanel.setBodyBorder(false);
        addUserWindow.add(tabPanel);

        FormPanel formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);
        addFields(true, formPanel, user, locale, vendors, userService.getUser());
        Button saveButton = new Button(appConstants.save(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    fetchUser(user);
                    send(new AddUserMessage(user));
                }
            });
        FormButtonBinding addFormBinding = new FormButtonBinding(formPanel);
        addFormBinding.addButton(saveButton);

        TabItem userTabItem = new TabItem(constants.userTab());
        initTabItem(formPanel, tabPanel, userTabItem);

        addUserWindow.addButton(saveButton);
        addUserWindow.addButton(new Button(appConstants.cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    addUserWindow.hide();
                }
            }));

        addUserWindow.show();
    }

    @Override
    public void showEditForm(final User user, final String locale, List<Vendor> vendors) {
        editUserWindow = new Window();
        editUserWindow.setHeading(constants.editWindowHeading());
        editUserWindow.setSize(800, 600);
        editUserWindow.setModal(true);
        editUserWindow.setScrollMode(Scroll.AUTO);
        editUserWindow.setOnEsc(false);

        TabPanel tabPanel = new TabPanel();
        tabPanel.setHeight(532);
        tabPanel.setBodyBorder(false);
        editUserWindow.add(tabPanel);

        // tabs
        FormPanel formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);

        addUserTab(tabPanel, formPanel, user, locale, vendors);
        addAccessAlbumTab(tabPanel, user, locale);
        addAlbumDiscountTab(tabPanel, user, locale);

        Button saveButton = new Button(appConstants.save(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (!validateAlbumDiscounts(albumDiscountStore)) {
                        alertAlbumDiscountsTabHasUnfilledOrIncorrectData();
                        return;
                    }

                    fetchUser(user);
                    if (securityService.isAllowed(Actions.USERS_MODIFY_VENDOR_AND_USERNAME)) {
                        send(new UpdateUserMessage(user));
                    } else {
                        send(new NoVendorNoUsernameUpdateUserMessage(user));
                    }
                }
            });
        new FormButtonBinding(formPanel).addButton(saveButton);
        editUserWindow.addButton(saveButton);

        editUserWindow.addButton(new Button(appConstants.cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    editUserWindow.hide();
                }
            }));

        editUserWindow.show();
    }

    private void addUserTab(TabPanel tabPanel, FormPanel formPanel, final User user, final String locale,
                            List<Vendor> vendors) {
        addFields(false, formPanel, user, locale, vendors, userService.getUser());

        LabelField field = new LabelField();
        field.setHideLabel(true);
        field.setValue(constants.bonusCodes() + ":");
        formPanel.add(field);

        for (Order<?> order : user.getOrders()) {
            BonusCode code = order.getBonusCode();
            if (code != null) {
                field = new LabelField();
                field.setHideLabel(true);
                field.setValue(code.getAction().getName() + " - " + code.getCode());
                formPanel.add(field);
            }
        }

        initTabItem(formPanel, tabPanel, new TabItem(constants.userTab()));
    }

    private void addAccessAlbumTab(TabPanel tabPanel, final User user, final String locale) {
        FormPanel formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);
        formPanel.setBorders(false);

        HorizontalPanel hp = new HorizontalPanel();

        final ObjectField<Product> productField = new ObjectField<Product>() {
            @Override
            protected String render(Product product) {
                if (product == null) {
                    return null;
                }
                return product.getName().getNonEmptyValue(locale);
            }

            @Override
            protected void load(int offset, int limit, String query, ObjectFieldCallback<Product> callback) {
                productAccessCallback = callback;
                presenter.loadProducts(offset, limit, query, productAccessCallback);
            }
        };
        productField.setEmptyText(constants.albumEmptyText());
        productField.setLoadingText(constants.albumLoadingText());
        productField.setAllowBlank(false);
        productField.setWidth(690);
        hp.add(productField);

        Button addProductButton = new Button(appConstants.add(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (!isAlbumExists(productField.getXValue(), productAccessStore)) {
                    addProduct(productField.getXValue(), productAccessStore, locale);
                } else {
                    MessageBox.alert(appConstants.error(), constants.albumExists(), null);
                }
            }
        });
        addProductButton.setStyleAttribute("marginLeft", "10px");
        hp.add(addProductButton);
        formPanel.add(hp, new FlowData(0, 0, 10, 0));

        ContentPanel gridPanel = new ContentPanel();
        gridPanel.setHeading(constants.availableAlbums());

        productAccessStore = new ListStore<BeanModel<Product>>();
        loadProducts(user, productAccessStore, locale);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig(ARTICLE, productConstants.article(), 100));
        columns.add(new ColumnConfig(NAME_TEXT, productConstants.name(), 658));

        final Grid<BeanModel<Product>> grid = new Grid<BeanModel<Product>>(productAccessStore, new ColumnModel(columns));
        grid.setHeight(395);

        ToolBar toolBar = new ToolBar();
        toolBar.add(new Button(appConstants.delete(), new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    List<BeanModel<Product>> selectedItems = grid.getSelectionModel().getSelectedItems();
                    if (selectedItems.isEmpty()) {
                        MessageBox.alert(appConstants.error(), constants.noAlbumsToDelete(), null);
                        return;
                    }

                    for (BeanModel<Product> item : selectedItems) {
                        productAccessStore.remove(item);
                    }
                }
            }));

        gridPanel.add(toolBar);
        gridPanel.add(grid);
        formPanel.add(gridPanel);

        initTabItem(formPanel, tabPanel, new TabItem(constants.accessAlbumTab()));
    }

    private void loadProducts(User user, ListStore<BeanModel<Product>> store, String locale) {
        for (Product product : user.getAccessedProducts()) {
            addProduct(product, store, locale);
        }
    }

    private void initTabItem(FormPanel formPanel, TabPanel tabPanel, TabItem tabItem) {
        tabItem.setAutoWidth(true);
        tabItem.setAutoHeight(true);
        tabItem.setBorders(false);
        tabItem.setScrollMode(Scroll.AUTO);
        tabItem.add(formPanel);
        tabPanel.add(tabItem);
    }

    private void addProduct(Product product, ListStore<BeanModel<Product>> store, String locale) {
        product.setTransient(ARTICLE, orderService.getProductArticle(product));
        product.setTransient(NAME_TEXT, product.getName().getNonEmptyValue(locale));
        store.add(new BeanModel<Product>(product));
    }

    private boolean isAlbumExists(Product product, ListStore<BeanModel<Product>> store) {
        for (BeanModel<Product> productModel : store.getModels()) {
            if (productModel.getBean().equals(product)) {
                return true;
            }
        }
        return false;
    }

    private void addAlbumDiscountTab(TabPanel tabPanel, final User user, final String locale) {
        FormPanel formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);

        ContentPanel gridPanel = new ContentPanel();
        gridPanel.setHeaderVisible(false);

        albumDiscountStore = new ListStore<UserAlbumDiscountModel>();
        loadAlbumDiscounts(user, albumDiscountStore, locale);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        // Album column
        ColumnConfig productColumn = new ColumnConfig(UserAlbumDiscount.PRODUCT, constants.album(), 500);
        final ObjectField<Product> productField = new ObjectField<Product>() {
            @Override
            protected String render(Product product) {
                if (product == null) {
                    return null;
                }
                return product.getName().getNonEmptyValue(locale);
            }

            @Override
            protected void load(int offset, int limit, String query, ObjectFieldCallback<Product> callback) {
                productDiscountCallback = callback;
                presenter.loadProducts(offset, limit, query, productDiscountCallback);
            }
        };
        productField.setEmptyText(constants.albumEmptyText());
        productField.setLoadingText(constants.albumLoadingText());
        productField.setAllowBlank(false);
        productField.setAutoValidate(true);
        productColumn.setEditor(new CellEditor(productField));
        productColumn.setRenderer(new GridCellRenderer<UserAlbumDiscountModel>() {
            @Override
            public Object render(UserAlbumDiscountModel model, String property, ColumnData config,
                                 int rowIndex, int colIndex, ListStore<UserAlbumDiscountModel> store,
                                 Grid<UserAlbumDiscountModel> grid) {
                ObjectModel<Product> productObjectModel = (ObjectModel) model.get(property);
                return (productObjectModel != null) ? productObjectModel.getText() : null;
            }
        });
        columns.add(productColumn);

        // DiscountPc column
        ColumnConfig discountColumn = new ColumnConfig(UserAlbumDiscount.DISCOUNT_PC, constants.discountPc(), 200);
        IntegerField numberField = new IntegerField();
        numberField.setAllowDecimals(false);
        numberField.setAllowNegative(false);
        numberField.setMinValue(1);
        numberField.setMaxValue(100);
        numberField.setAutoValidate(true);
        discountColumn.setEditor(new CellEditor(numberField));
        columns.add(discountColumn);

        ColumnModel columnModel = new ColumnModel(columns);
        albumDiscountGrid = new EditorGrid<UserAlbumDiscountModel>(albumDiscountStore, columnModel);
        albumDiscountGrid.setAutoExpandColumn(UserAlbumDiscount.PRODUCT);
        albumDiscountGrid.setHeight(200);
        albumDiscountGrid.addListener(Events.AfterEdit, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent baseEvent) {
                albumDiscountStore.commitChanges();

                UserAlbumDiscountModel model = albumDiscountGrid.getSelectionModel().getSelectedItem();
                UserAlbumDiscount albumDiscount = model.getAlbumDiscount();

                if (isAlbumExists(model, albumDiscountStore)) {
                    alertAlbumDiscountExists();
                    model.setProduct(createEmptyProduct(), locale);
                    albumDiscountStore.update(model);
                }

                Product product = albumDiscount.getProduct();
                boolean valueHasChanged = !Objects.equal(product, model.getProduct())
                    || !Objects.equal(albumDiscount.getDiscountPc(), model.getDiscountPc());
                if (valueHasChanged) {
                    albumDiscount.setProduct(model.getProduct());
                    albumDiscount.setDiscountPc(model.getDiscountPc());
                }
            }
        });

        ToolBar toolBar = new ToolBar();
        toolBar.add(new Button(appConstants.add(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                Product emptyProduct = createEmptyProduct();
                UserAlbumDiscount albumDiscount = new UserAlbumDiscount(emptyProduct, 0);
                UserAlbumDiscountModel model = new UserAlbumDiscountModel(albumDiscount, locale);

                albumDiscountGrid.stopEditing();
                albumDiscountStore.insert(model, 0);
                albumDiscountGrid.startEditing(albumDiscountStore.indexOf(model), 0);
            }
        }));
        toolBar.add(new Button(appConstants.delete(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                UserAlbumDiscountModel selectedItem = albumDiscountGrid.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    albumDiscountStore.remove(selectedItem);
                } else {
                    MessageBox.alert(appConstants.error(), constants.noRowsToDelete(), null);
                }
            }
        }));

        gridPanel.add(toolBar);
        gridPanel.add(albumDiscountGrid);
        formPanel.add(gridPanel);

        initTabItem(formPanel, tabPanel, new TabItem(constants.albumDiscountTab()));
    }

    private Product createEmptyProduct() {
        Product emptyProduct = new AlbumImpl();
        emptyProduct.setName(I18n.ms(constants.selectAlbum(), constants.selectAlbum()));
        return emptyProduct;
    }

    private void loadAlbumDiscounts(User user, ListStore<UserAlbumDiscountModel> store, final String locale) {
        for (UserAlbumDiscount albumDiscount : user.getAlbumDiscounts()) {
            store.add(new UserAlbumDiscountModel(albumDiscount, locale));
        }
    }

    private boolean isAlbumExists(final UserAlbumDiscountModel selectedItem, ListStore<UserAlbumDiscountModel> store) {
        return FluentIterable.from(store.getModels()).anyMatch(new Predicate<UserAlbumDiscountModel>() {
            @Override
            public boolean apply(@Nullable UserAlbumDiscountModel model) {
                assert model != null;
                return model != selectedItem && model.getProduct() != null
                        && model.getProduct().equals(selectedItem.getProduct());
            }
        });
    }

    private boolean validateAlbumDiscounts(ListStore<UserAlbumDiscountModel> store) {
        if (store == null)  {
            return true;
        }
        boolean hasEmptyProduct = FluentIterable.from(store.getModels())
            .anyMatch(new Predicate<UserAlbumDiscountModel>() {
                @Override
                public boolean apply(@Nullable UserAlbumDiscountModel input) {
                    assert input != null;
                    Product product = input.getProduct();
                    return product == null || product.getId() == null;
                }
            });
        return !hasEmptyProduct;
    }

    private void alertAlbumDiscountsTabHasUnfilledOrIncorrectData() {
        MessageBox.alert(appConstants.error(), constants.albumDiscountsHasUnfilledOrIncorrectData(), null);
    }

    @Override
    public void alertAlbumDiscountExists() {
        MessageBox.alert(appConstants.error(), constants.albumDiscountExists(), null);
    }

    public void addFields(boolean create, FormPanel formPanel, User user,
                          String locale, List<Vendor> vendors, User currentUser) {
        formPanel.setLabelWidth(150);

        vendorField = new SelectField<Vendor>();
        vendorField.setFieldLabel(constants.vendorField());
        vendorField.setAllowBlank(false);
        for (Vendor vendor : vendors) {
            vendorField.add(vendor, vendor.getName());
        }
        if (!securityService.isAllowed(Actions.USERS_MODIFY_VENDOR_AND_USERNAME)) {
            vendorField.setVisible(false);
        }
        formPanel.add(vendorField);
        vendorField.setXValue(user.getVendor());

        activeField = new CheckBox();
        activeField.setFieldLabel(constants.active());
        activeField.setValue(user.isActive());
        activeField.setBoxLabel("");
        if (create)
            activeField.addPlugin(new HintPlugin(constants.activeHint()));
        formPanel.add(activeField);

        userNameField = new TextField<String>();
        userNameField.setFieldLabel(constants.userName() + " *");
        userNameField.setAllowBlank(false);
        userNameField.setValue(user.getUserName());
        userNameField.setMinLength(5);
        userNameField.addListener(Events.Change, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                XEmailField field = emailsFieldSet.getFirstField();
                EmailField emailField = field.getEmailField();
                if (emailField.getValue() == null)
                    emailField.setValue(userNameField.getValue());
            }
        });
        if (!securityService.isAllowed(Actions.USERS_MODIFY_VENDOR_AND_USERNAME)) {
            userNameField.setReadOnly(true);
        }
        formPanel.add(userNameField);

        passwordField = new TextField<String>();
        String passwordTitle = constants.password();
        if (create) {
            passwordTitle += " *";
            passwordField.setAllowBlank(false);
        } else
            passwordField.addPlugin(new HintPlugin(constants.blankPassword()));
        passwordField.setValue(user.getPassword());
        passwordField.setFieldLabel(passwordTitle);
        passwordField.setMinLength(6);
        formPanel.add(passwordField);

        lastNameField = new TextField<String>();
        lastNameField.setFieldLabel(constants.lastName());
        lastNameField.setValue(user.getLastName());
        formPanel.add(lastNameField);

        nameField = new TextField<String>();
        nameField.setFieldLabel(constants.name() + " *");
        nameField.setValue(user.getName());
        formPanel.add(nameField);

        surnameField = new TextField<String>();
        surnameField.setFieldLabel(constants.surname());
        surnameField.setValue(user.getSurname());
        formPanel.add(surnameField);

        levelField = new SelectField<Integer>();
        levelField.add(0, "-");
        levelField.add(1, "1");
        levelField.add(2, "2");
        levelField.add(3, "3");
        levelField.add(4, "4");
        levelField.add(5, "5");
        levelField.add(6, "6");
        levelField.add(7, "7");
        levelField.add(8, "8");
        levelField.add(9, "9");
        levelField.setFieldLabel(constants.level());
        levelField.setXValue(user.getLevel());
        levelField.setAllowBlank(false);
        formPanel.add(levelField);

        photographerField = new CheckBox();
        photographerField.setFieldLabel(constants.photographerField());
        photographerField.setValue(user.isPhotographer());
        photographerField.setBoxLabel("");
        formPanel.add(photographerField);

        if (currentUser.getVendor().getType() == VendorType.IMAGEBOOK) {
            urgentOrdersField = new CheckBox();
            urgentOrdersField.setFieldLabel(constants.urgentOrdersField());
            urgentOrdersField.setValue(user.isUrgentOrders());
            urgentOrdersField.setBoxLabel("");
            formPanel.add(urgentOrdersField);
        }

        advOrdersField = new CheckBox();
        advOrdersField.setFieldLabel(constants.advOrdersField());
        advOrdersField.setValue(user.isAdvOrders());
        advOrdersField.setBoxLabel("");
        formPanel.add(advOrdersField);

        emailsFieldSet = new ListFieldSet<Email, XEmailField>(user.getEmails(),
                appConstants, xgxtMessages) {
            @Override
            public Email createObject() {
                return new Email();
            }

            @Override
            public XEmailField createField(Email email) {
                XEmailField field = new XEmailField(xgxtConstants, constants);
                field.setValue(email);
                return field;
            }

            @Override
            public Email fetchObject(XEmailField field) {
                return field.getValue();
            }
        };
        emailsFieldSet.setHeading(constants.emails() + " *");
        emailsFieldSet.setMinCount(1);
        emailsFieldSet.setHint(constants.emailsHint());
        formPanel.add(emailsFieldSet);

        skipMailingField = new CheckBox();
        skipMailingField.setFieldLabel(constants.skipMailing());
        skipMailingField.setValue(user.isSkipMailing());
        skipMailingField.setBoxLabel("");
        formPanel.add(skipMailingField);

        phonesFieldSet = new ListFieldSet<Phone, XPhoneField>(user.getPhones(),
                appConstants, xgxtMessages) {
            @Override
            public Phone createObject() {
                return new Phone();
            }

            @Override
            public XPhoneField createField(Phone phone) {
                XPhoneField field = new XPhoneField();
                field.setAllowBlank(false);
                field.setValue(phone);
                return field;
            }

            @Override
            public Phone fetchObject(XPhoneField field) {
                return field.getValue();
            }
        };
        phonesFieldSet.setHeading(constants.phones());
        formPanel.add(phonesFieldSet);

        addressesFieldSet = new ListFieldSet<Address, AddressFieldSet>(
                user.getAddresses(), appConstants, xgxtMessages) {
            @Override
            public Address createObject() {
                return new Address();
            }

            @Override
            public AddressFieldSet createField(Address address) {
                AddressFieldSet addressFieldSet = new AddressFieldSet(constants);
                addressFieldSet.setValue(address);
                return addressFieldSet;
            }

            @Override
            public Address fetchObject(AddressFieldSet field) {
                return field.fetch();
            }
        };
        addressesFieldSet.setHeading(constants.addresses());
        formPanel.add(addressesFieldSet);

        localeField = new SelectField<String>();
        localeField.add(Locales.RU, Locales.RU);
        // for (String xLocale : locales) {
        // localeField.add(xLocale, xLocale);
        // }
        localeField.setFieldLabel(constants.locale());
        localeField.setAllowBlank(false);
        localeField.setXValue(user.getLocale());
        formPanel.add(localeField);

        invitationStateField = new SelectField<Integer>();
        for (int state : InvitationState.values.keySet()) {
            invitationStateField.add(state, InvitationState.values.get(state).get(locale));
        }
        invitationStateField.setFieldLabel(constants.invitationState());
        invitationStateField.setXValue(user.getInvitationState());
        formPanel.add(invitationStateField);

        editorSourcesStoragePeriodField = new IntegerField();
        editorSourcesStoragePeriodField.setFieldLabel(constants.editorSourcesStoragePeriodField());
        editorSourcesStoragePeriodField.setAllowDecimals(false);
        editorSourcesStoragePeriodField.setAllowNegative(false);
        editorSourcesStoragePeriodField.setMinValue(1);
        editorSourcesStoragePeriodField.setValue(user.getEditorSourcesStoragePeriod());
        formPanel.add(editorSourcesStoragePeriodField);

        LabelField labelField = new LabelField();
        labelField.setFieldLabel(constants.info() + ":");
        formPanel.add(labelField);
        infoField = new TextArea();
        infoField.setValue(user.getInfo());
        infoField.setHideLabel(true);
        formPanel.add(infoField, new FormData(400, 100));
    }

    private void fetchUser(User user) {
        user.setVendor(vendorField.getXValue());
        user.setActive(activeField.getValue());
        user.setUserName(userNameField.getValue());
        String password = passwordField.getValue();
        if (password != null) {
            password = password.trim();
        }
        user.setPassword(password);
        user.setName(nameField.getValue());
        user.setLastName(lastNameField.getValue());
        user.setSurname(surnameField.getValue());
        user.setSkipMailing(skipMailingField.getValue());
        emailsFieldSet.fetch();
        phonesFieldSet.fetch();
        addressesFieldSet.fetch();
        user.setLocale(localeField.getXValue());
        user.setInvitationState(invitationStateField.getXValue());
        user.setInfo(infoField.getValue());
        user.setLevel(levelField.getXValue());
        user.setPhotographer(photographerField.getValue());
        if (urgentOrdersField != null) {
            user.setUrgentOrders(urgentOrdersField.getValue());
        }
        user.setAdvOrders(advOrdersField.getValue());
        user.setEditorSourcesStoragePeriod(editorSourcesStoragePeriodField.getValue());

        if (productAccessStore != null) {
            HashSet<Product> products = new HashSet<Product>();
            for (BeanModel<Product> productModel : productAccessStore.getModels()) {
                products.add(productModel.getBean());
            }
            user.setAccessedProducts(products);
        }

        if (albumDiscountStore != null) {
            user.getAlbumDiscounts().clear();
            for (UserAlbumDiscountModel albumDiscountModel : albumDiscountStore.getModels()) {
                user.addAlbumDiscount(albumDiscountModel.getAlbumDiscount());
            }
        }
    }

    @Override
    public void hideAddForm() {
        addUserWindow.hide();
    }

    @Override
    public void reload() {
        loader.load();
    }

    @Override
    public void hideEditForm() {
        editUserWindow.hide();
    }

    @Override
    public void confirmDeleteUsers(final List<User> users) {
        new ConfirmMessageBox(appConstants.warning(), constants.deleteUsersConfirmation(), new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                send(new DeleteUsersMessage(users));
            }
        });
    }

    @Override
    public void alertCannotDeleteOwnUser() {
        MessageBox.alert(appConstants.error(), constants.cannotDeleteOwnUser(), null);
    }

    @Override
    public void alertNoUsersToDelete() {
        MessageBox.alert(appConstants.error(), constants.noUsersToDelete(), null);
    }

    @Override
    public void alertUserExists() {
        MessageBox.alert(appConstants.error(), constants.userExists(), null);
    }

    @Override
    public void showProducts(List<Product> products, int offset, long total, ObjectFieldCallback<Product> callback) {
        callback.onLoaded(products, offset, total);
    }
}
