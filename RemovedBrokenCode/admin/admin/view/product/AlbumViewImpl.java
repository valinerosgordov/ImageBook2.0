package ru.imagebook.client.admin.view.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldSetEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
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
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.ctl.product.AddAlbumMessage;
import ru.imagebook.client.admin.ctl.product.AlbumView;
import ru.imagebook.client.admin.ctl.product.DeleteAlbumsMessage;
import ru.imagebook.client.admin.ctl.product.DeleteAlbumsRequestMessage;
import ru.imagebook.client.admin.ctl.product.ProductMessages;
import ru.imagebook.client.admin.ctl.product.ShowEditAlbumFormMessage;
import ru.imagebook.client.admin.ctl.product.UpdateAlbumMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.admin.view.user.UserConstants;
import ru.imagebook.client.common.service.order.OrderService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Availability;
import ru.imagebook.shared.model.Binding;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Cover;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Paper;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.HintPlugin;
import ru.minogin.core.client.gxt.form.BooleanField;
import ru.minogin.core.client.gxt.form.DoubleField;
import ru.minogin.core.client.gxt.form.FloatField;
import ru.minogin.core.client.gxt.form.ObjectField;
import ru.minogin.core.client.gxt.form.ObjectFieldCallback;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.SelectValue;
import ru.minogin.core.client.gxt.form.XTextField;

@Singleton
public class AlbumViewImpl extends View implements AlbumView {
    private static final String ARTICLE = "article";
    private static final String LOGIN = "login";
    private static final String TYPE_TEXT = "typeText";
    private static final String NAME_TEXT = "nameText";
    private static final String AVAILABILITY_TEXT = "availText";
    private static final String BINDING_TEXT = "bindingText";
    private static final String COVER_TEXT = "coverText";
    private static final String PAPER_TEXT = "paperText";
    private static final String FULL_NAME = "fullName";
    private static final String USER_NAME = "userName";

    private final Widgets widgets;
    private final ProductConstants constants;
    private final UserConstants userConstants;
    private final CommonConstants appConstants;
    private final OrderService orderService;

    private Grid<BeanModel<Album>> grid;
    private final Format format;
    private Window addWindow;
    private SelectField<Integer> typeField;
    private NumberField numberField;
    private MultiStringFieldRenderer nameFieldRenderer;
    private SelectField<Integer> availField;
    private SelectField<Integer> bindingField;
    private SelectField<Integer> coverField;
    private SelectField<Integer> paperField;
    private NumberField minPageCountField;
    private NumberField maxPageCountField;
    private NumberField minQuantityField;
    private FieldSet colorRangeFieldSet;
    private HashMap<Integer, CheckBox> coverLamRangeFields;
    private HashMap<Integer, CheckBox> pageLamRangeFields;
    private SelectField<Integer> multiplicityField;
    private TextField<String> blockFormatField;
    private HashMap<Color, CheckBox> colorRangeFields;
    private Album album;
    private ListStore<BeanModel<Album>> store;
    private LabelToolItem totalItem;
    private Window editWindow;
    private TextField<String> sizeField;
    private NumberField widthField;
    private NumberField heightField;
    private TextField<String> coverSizeField;
    private NumberField pdfCoverWidthField;
    private NumberField pdfCoverHeightField;
    private TextField<String> jpegFolderField;
    private TextField<String> jpegCoverFolderField;
    private NumberField innerCropField;
    private CheckBox hardcoverField;
    private NumberField frontUpperCropField;
    private NumberField frontBottomCropField;
    private NumberField frontLeftCropField;
    private NumberField frontRightCropField;
    private NumberField backUpperCropField;
    private NumberField backBottomCropField;
    private NumberField backLeftCropField;
    private NumberField backRightCropField;
    private NumberField blockWidthField;
    private NumberField blockHeightField;
    private FloatField upperSafeAreaField;
    private FloatField bottomSafeAreaField;
    private FloatField innerSafeAreaField;
    private FloatField outerSafeAreaField;
    private FloatField upperCoverSafeAreaField;
    private FloatField bottomCoverSafeAreaField;
    private FloatField leftCoverSafeAreaField;
    private FloatField rightCoverSafeAreaField;
    private NumberField coverWidthEditorParamsField;
    private NumberField coverHeightEditorParamsField;
    private TextField<String> coverNameField;
    private BooleanField addressPrintedField;
    private XTextField lastPageTemplate;
    private BooleanField nonEditorField;
    private BooleanField nonCalcField;
    private BooleanField trialAlbumField;
    private DoubleField pagePriceField;
    private DoubleField basePriceField;
    private DoubleField coverLaminationPriceField;
    private DoubleField phPagePriceField;
    private DoubleField phBasePriceField;
    private DoubleField phCoverLaminationPriceField;
    private DoubleField pageLaminationPriceField;
    private DoubleField phPageLaminationPriceField;
    private FieldSet specialOfferFieldSet;
    private NumberField minAlbumsCountForDiscountField;
    private NumberField imagebookDiscountField;
    private NumberField phDiscountField;
    private NumberField barcodeXEditorParamsField;
    private NumberField barcodeYEditorParamsField;
    private NumberField barcodeXMPhotoParamsField;
    private NumberField barcodeYMPhotoParamsField;
    private BooleanField trialDeliveryField;
    private TextField<String> approxProdTime;
    private TextArea calcComment;
    private BooleanField lastSpreadBarcodeField;

    private ObjectField<User> userField;
    private AlbumPresenter presenter;
    private ListStore<BeanModel<User>> usersStore;
    private Radio radioUserAll;

    protected AsyncCallback<PagingLoadResult<BeanModel<User>>> callback;
    private ObjectFieldCallback<User> userCallback;
    private CheckBox flyleafBox;
    private CheckBox supportsVellumBox;

    public void setPresenter(AlbumPresenter presenter) {
        this.presenter = presenter;
    }

    @Inject
    public AlbumViewImpl(Dispatcher dispatcher, Widgets widgets,
                         ProductConstants constants, CommonConstants appConstants, UserConstants userConstants,
                         OrderService orderService, Format format) {
        super(dispatcher);

        this.widgets = widgets;
        this.constants = constants;
        this.appConstants = appConstants;
        this.userConstants = userConstants;
        this.orderService = orderService;
        this.format = format;
    }

    @Override
    public void showAlbumsSection() {
        LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
        desktop.removeAll();

        ContentPanel panel = new ContentPanel(new FitLayout());
        panel.setHeading(constants.albumsPanelHeading());

        ToolBar toolBar = new ToolBar();
        toolBar.add(new Button(appConstants.add(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        send(ProductMessages.SHOW_ADD_ALBUM_FORM);
                    }
                }));
        toolBar.add(new Button(appConstants.edit(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        edit();
                    }
                }));
        toolBar.add(new Button(appConstants.delete(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        List<BeanModel<Album>> selectedItems = grid.getSelectionModel().getSelectedItems();
                        List<Album> albums = new ArrayList<Album>();
                        for (BeanModel<Album> item : selectedItems) {
                            albums.add(item.getBean());
                        }
                        send(new DeleteAlbumsRequestMessage(albums));
                    }
                }));
        toolBar.add(new Button(constants.photosButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        presenter.photosButtonClicked();
                    }
                }));
        panel.setTopComponent(toolBar);

        store = new ListStore<BeanModel<Album>>();

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig(ARTICLE, constants.article(), 50));
        columns.add(new ColumnConfig(TYPE_TEXT, constants.type(), 100));

        ColumnConfig numberColumn = new ColumnConfig(Product.NUMBER, constants.number(), 50);
        numberColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(numberColumn);

        columns.add(new ColumnConfig(NAME_TEXT, constants.name(), 100));
        columns.add(new ColumnConfig(AVAILABILITY_TEXT, constants.availability(), 50));
        columns.add(new ColumnConfig(Product.BLOCK_FORMAT, constants.blockFormat(), 100));
        columns.add(new ColumnConfig(BINDING_TEXT, constants.binding(), 100));
        columns.add(new ColumnConfig(COVER_TEXT, constants.cover(), 100));
        columns.add(new ColumnConfig(PAPER_TEXT, constants.paper(), 100));

        ColumnConfig multColumn = new ColumnConfig(Product.MULTIPLICITY, constants.multiplicity(), 50);
        multColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(multColumn);

        ColumnConfig minColumn = new ColumnConfig(Product.MIN_PAGE_COUNT, constants.minPageCount(), 50);
        minColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(minColumn);

        ColumnConfig maxColumn = new ColumnConfig(Product.MAX_PAGE_COUNT, constants.maxPageCount(), 50);
        maxColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(maxColumn);

        ColumnConfig quantityColumn = new ColumnConfig(Product.MIN_QUANTITY, constants.minQuantity(), 50);
        quantityColumn.setAlignment(HorizontalAlignment.RIGHT);
        columns.add(quantityColumn);

        grid = new Grid<BeanModel<Album>>(store, new ColumnModel(columns));
        grid.getView().setSortingEnabled(false);
        grid.getView().setForceFit(true);
        grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                edit();
            }
        });
        panel.add(grid, new RowData(1, 1));

        ToolBar gridToolBar = new ToolBar();
        totalItem = new LabelToolItem();
        gridToolBar.add(totalItem);
        // LiveToolItem item = new LiveToolItem();
        // item.bindGrid(grid);
        // gridToolBar.add(item);
        panel.setBottomComponent(gridToolBar);

        desktop.add(panel);
        desktop.layout();
    }

    @Override
    public void showAlbums(List<Album> albums, String locale) {
        // TODO show total

        store.removeAll();
        for (Album album : albums) {
            album.setTransient(ARTICLE, orderService.getProductArticle(album));
            album.setTransient(TYPE_TEXT, format.formatType(album.getType(), locale));
            album.setTransient(NAME_TEXT, album.getName().getNonEmptyValue(locale));
            album.setTransient(AVAILABILITY_TEXT, Availability.values.get(album.getAvailability())
                .getNonEmptyValue(locale));
            album.setTransient(BINDING_TEXT, Binding.values.get(album.getBinding()).getNonEmptyValue(locale));
            album.setTransient(COVER_TEXT, Cover.values.get(album.getCover()).getNonEmptyValue(locale));
            album.set(PAPER_TEXT, Paper.values.get(album.getPaper()).getNonEmptyValue(locale));
            store.add(new BeanModel<Album>(album));
        }

        totalItem.setLabel(constants.totalItem() + " " + albums.size());
    }

    @Override
    public void showAddForm(final Album album, String locale, Collection<String> locales) {
        this.album = album;

        addWindow = new Window();
        addWindow.setHeading(constants.addWindowHeading());
        addWindow.setSize(800, 600);
        addWindow.setModal(true);
        addWindow.setScrollMode(Scroll.AUTO);

        TabPanel tabPanel = new TabPanel();
        tabPanel.setHeight(520);
        tabPanel.setBorders(false);

        FormPanel formPanel = new FormPanel();
        formPanel.setLabelWidth(150);
        formPanel.setHeaderVisible(false);

        addFields(formPanel, album, locale, locales);
        Button saveButton = new Button(appConstants.save(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        fetchAlbum(album);
                        send(new AddAlbumMessage(album));
                    }
                });
        new FormButtonBinding(formPanel).addButton(saveButton);

        TabItem albumTabItem = new TabItem(constants.albumTab());
        initTabItem(formPanel, tabPanel, albumTabItem);
        addWindow.add(tabPanel);

        addWindow.addButton(saveButton);
        addWindow.addButton(new Button(appConstants.cancel(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        addWindow.hide();
                    }
                }));

        addWindow.show();
    }

    private void addFields(FormPanel formPanel, final Album album, String locale, Collection<String> locales) {
        typeField = new SelectField<Integer>();
        typeField.setFieldLabel(constants.type());
        typeField.setAllowBlank(false);
        for (int type : ProductType.values.keySet()) {
            typeField.add(type, format.formatType(type, locale));
        }
        typeField.setXValue(album.getType());
        typeField.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Integer>>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<SelectValue<Integer>> se) {
                album.setType(typeField.getXValue());
                if (album.isSeparateCover()) {
                    coverSizeField.show();
                    pdfCoverWidthField.show();
                    pdfCoverHeightField.show();
                    jpegCoverFolderField.show();
                    innerCropField.show();
                    hardcoverField.show();
                    frontUpperCropField.show();
                    frontBottomCropField.show();
                    frontLeftCropField.show();
                    frontRightCropField.show();
                    backUpperCropField.show();
                    backBottomCropField.show();
                    backLeftCropField.show();
                    backRightCropField.show();
                    coverWidthEditorParamsField.show();
                    coverHeightEditorParamsField.show();
                } else {
                    coverSizeField.hide();
                    pdfCoverWidthField.hide();
                    pdfCoverHeightField.hide();
                    jpegCoverFolderField.hide();
                    innerCropField.hide();
                    hardcoverField.hide();
                    frontUpperCropField.hide();
                    frontBottomCropField.hide();
                    frontLeftCropField.hide();
                    frontRightCropField.hide();
                    backUpperCropField.hide();
                    backBottomCropField.hide();
                    backLeftCropField.hide();
                    backRightCropField.hide();
                    coverWidthEditorParamsField.hide();
                    coverHeightEditorParamsField.hide();
                }

                if (album.isClipType()) {
                    coverLaminationPriceField.show();
                } else {
                    coverLaminationPriceField.setValue(0d);
                    coverLaminationPriceField.hide();
                }
            }
        });
        formPanel.add(typeField, new FormData(300, -1));

        numberField = new NumberField();
        numberField.setPropertyEditorType(Integer.class);
        numberField.setAllowNegative(false);
        numberField.setFieldLabel(constants.number() + " *");
        numberField.setAllowBlank(false);
        numberField.setValue(album.getNumber());
        formPanel.add(numberField);

        nameFieldRenderer = new MultiStringFieldRenderer(formPanel, locales, constants.name());
        nameFieldRenderer.setFieldWidth(400);
        nameFieldRenderer.setAllowBlank(false);
        nameFieldRenderer.render();
        nameFieldRenderer.setValue(album.getName());

        availField = new SelectField<Integer>();
        availField.setFieldLabel(constants.availability() + " *");
        availField.setAllowBlank(false);
        for (int avail : Availability.values.keySet()) {
            availField.add(avail, Availability.values.get(avail).get(locale));
        }
        availField.setXValue(album.getAvailability());
        formPanel.add(availField);

        blockFormatField = new TextField<String>();
        blockFormatField.setFieldLabel(constants.blockFormat() + " *");
        blockFormatField.setAllowBlank(false);
        blockFormatField.setValue(album.getBlockFormat());
        formPanel.add(blockFormatField);

        blockWidthField = new NumberField();
        blockWidthField.setPropertyEditorType(Integer.class);
        blockWidthField.setFieldLabel(constants.blockWidthField());
        blockWidthField.setValue(album.getBlockWidth());
        formPanel.add(blockWidthField);

        blockHeightField = new NumberField();
        blockHeightField.setPropertyEditorType(Integer.class);
        blockHeightField.setFieldLabel(constants.blockHeightField());
        blockHeightField.setValue(album.getBlockHeight());
        formPanel.add(blockHeightField);

        bindingField = new SelectField<Integer>();
        bindingField.setFieldLabel(constants.binding() + " *");
        bindingField.setAllowBlank(false);
        for (int binding : Binding.values.keySet()) {
            bindingField.add(binding, Binding.values.get(binding).get(locale));
        }
        bindingField.setXValue(album.getBinding());
        formPanel.add(bindingField);

        coverField = new SelectField<Integer>();
        coverField.setFieldLabel(constants.cover() + " *");
        coverField.setAllowBlank(false);
        for (int cover : Cover.values.keySet()) {
            coverField.add(cover, Cover.values.get(cover).get(locale));
        }
        coverField.setXValue(album.getCover());
        formPanel.add(coverField);

        paperField = new SelectField<Integer>();
        paperField.setFieldLabel(constants.paper() + " *");
        paperField.setAllowBlank(false);
        for (int paper : Paper.values.keySet()) {
            paperField.add(paper, Paper.values.get(paper).get(locale));
        }
        paperField.setXValue(album.getPaper());
        formPanel.add(paperField, new FormData(400, -1));

        multiplicityField = new SelectField<Integer>();
        multiplicityField.setFieldLabel(constants.multiplicity() + " *");
        multiplicityField.setAllowBlank(false);
        multiplicityField.add(2);
        multiplicityField.add(4);
        multiplicityField.add(8);
        multiplicityField.add(12);
        multiplicityField.setXValue(album.getMultiplicity());
        formPanel.add(multiplicityField);

        minPageCountField = new NumberField();
        minPageCountField.setPropertyEditorType(Integer.class);
        minPageCountField.setAllowNegative(false);
        minPageCountField.setFieldLabel(constants.minPageCount() + " *");
        minPageCountField.setAllowBlank(false);
        minPageCountField.setValue(album.getMinPageCount());
        formPanel.add(minPageCountField);

        maxPageCountField = new NumberField();
        maxPageCountField.setPropertyEditorType(Integer.class);
        maxPageCountField.setAllowNegative(false);
        maxPageCountField.setFieldLabel(constants.maxPageCount() + " *");
        maxPageCountField.setAllowBlank(false);
        maxPageCountField.setValue(album.getMaxPageCount());
        formPanel.add(maxPageCountField);

        minQuantityField = new NumberField();
        minQuantityField.setPropertyEditorType(Integer.class);
        minQuantityField.setAllowNegative(false);
        minQuantityField.setFieldLabel(constants.minQuantity() + " *");
        minQuantityField.setAllowBlank(false);
        minQuantityField.setValue(album.getMinQuantity());
        formPanel.add(minQuantityField);

        pagePriceField = new DoubleField("Цена за 1 страницу", false, formPanel);
        pagePriceField.setValue(album.getPagePrice());

        basePriceField = new DoubleField("Базовая цена", false, formPanel);
        basePriceField.setValue(album.getBasePrice());

        coverLaminationPriceField = new DoubleField("Цена ламинации обложки", false, formPanel);
        coverLaminationPriceField.setValue(album.getCoverLaminationPrice());
        coverLaminationPriceField.setVisible(album.isClipType());

        phPagePriceField = new DoubleField("Цена типографии за 1 страницу", false, formPanel);
        phPagePriceField.setValue(album.getPhPagePrice());

        phBasePriceField = new DoubleField("Базовая цена типографии", false, formPanel);
        phBasePriceField.setValue(album.getPhBasePrice());

        phCoverLaminationPriceField = new DoubleField("Цена типографии за ламинацию обложки", false,
            formPanel);
        phCoverLaminationPriceField.setValue(album.getPhCoverLaminationPrice());

        pageLaminationPriceField = new DoubleField(constants.pageLaminationPriceField(), false, formPanel);
        pageLaminationPriceField.setValue(album.getPageLaminationPrice());

        phPageLaminationPriceField = new DoubleField(constants.phPageLaminationPriceField(), false, formPanel);
        phPageLaminationPriceField.setValue(album.getPhPageLaminationPrice());

        colorRangeFieldSet = new FieldSet();
        colorRangeFieldSet.setHeading(constants.colorRange());
        formPanel.add(colorRangeFieldSet);

        coverLamRangeFields = new HashMap<Integer, CheckBox>();
        FieldSet coverLamRangeFieldSet = new FieldSet();
        coverLamRangeFieldSet.setHeading(constants.coverLamRange());
        List<Integer> coverLamRange = album.getCoverLamRange();
        for (int coverLam : CoverLamination.values.keySet()) {
            CheckBox checkBox = new CheckBox();
            checkBox.setHideLabel(true);
            checkBox.setBoxLabel(CoverLamination.values.get(coverLam)
                    .getNonEmptyValue(locale));
            checkBox.setValue(coverLamRange.contains(coverLam));
            coverLamRangeFields.put(coverLam, checkBox);
            coverLamRangeFieldSet.add(checkBox);
        }
        formPanel.add(coverLamRangeFieldSet);

        pageLamRangeFields = new HashMap<Integer, CheckBox>();
        FieldSet pageLamRangeFieldSet = new FieldSet();
        pageLamRangeFieldSet.setHeading(constants.pageLamRange());
        List<Integer> pageLamRange = album.getPageLamRange();
        for (int pageLam : PageLamination.values.keySet()) {
            CheckBox checkBox = new CheckBox();
            checkBox.setHideLabel(true);
            checkBox.setBoxLabel(PageLamination.values.get(pageLam).getNonEmptyValue(
                    locale));
            checkBox.setValue(pageLamRange.contains(pageLam));
            pageLamRangeFields.put(pageLam, checkBox);
            pageLamRangeFieldSet.add(checkBox);
        }
        formPanel.add(pageLamRangeFieldSet);

        sizeField = new TextField<String>();
        sizeField.setFieldLabel(constants.sizeField());
        sizeField.setValue(album.getSize());
        sizeField.addPlugin(new HintPlugin(constants.sizeHint()));
        formPanel.add(sizeField);

        widthField = new NumberField();
        widthField.setPropertyEditorType(Integer.class);
        widthField.setFieldLabel(constants.widthField());
        widthField.setValue(album.getWidth());
        formPanel.add(widthField);

        heightField = new NumberField();
        heightField.setPropertyEditorType(Integer.class);
        heightField.setFieldLabel(constants.heightField());
        heightField.setValue(album.getHeight());
        formPanel.add(heightField);

        jpegFolderField = new TextField<String>();
        jpegFolderField.setFieldLabel(constants.jpegFolderField());
        jpegFolderField.setValue(album.getJpegFolder());
        jpegFolderField.addPlugin(new HintPlugin(constants.jpegFolderHint()));
        formPanel.add(jpegFolderField);

        coverSizeField = new TextField<String>();
        coverSizeField.setFieldLabel(constants.coverSizeField());
        coverSizeField.setValue(album.getCoverSize());
        coverSizeField.setVisible(album.isSeparateCover());
        coverSizeField.addPlugin(new HintPlugin(constants.coverSizeHint()));
        formPanel.add(coverSizeField);

        pdfCoverWidthField = new NumberField();
        pdfCoverWidthField.setPropertyEditorType(Integer.class);
        pdfCoverWidthField.setFieldLabel(constants.pdfCoverWidthField());
        pdfCoverWidthField.setValue(album.getPdfCoverWidth());
        pdfCoverWidthField.setVisible(album.isSeparateCover());
        formPanel.add(pdfCoverWidthField);

        pdfCoverHeightField = new NumberField();
        pdfCoverHeightField.setPropertyEditorType(Integer.class);
        pdfCoverHeightField.setFieldLabel(constants.pdfCoverHeightField());
        pdfCoverHeightField.setValue(album.getPdfCoverHeight());
        pdfCoverHeightField.setVisible(album.isSeparateCover());
        formPanel.add(pdfCoverHeightField);

        jpegCoverFolderField = new TextField<String>();
        jpegCoverFolderField.setFieldLabel(constants.jpegCoverFolderField());
        jpegCoverFolderField.setValue(album.getJpegCoverFolder());
        jpegCoverFolderField.setVisible(album.isSeparateCover());
        jpegCoverFolderField.addPlugin(new HintPlugin(constants
                .jpegCoverFolderHint()));
        formPanel.add(jpegCoverFolderField);

        innerCropField = new NumberField();
        innerCropField.setPropertyEditorType(Integer.class);
        innerCropField.setFieldLabel(constants.innerCropField());
        innerCropField.setValue(album.getInnerCrop());
        innerCropField.setVisible(album.isSeparateCover());
        formPanel.add(innerCropField);

        frontUpperCropField = new NumberField();
        frontUpperCropField.setPropertyEditorType(Integer.class);
        frontUpperCropField.setFieldLabel(constants.frontUpperCropField());
        frontUpperCropField.setValue(album.getFrontUpperCrop());
        frontUpperCropField.setVisible(album.isSeparateCover());
        formPanel.add(frontUpperCropField);

        frontBottomCropField = new NumberField();
        frontBottomCropField.setPropertyEditorType(Integer.class);
        frontBottomCropField.setFieldLabel(constants.frontBottomCropField());
        frontBottomCropField.setValue(album.getFrontBottomCrop());
        frontBottomCropField.setVisible(album.isSeparateCover());
        formPanel.add(frontBottomCropField);

        frontLeftCropField = new NumberField();
        frontLeftCropField.setPropertyEditorType(Integer.class);
        frontLeftCropField.setFieldLabel(constants.frontLeftCropField());
        frontLeftCropField.setValue(album.getFrontLeftCrop());
        frontLeftCropField.setVisible(album.isSeparateCover());
        formPanel.add(frontLeftCropField);

        frontRightCropField = new NumberField();
        frontRightCropField.setPropertyEditorType(Integer.class);
        frontRightCropField.setFieldLabel(constants.frontRightCropField());
        frontRightCropField.setValue(album.getFrontRightCrop());
        frontRightCropField.setVisible(album.isSeparateCover());
        formPanel.add(frontRightCropField);

        backUpperCropField = new NumberField();
        backUpperCropField.setPropertyEditorType(Integer.class);
        backUpperCropField.setFieldLabel(constants.backUpperCropField());
        backUpperCropField.setValue(album.getBackUpperCrop());
        backUpperCropField.setVisible(album.isSeparateCover());
        formPanel.add(backUpperCropField);

        backBottomCropField = new NumberField();
        backBottomCropField.setPropertyEditorType(Integer.class);
        backBottomCropField.setFieldLabel(constants.backBottomCropField());
        backBottomCropField.setValue(album.getBackBottomCrop());
        backBottomCropField.setVisible(album.isSeparateCover());
        formPanel.add(backBottomCropField);

        backLeftCropField = new NumberField();
        backLeftCropField.setPropertyEditorType(Integer.class);
        backLeftCropField.setFieldLabel(constants.backLeftCropField());
        backLeftCropField.setValue(album.getBackLeftCrop());
        backLeftCropField.setVisible(album.isSeparateCover());
        formPanel.add(backLeftCropField);

        backRightCropField = new NumberField();
        backRightCropField.setPropertyEditorType(Integer.class);
        backRightCropField.setFieldLabel(constants.backRightCropField());
        backRightCropField.setValue(album.getBackRightCrop());
        backRightCropField.setVisible(album.isSeparateCover());
        formPanel.add(backRightCropField);

        hardcoverField = new CheckBox();
        hardcoverField.setBoxLabel("");
        hardcoverField.setFieldLabel(constants.hardcoverField());
        hardcoverField.setValue(album.isHardcover());
        hardcoverField.setVisible(album.isSeparateCover());
        formPanel.add(hardcoverField);

        FieldSet safeAreaFieldSet = new FieldSet();
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(150);
        safeAreaFieldSet.setLayout(layout);
        safeAreaFieldSet.setHeading(constants.safeAreaFieldSet());

        upperSafeAreaField = new FloatField();
        upperSafeAreaField.setFieldLabel(constants.upperSafeAreaField());
        upperSafeAreaField.setValue(album.getUpperSafeArea());
        safeAreaFieldSet.add(upperSafeAreaField);

        bottomSafeAreaField = new FloatField();
        bottomSafeAreaField.setFieldLabel(constants.bottomSafeAreaField());
        bottomSafeAreaField.setValue(album.getBottomSafeArea());
        safeAreaFieldSet.add(bottomSafeAreaField);

        innerSafeAreaField = new FloatField();
        innerSafeAreaField.setFieldLabel(constants.innerSafeAreaField());
        innerSafeAreaField.setValue(album.getInnerSafeArea());
        safeAreaFieldSet.add(innerSafeAreaField);

        outerSafeAreaField = new FloatField();
        outerSafeAreaField.setFieldLabel(constants.outerSafeAreaField());
        outerSafeAreaField.setValue(album.getOuterSafeArea());
        safeAreaFieldSet.add(outerSafeAreaField);

        upperCoverSafeAreaField = new FloatField();
        upperCoverSafeAreaField.setFieldLabel(constants.upperCoverSafeAreaField());
        upperCoverSafeAreaField.setValue(album.getUpperCoverSafeArea());
        safeAreaFieldSet.add(upperCoverSafeAreaField);

        bottomCoverSafeAreaField = new FloatField();
        bottomCoverSafeAreaField
                .setFieldLabel(constants.bottomCoverSafeAreaField());
        bottomCoverSafeAreaField.setValue(album.getBottomCoverSafeArea());
        safeAreaFieldSet.add(bottomCoverSafeAreaField);

        leftCoverSafeAreaField = new FloatField();
        leftCoverSafeAreaField.setFieldLabel(constants.leftCoverSafeAreaField());
        leftCoverSafeAreaField.setValue(album.getLeftCoverSafeArea());
        safeAreaFieldSet.add(leftCoverSafeAreaField);

        rightCoverSafeAreaField = new FloatField();
        rightCoverSafeAreaField.setFieldLabel(constants.rightCoverSafeAreaField());
        rightCoverSafeAreaField.setValue(album.getRightCoverSafeArea());
        safeAreaFieldSet.add(rightCoverSafeAreaField);

        formPanel.add(safeAreaFieldSet);

        /**
         * Editor's parameters fieldSet
         */
        FieldSet editorParamsFieldSet = new FieldSet();
        FormLayout editorParamslayout = new FormLayout();
        editorParamslayout.setLabelWidth(150);
        editorParamsFieldSet.setLayout(editorParamslayout);
        editorParamsFieldSet.setHeading(constants.editorParamsFieldSet());

        coverWidthEditorParamsField = new NumberField();
        coverWidthEditorParamsField.setPropertyEditorType(Integer.class);
        coverWidthEditorParamsField.setFieldLabel(constants.coverWidthEditorParamsField());
        coverWidthEditorParamsField.setValue(album.getCoverWidth());
        coverWidthEditorParamsField.setVisible(album.isSeparateCover());
        editorParamsFieldSet.add(coverWidthEditorParamsField);

        coverHeightEditorParamsField = new NumberField();
        coverHeightEditorParamsField.setPropertyEditorType(Integer.class);
        coverHeightEditorParamsField.setFieldLabel(constants.coverHeightEditorParamsField());
        coverHeightEditorParamsField.setValue(album.getCoverHeight());
        coverHeightEditorParamsField.setVisible(album.isSeparateCover());
        editorParamsFieldSet.add(coverHeightEditorParamsField);

        LabelField barcodeCoordinatesHintLabel = new LabelField(constants.barcodeCoordinatesHint());
        barcodeCoordinatesHintLabel.setHideLabel(true);
        editorParamsFieldSet.add(barcodeCoordinatesHintLabel, new FormData(600, -1));
        barcodeXEditorParamsField = new NumberField();
        barcodeXEditorParamsField.setPropertyEditorType(Integer.class);
        barcodeXEditorParamsField.setFieldLabel(constants.barcodeXParamsField());
        barcodeXEditorParamsField.setValue(album.getBarcodeX());
        editorParamsFieldSet.add(barcodeXEditorParamsField);

        barcodeYEditorParamsField = new NumberField();
        barcodeYEditorParamsField.setPropertyEditorType(Integer.class);
        barcodeYEditorParamsField.setFieldLabel(constants.barcodeYParamsField());
        barcodeYEditorParamsField.setValue(album.getBarcodeY());
        editorParamsFieldSet.add(barcodeYEditorParamsField);

        formPanel.add(editorParamsFieldSet);

        /**
         * MPhoto's parameters fieldSet
         */
        FieldSet mphotoParamsFieldSet = new FieldSet();
        FormLayout mphotoParamslayout = new FormLayout();
        mphotoParamslayout.setLabelWidth(150);
        mphotoParamsFieldSet.setLayout(mphotoParamslayout);
        mphotoParamsFieldSet.setHeading(constants.mphotoParamsFieldSet());

        mphotoParamsFieldSet.add(barcodeCoordinatesHintLabel, new FormData(600, -1));
        barcodeXMPhotoParamsField = new NumberField();
        barcodeXMPhotoParamsField.setPropertyEditorType(Integer.class);
        barcodeXMPhotoParamsField.setFieldLabel(constants.barcodeXParamsField());
        barcodeXMPhotoParamsField.setValue(album.getMphotoBarcodeX());
        mphotoParamsFieldSet.add(barcodeXMPhotoParamsField);

        barcodeYMPhotoParamsField = new NumberField();
        barcodeYMPhotoParamsField.setPropertyEditorType(Integer.class);
        barcodeYMPhotoParamsField.setFieldLabel(constants.barcodeYParamsField());
        barcodeYMPhotoParamsField.setValue(album.getMphotoBarcodeY());
        mphotoParamsFieldSet.add(barcodeYMPhotoParamsField);

        formPanel.add(mphotoParamsFieldSet);

        coverNameField = new TextField<String>();
        coverNameField.setFieldLabel(constants.coverNameField());
        coverNameField.setValue(album.getCoverName());
        formPanel.add(coverNameField);

        nonEditorField = new BooleanField(constants.nonEditorField(), album.isNonEditor(), formPanel);
        nonCalcField = new BooleanField(constants.nonCalcField(), album.isNonCalc(), formPanel);
        trialAlbumField = new BooleanField(constants.trialAlbumField(), album.isTrialAlbum(), formPanel);
        trialDeliveryField = new BooleanField(constants.trialDeliveryField(), album.isTrialDelivery(), formPanel);
        addressPrintedField = new BooleanField(constants.addressPrintedField(), album.isAddressPrinted(), formPanel);
        lastPageTemplate = new XTextField(constants.lastPageTemplate(), true, album.getLastPageTemplate(),
            formPanel);

        approxProdTime = new TextField<String>();
        approxProdTime.setFieldLabel(constants.approxProdTime());
        approxProdTime.setValue(album.getApproxProdTime());
        formPanel.add(approxProdTime);

        calcComment = new TextArea();
        calcComment.setFieldLabel(constants.calcComment());
        calcComment.setValue(album.getCalcComment());
        formPanel.add(calcComment);

        lastSpreadBarcodeField = new BooleanField(constants.lastSpreadBarcodeField(), album.isBarcodeOnTheLastSpread(),
            formPanel);
        lastSpreadBarcodeField.setVisible(album.isTablet());

        // Special Offer block (see http://jira.minogin.ru/browse/IMAGEBOOK-224)
        specialOfferFieldSet = new FieldSet();
        specialOfferFieldSet.setHeading(constants.specialOfferFieldSet());

        FormLayout specialOfferFormLayout = new FormLayout();
        specialOfferFormLayout.setLabelWidth(150);
        specialOfferFieldSet.setLayout(specialOfferFormLayout);

        specialOfferFieldSet.setCheckboxToggle(true);
        specialOfferFieldSet.setExpanded(album.isHasSpecialOffer());
        formPanel.add(specialOfferFieldSet);

        specialOfferFieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
            @Override
            public void handleEvent(FieldSetEvent be) {
                minAlbumsCountForDiscountField.setAllowBlank(true);
                imagebookDiscountField.setAllowBlank(true);
                phDiscountField.setAllowBlank(true);
                minAlbumsCountForDiscountField.validate();
                imagebookDiscountField.validate();
                phDiscountField.validate();
            }
        });
        specialOfferFieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
            @Override
            public void handleEvent(FieldSetEvent be) {
                minAlbumsCountForDiscountField.setAllowBlank(false);
                imagebookDiscountField.setAllowBlank(false);
                phDiscountField.setAllowBlank(false);
                minAlbumsCountForDiscountField.validate();
                imagebookDiscountField.validate();
                phDiscountField.validate();
            }
        });

        minAlbumsCountForDiscountField = new NumberField();
        minAlbumsCountForDiscountField.setPropertyEditorType(Integer.class);
        minAlbumsCountForDiscountField.setAllowNegative(false);
        minAlbumsCountForDiscountField.setFieldLabel(constants.minAlbumsCountForDiscountField());
        minAlbumsCountForDiscountField.setAllowBlank(!album.isHasSpecialOffer());
        minAlbumsCountForDiscountField.setValue(album.getMinAlbumsCountForDiscount());
        specialOfferFieldSet.add(minAlbumsCountForDiscountField);

        imagebookDiscountField = new NumberField();
        imagebookDiscountField.setPropertyEditorType(Integer.class);
        imagebookDiscountField.setAllowNegative(false);
        imagebookDiscountField.setFieldLabel(constants.imagebookDiscountField());
        imagebookDiscountField.setAllowBlank(!album.isHasSpecialOffer());
        imagebookDiscountField.setValue(album.getImagebookDiscount());
        specialOfferFieldSet.add(imagebookDiscountField);

        phDiscountField = new NumberField();
        phDiscountField.setPropertyEditorType(Integer.class);
        phDiscountField.setAllowNegative(false);
        phDiscountField.setFieldLabel(constants.phDiscountField());
        phDiscountField.setAllowBlank(!album.isHasSpecialOffer());
        phDiscountField.setValue(album.getPhDiscount());
        specialOfferFieldSet.add(phDiscountField);

        flyleafBox = new CheckBox();
        flyleafBox.setHideLabel(true);
        flyleafBox.setBoxLabel(constants.flyleafs());
        flyleafBox.setValue(album.isFlyleafs());
        formPanel.add(flyleafBox);

        supportsVellumBox = new CheckBox();
        supportsVellumBox.setHideLabel(true);
        supportsVellumBox.setBoxLabel(constants.vellum());
        supportsVellumBox.setValue(album.isSupportsVellum());
        formPanel.add(supportsVellumBox);
    }

    @Override
    public void showColorRangeField(List<Color> colors, String locale) {
        colorRangeFields = new HashMap<Color, CheckBox>();
        List<Integer> colorRange = album.getColorRange();
        for (Color color : colors) {
            CheckBox checkBox = new CheckBox();
            checkBox.setHideLabel(true);
            checkBox.setBoxLabel(color.getName().getNonEmptyValue(locale));
            checkBox.setValue(colorRange.contains(color.getNumber()));
            colorRangeFields.put(color, checkBox);
            colorRangeFieldSet.add(checkBox);
        }
        colorRangeFieldSet.layout();
    }

    private void fetchAlbum(Album album) {
        album.setType(typeField.getXValue());
        album.setNumber((Integer) numberField.getValue());
        album.setName(nameFieldRenderer.getValue());
        album.setAvailability(availField.getXValue());
        album.setBlockFormat(blockFormatField.getValue());
        album.setBinding(bindingField.getXValue());
        album.setCover(coverField.getXValue());
        album.setPaper(paperField.getXValue());
        album.setMultiplicity(multiplicityField.getXValue());
        album.setMinPageCount((Integer) minPageCountField.getValue());
        album.setMaxPageCount((Integer) maxPageCountField.getValue());
        album.setMinQuantity((Integer) minQuantityField.getValue());
        album.setPagePrice(pagePriceField.getValue());
        album.setBasePrice(basePriceField.getValue());
        album.setCoverLaminationPrice(coverLaminationPriceField.getValue());
        album.setPhPagePrice(phPagePriceField.getValue());
        album.setPhBasePrice(phBasePriceField.getValue());
        album.setPhCoverLaminationPrice(phCoverLaminationPriceField.getValue());
        album.setPageLaminationPrice(pageLaminationPriceField.getValue());
        album.setPhPageLaminationPrice(phPageLaminationPriceField.getValue());

        List<Integer> colorRange = new ArrayList<Integer>();
        for (Color color : colorRangeFields.keySet()) {
            CheckBox checkBox = colorRangeFields.get(color);
            if (checkBox.getValue())
                colorRange.add(color.getNumber());
        }
        album.setColorRange(colorRange);

        List<Integer> coverLamRange = new ArrayList<Integer>();
        for (int coverLam : coverLamRangeFields.keySet()) {
            CheckBox checkBox = coverLamRangeFields.get(coverLam);
            if (checkBox.getValue())
                coverLamRange.add(coverLam);
        }
        album.setCoverLamRange(coverLamRange);

        List<Integer> pageLamRange = new ArrayList<Integer>();
        for (int pageLam : pageLamRangeFields.keySet()) {
            CheckBox checkBox = pageLamRangeFields.get(pageLam);
            if (checkBox.getValue())
                pageLamRange.add(pageLam);
        }
        album.setPageLamRange(pageLamRange);

        album.setHardcover(hardcoverField.getValue());
        album.setSize(sizeField.getValue());
        album.setWidth((Integer) widthField.getValue());
        album.setHeight((Integer) heightField.getValue());
        album.setCoverSize(coverSizeField.getValue());
        album.setPdfCoverWidth((Integer) pdfCoverWidthField.getValue());
        album.setPdfCoverHeight((Integer) pdfCoverHeightField.getValue());
        album.setJpegFolder(jpegFolderField.getValue());
        album.setJpegCoverFolder(jpegCoverFolderField.getValue());
        album.setInnerCrop((Integer) innerCropField.getValue());
        album.setFrontUpperCrop((Integer) frontUpperCropField.getValue());
        album.setFrontBottomCrop((Integer) frontBottomCropField.getValue());
        album.setFrontLeftCrop((Integer) frontLeftCropField.getValue());
        album.setFrontRightCrop((Integer) frontRightCropField.getValue());
        album.setBackUpperCrop((Integer) backUpperCropField.getValue());
        album.setBackBottomCrop((Integer) backBottomCropField.getValue());
        album.setBackLeftCrop((Integer) backLeftCropField.getValue());
        album.setBackRightCrop((Integer) backRightCropField.getValue());
        album.setBlockWidth((Integer) blockWidthField.getValue());
        album.setBlockHeight((Integer) blockHeightField.getValue());

        album.setUpperSafeArea(upperSafeAreaField.getValue());
        album.setBottomSafeArea(bottomSafeAreaField.getValue());
        album.setInnerSafeArea(innerSafeAreaField.getValue());
        album.setOuterSafeArea(outerSafeAreaField.getValue());
        album.setUpperCoverSafeArea(upperCoverSafeAreaField.getValue());
        album.setBottomCoverSafeArea(bottomCoverSafeAreaField.getValue());
        album.setLeftCoverSafeArea(leftCoverSafeAreaField.getValue());
        album.setRightCoverSafeArea(rightCoverSafeAreaField.getValue());

        album.setCoverWidth((Integer) coverWidthEditorParamsField.getValue());
        album.setCoverHeight((Integer) coverHeightEditorParamsField.getValue());

        album.setBarcodeX((Integer) barcodeXEditorParamsField.getValue());
        album.setBarcodeY((Integer) barcodeYEditorParamsField.getValue());

        album.setMphotoBarcodeX((Integer) barcodeXMPhotoParamsField.getValue());
        album.setMphotoBarcodeY((Integer) barcodeYMPhotoParamsField.getValue());

        album.setCoverName(coverNameField.getValue());
        album.setNonEditor(nonEditorField.getValue());
        album.setTrialAlbum(trialAlbumField.getValue());
        album.setTrialDelivery(trialDeliveryField.getValue());
        album.setAddressPrinted(addressPrintedField.getValue());
        album.setLastPageTemplate(lastPageTemplate.getValue());
        album.setApproxProdTime(approxProdTime.getValue());
        album.setCalcComment(calcComment.getValue());
        album.setBarcodeOnTheLastSpread(lastSpreadBarcodeField.getValue());
        album.setNonCalc(nonCalcField.getValue());

        album.setHasSpecialOffer(specialOfferFieldSet.isExpanded());
        album.setMinAlbumsCountForDiscount((Integer) minAlbumsCountForDiscountField.getValue());
        album.setImagebookDiscount((Integer) imagebookDiscountField.getValue());
        album.setPhDiscount((Integer) phDiscountField.getValue());

        if (usersStore != null) {
            HashSet<User> usersList = new HashSet<User>();
            for (int i = 0; i < usersStore.getCount(); i++) {
                usersList.add(usersStore.getAt(i).getBean());
            }
            album.setAccessedUsers(usersList);
        }

        album.setFlyleafs(flyleafBox.getValue());
        album.setSupportsVellum(supportsVellumBox.getValue());
    }

    @Override
    public void hideAddForm() {
        addWindow.hide();
    }

    @Override
    public void confirmDeleteAlbums(final List<Album> albums) {
        new ConfirmMessageBox(appConstants.warning(), constants.confirmDeleteAlbums(),
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        send(new DeleteAlbumsMessage(albums));
                    }
                });
    }

    @Override
    public void alertNoAlbumsToDelete() {
        MessageBox.alert(appConstants.warning(), constants.noAlbumsToDelete(), null);
    }

    @Override
    public void alertAlbumUsed() {
        MessageBox.alert(appConstants.error(), constants.albumUsed(), null);
    }

    private void edit() {
        Album selectedAlbum = getSelectedAlbum();
        if (selectedAlbum != null) {
            send(new ShowEditAlbumFormMessage(selectedAlbum));
        }
    }

    @Override
    public void showEditForm(final Album album, String locale, Collection<String> locales) {
        this.album = album;

        editWindow = new Window();
        editWindow.setHeading(constants.editWindowHeading());
        editWindow.setSize(800, 600);
        editWindow.setModal(true);
        editWindow.setScrollMode(Scroll.AUTO);

        TabPanel tabPanel = new TabPanel();
        tabPanel.setHeight(520);
        tabPanel.setBorders(false);

        // Tabs
        addAlbumTab(tabPanel, album, locale, locales);
        addAccessUserTab(tabPanel, album);
        editWindow.add(tabPanel);

        editWindow.addButton(new Button(appConstants.cancel(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        editWindow.hide();
                    }
                }));
        editWindow.show();
    }

    private void addAlbumTab(TabPanel tabPanel, final Album album, final String locale, Collection<String> locales) {
        FormPanel formPanel = new FormPanel();
        formPanel.setLabelWidth(150);
        formPanel.setHeaderVisible(false);
        addFields(formPanel, album, locale, locales);

        Button saveButton = new Button(appConstants.save(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        if (radioUserAll != null && radioUserAll.getValue()) {
                            usersStore.removeAll();
                        }
                        fetchAlbum(album);
                        send(new UpdateAlbumMessage(album));
                    }
                });
        new FormButtonBinding(formPanel).addButton(saveButton);
        editWindow.addButton(saveButton);

        TabItem albumTabItem = new TabItem(constants.albumTab());
        initTabItem(formPanel, tabPanel, albumTabItem);
    }

    private void addAccessUserTab(TabPanel tabPanel, final Album album) {
        FormPanel formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);
        formPanel.setLayout(new FitLayout());

        // radio
        final RadioGroup radioGroup = new RadioGroup();
        radioGroup.setOrientation(Style.Orientation.VERTICAL);
        radioGroup.setHeight(100);
        radioGroup.setAutoHeight(true);

        radioUserAll = new Radio();
        radioUserAll.setBoxLabel(userConstants.toAll());
        radioGroup.add(radioUserAll);

        final Radio radioUserChosen = new Radio();
        radioUserChosen.setBoxLabel(userConstants.chosenUsers());
        radioGroup.add(radioUserChosen);

        formPanel.add(radioGroup);

        // select user
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);

        userField = new ObjectField<User>() {
            @Override
            protected String render(User user) {
                return user == null ? null : user.getUserName() + " - " + user.getFullName();
            }

            @Override
            protected void load(int offset, int limit, String query, ObjectFieldCallback<User> callback) {
                userCallback = callback;
                presenter.loadUsers(offset, limit, query);
            }
        };

        userField.setEmptyText(userConstants.userEmptyText());
        userField.setAllowBlank(false);
        userField.setWidth(500);
        hp.add(userField);

        final Button addUserButton = new Button(appConstants.add(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (isUserExists(userField.getXValue(), usersStore)) {
                    MessageBox.alert(appConstants.error(), constants.userAlreadyAdded(), null);
                    return;
                }
                addUser(userField.getXValue(), usersStore);
            }
        });
        hp.add(addUserButton);
        formPanel.add(hp, new FlowData(0, 0, 10, 0));

        final ContentPanel gridPanel = new ContentPanel();
        gridPanel.setHeading(userConstants.availableForUsers());

        // init users grid
        usersStore = new ListStore<BeanModel<User>>();
        loadUsers(album, usersStore);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig(USER_NAME, userConstants.userName(), 300));
        columns.add(new ColumnConfig(FULL_NAME, userConstants.name(), 350));

        final Grid<BeanModel<User>> grid = new Grid<BeanModel<User>>(usersStore, new ColumnModel(columns));
        grid.setAutoWidth(true);
        grid.setHeight(350);
        grid.setStripeRows(true);
        grid.setAutoExpandColumn(FULL_NAME);

        ToolBar toolBar = new ToolBar();
        toolBar.add(new Button(appConstants.delete(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<BeanModel<User>> selectedItems = grid.getSelectionModel().getSelectedItems();
                if (selectedItems.isEmpty()) {
                    MessageBox.alert(appConstants.error(), userConstants.noUsersToDelete(), null);
                    return;
                }

                for (BeanModel<User> item : selectedItems) {
                    usersStore.remove(item);
                }
            }
        }));

        gridPanel.add(toolBar);
        gridPanel.add(grid);
        formPanel.add(gridPanel);

        if (usersStore.getCount() > 0) {
            radioUserChosen.setValue(true);
            radioUserAll.setValue(false);
        } else {
            radioUserChosen.setValue(false);
            radioUserAll.setValue(true);
            userField.setVisible(false);
            addUserButton.setVisible(false);
            gridPanel.setVisible(false);
        }

        radioGroup.addListener(Events.Change, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                if (radioGroup.getValue().equals(radioUserChosen)) {
                    userField.setVisible(true);
                    addUserButton.setVisible(true);
                    gridPanel.setVisible(true);
                } else {
                    userField.setVisible(false);
                    addUserButton.setVisible(false);
                    gridPanel.setVisible(false);
                }
            }
        });

        TabItem accessTabItem = new TabItem(userConstants.accessTab());
        initTabItem(formPanel, tabPanel, accessTabItem);
    }

    @Override
    public void showUsers(List<User> users, int offset, long total, String locale) {
        userCallback.onLoaded(users, offset, total);
    }

    private void initTabItem(FormPanel formPanel, TabPanel tabPanel, TabItem tabItem) {
        tabItem.setAutoWidth(true);
        tabItem.setAutoHeight(true);
        tabItem.setBorders(false);
        tabItem.setScrollMode(Scroll.AUTO);
        tabItem.add(formPanel);
        tabPanel.add(tabItem);
    }

    private boolean isUserExists(User user, ListStore<BeanModel<User>> store) {
        for (BeanModel<User> userModel : store.getModels()) {
            if (userModel.getBean().equals(user)) {
                return true;
            }
        }
        return false;
    }

    private void loadUsers(Album album, ListStore<BeanModel<User>> store) {
        for (User user : album.getAccessedUsers()) {
            addUser(user, store);
        }
    }

    private void addUser(User user, ListStore<BeanModel<User>> store) {
        user.setTransient(LOGIN, user.getUserName());
        user.setTransient(FULL_NAME, user.getFullName());
        store.add(new BeanModel<User>(user));
    }

    @Override
    public void hideEditForm() {
        editWindow.hide();
    }

    @Override
    public Album getSelectedAlbum() {
        BeanModel<Album> selectedItem = grid.getSelectionModel().getSelectedItem();
        return selectedItem != null ? selectedItem.getBean() : null;
    }

    @Override
    public void productSelectionEmpty() {
        MessageBox.alert(appConstants.error(), constants.albumSelectionEmpty(), null);
    }
}
