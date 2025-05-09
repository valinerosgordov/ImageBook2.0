package ru.imagebook.client.app.view.order;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import ru.imagebook.client.app.view.common.ModalDialog;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.minogin.core.client.i18n.locale.Locales;

import java.util.List;
import java.util.Map;

public class CreateAlbumForm implements IsWidget {
    private Map<Integer, List<Product>> productsMap;

    interface CreateAlbumFormUiBinder extends UiBinder<Widget, CreateAlbumForm> {
    }

    private final HandlerManager handlerManager = new HandlerManager(this);

    @UiField
    ModalDialog createAlbumForm;
    @UiField
    ListBox typeField;
    @UiField
    ListBox productField;
    @UiField
    ListBox pageCountField;

    private static CreateAlbumFormUiBinder uiBinder = GWT.create(CreateAlbumFormUiBinder.class);

    @Override
    public Widget asWidget() {
        Widget ui = uiBinder.createAndBindUi(this);

        createAlbumForm.setOkButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handlerManager.fireEvent(new CreateAlbumEvent(
                        new Integer(productField.getValue(productField.getSelectedIndex())),
                        new Integer(pageCountField.getValue(pageCountField.getSelectedIndex()))
                ));
            }
        });

        createAlbumForm.setCancelButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        return ui;
    }

    public void show(Map<Integer, List<Product>> products) {
        this.productsMap = products;

        typeField.clear();
        for (Integer type : products.keySet()) {
            typeField.addItem(ProductType.values.get(type).get(Locales.RU), type + "");
        }
        typeField.setSelectedIndex(0);
        updateProducts();

        createAlbumForm.show();
    }

    public void hide() {
        createAlbumForm.hide();
    }

    @UiHandler("typeField")
    public void onTypeFieldChange(ChangeEvent event) {
        updateProducts();
    }

    @UiHandler("productField")
    public void onProductFieldChange(ChangeEvent event) {
        updatePageCount();
    }

    private void updateProducts() {
        Integer type = new Integer(typeField.getValue(typeField.getSelectedIndex()));
        List<Product> products = this.productsMap.get(type);

        productField.clear();
        for (Product product : products) {
            productField.addItem(product.getName().get(Locales.RU), product.getId() + "");
        }
        productField.setSelectedIndex(0);
        updatePageCount();
    }

    private void updatePageCount() {
        Integer type = new Integer(typeField.getValue(typeField.getSelectedIndex()));
        List<Product> products = this.productsMap.get(type);
        Product product = products.get(productField.getSelectedIndex());

        pageCountField.clear();
        for (int i = product.getMinPageCount(); i <= product.getMaxPageCount(); i += product.getMultiplicity()) {
            pageCountField.addItem(i + "", i + "");
        }
        pageCountField.setSelectedIndex(0);
    }

    public HandlerRegistration addCreateAlbumEventHandler(CreateAlbumEventHandler handler) {
        return handlerManager.addHandler(CreateAlbumEvent.TYPE, handler);
    }
}
