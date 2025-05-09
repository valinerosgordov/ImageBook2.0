package ru.imagebook.client.admin.view.product;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style;
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
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.shared.model.ProductImage;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.IntegerField;
import ru.minogin.core.client.gxt.form.XTextArea;
import ru.minogin.core.client.text.StringUtil;

@Singleton
public class ProductImageViewImpl implements ProductImageView {
    @Inject
    private ProductImageConstants constants;
    @Inject
    private CommonConstants commonConstants;

    private ProductImagePresenter presenter;

    private XWindow window;
    private LayoutContainer panel;
    private FormPanel formPanel;
    private ContentPanel listPanel;

    protected AsyncCallback<PagingLoadResult<ProductImageModel>> callback;
    private BasePagingLoader<PagingLoadResult<ProductImageModel>> loader;
    private Grid<ProductImageModel> grid;
    private ListStore<ProductImageModel> store;
    private DataProxy<PagingLoadResult<ProductImageModel>> proxy;

    private IntegerField numberField;
    private TextArea descriptionField;
    private FileUploadField photoFileField;
    private com.google.gwt.user.client.ui.Image fullImage;

    @Override
    public void setPresenter(ProductImagePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void show(String productName) {
        window = new XWindow(constants.photosHeading(productName));
        window.setSize(1000, 500);

        panel = new LayoutContainer(new BorderLayout());

        formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);
        formPanel.setMethod(FormPanel.Method.POST);
        formPanel.setEncoding(FormPanel.Encoding.MULTIPART);
        formPanel.setAction(GWT.getHostPageBaseURL() + "product/uploadImageFile");
        formPanel.setScrollMode(Style.Scroll.AUTO);
        formPanel.setLabelWidth(150);
        formPanel.setFieldWidth(150);

        numberField = new IntegerField(constants.numberField(), false, formPanel);
        numberField.setMinValue(1);
        numberField.getMessages().setMinText(constants.incorrectNumberField());
        descriptionField = new XTextArea(constants.descriptionField(), true, formPanel);
        descriptionField.setHeight(50);
        photoFileField = new FileUploadField();
        photoFileField.setName("image");
        photoFileField.setFieldLabel(constants.photoFileField());
        photoFileField.getMessages().setBrowseText(constants.selectFile());
        photoFileField.setAllowBlank(false);
        formPanel.add(photoFileField);
        formPanel.add(new Button(commonConstants.save(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (StringUtil.isEmpty(photoFileField.getValue())) {
                    presenter.addEditFormSave();
                } else {
                    if (numberField.validate()) {
                        formPanel.submit();
                    }
                }
            }
        }));
        fullImage = new  com.google.gwt.user.client.ui.Image();
        fullImage.setSize("350px", "320px");
        fullImage.setVisible(false);
        BorderLayoutData imgData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        imgData.setMargins(new Margins(5));
        formPanel.add(fullImage, imgData);

        BorderLayoutData data = new BorderLayoutData(Style.LayoutRegion.CENTER, 0.4f);
        data.setSplit(true);
        data.setMargins(new Margins(5));
        panel.add(formPanel, data);

        listPanel = new ContentPanel(new FitLayout());
        listPanel.setHeaderVisible(false);
        ToolBar toolBar = new ToolBar();
        toolBar.add(new Button(commonConstants.delete(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                presenter.deleteButtonClicked();
            }
        }));
        listPanel.setTopComponent(toolBar);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig(ProductImage.NUMBER, constants.numberColumn(), 150));
        columns.add(new ColumnConfig(ProductImage.SOURCE_FILE, constants.sourceFileColumn(), 150));
        ColumnConfig pathConfig = new ColumnConfig(ProductImage.PATH, constants.photoColumn(), 150);
        pathConfig.setRenderer(new GridCellRenderer<ProductImageModel>() {
            @Override
            public Object render(ProductImageModel model,
                                 String property,
                                 ColumnData config,
                                 int rowIndex,
                                 int colIndex,
                                 ListStore<ProductImageModel> imageEntryModelListStore,
                                 Grid<ProductImageModel> imageEntryModelGrid) {
                String url = model.get(property);
                com.google.gwt.user.client.ui.Image img = new com.google.gwt.user.client.ui.Image(url);
                img.setSize("30px", "30px");
                return img;
            }
        });
        columns.add(pathConfig);
        proxy = new DataProxy<PagingLoadResult<ProductImageModel>>() {
            @Override
            public void load(
                    DataReader<PagingLoadResult<ProductImageModel>> reader,
                    Object loadConfig,
                    AsyncCallback<PagingLoadResult<ProductImageModel>> callback) {
                ProductImageViewImpl.this.callback = callback;
                PagingLoadConfig config = (PagingLoadConfig) loadConfig;
                presenter.photosLoaded(config.getOffset(), config.getLimit());
            }
        };
        loader = new BasePagingLoader<PagingLoadResult<ProductImageModel>>(proxy);
        store = new ListStore<ProductImageModel>(loader);

        grid = new Grid<ProductImageModel>(store, new ColumnModel(columns));
        GridView liveView = new GridView(); //use GridView to enable mouse scrolling
        // FixedLiveGridView liveView = new FixedLiveGridView();
        liveView.setEmptyText(constants.emptyGrid());
        grid.setView(liveView);
        grid.getView().setSortingEnabled(false);

        grid.addListener(Events.RowClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                presenter.photoSelected();
            }
        });

        listPanel.add(grid);


        data = new BorderLayoutData(Style.LayoutRegion.EAST, 0.6f);
        data.setSplit(true);
        data.setMargins(new Margins(5, 5, 5, 0));
        panel.add(listPanel, data);

        window.add(panel);
        window.show();

        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                super.windowHide(we);
                presenter.onHide();
            }
        });

        clearFields();
    }

    @Override
    public void showPhotos(List<ProductImage> images, int offset, long total) {
        List<ProductImageModel> photoModels = new ArrayList<ProductImageModel>();
        for (ProductImage image : images) {
            photoModels.add(new ProductImageModel(image));
        }
        callback.onSuccess(new BasePagingLoadResult<ProductImageModel>(photoModels, offset, (int) total));
    }

    @Override
    public void setFormValues(ProductImage photo) {
        numberField.setValue(photo.getNumber());
        descriptionField.setValue(photo.getDescription());
    }

    @Override
    public ProductImage getSelectedPhoto() {
        ProductImageModel model = grid.getSelectionModel().getSelectedItem();
        return model == null ? null : model.getEntity();
    }

    @Override
    public List<ProductImage> getSelectedPhotos() {
        List<ProductImage> entities = new ArrayList<ProductImage>();

        for (ProductImageModel model : grid.getSelectionModel().getSelectedItems()) {
            entities.add(model.getEntity());
        }

        return entities;
    }

    @Override
    public void reload() {
        loader.load();
        clearFields();
    }

    @Override
    public void fetch(ProductImage image) {
        image.setNumber(numberField.getValue());
        image.setDescription(descriptionField.getValue());
    }

    @Override
    public void hide() {
        window.hide();
    }

    @Override
    public void confirmDelete() {
        new ConfirmMessageBox(commonConstants.warning(), constants.confirmDelete(),
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        presenter.deletionConfirmed();
                    }
                });
    }

    @Override
    public void clearFields() {
        numberField.clear();
        descriptionField.clear();
        photoFileField.clear();
        fullImage.setVisible(false);
    }

    @Override
    public void updatePhoto(String path) {
        if (!StringUtil.isEmpty(path)) {
            fullImage.setUrl(path);
            fullImage.setVisible(true);
        } else {
            fullImage.setVisible(false);
        }
    }

    @Override
    public void emptySelection() {
        MessageBox.alert(commonConstants.error(), constants.emptySelection(), null);
    }
}
