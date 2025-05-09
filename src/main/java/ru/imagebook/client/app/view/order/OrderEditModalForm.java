package ru.imagebook.client.app.view.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.app.view.common.ModalDialog;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Product;


public class OrderEditModalForm implements IsWidget {
    interface OrderEditFormUiBinder extends UiBinder<Widget, OrderEditModalForm> {
    }

    @UiField
    ModalDialog orderEditModalForm;
    @UiField
    HTMLPanel colorFieldGroup;
    @UiField
    ListBox colorField;
    @UiField
    HTMLPanel coverLamFieldGroup;
    @UiField
    ListBox coverLamField;
    @UiField
    HTMLPanel pageLamFieldGroup;
    @UiField
    ListBox pageLamField;

    private static OrderEditFormUiBinder uiBinder = GWT.create(OrderEditFormUiBinder.class);

    private final HandlerManager handlerManager = new HandlerManager(this);

    private Order<?> order;

    @Override
    public Widget asWidget() {
        Widget ui = uiBinder.createAndBindUi(this);
        orderEditModalForm.setOkButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                orderEditModalForm.setOkButtonEnabled(false);

                final Product product = order.getProduct();
                Integer colorId = fetchColorId(product);
                Integer coverLam = fetchCoverLam(product);
                Integer pageLam = fetchPageLam(product);

                handlerManager.fireEvent(
                    new SetOrderParamsEvent(order.getId(), colorId, coverLam, pageLam));
            }
        });
        orderEditModalForm.setCancelButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        return ui;
    }

    public void show(final Order<?> order, List<Color> colors, String locale) {
        this.order = order;

        initColorField(order, colors, locale);
        initCoverLamField(order, locale);
        initPageLamField(order, locale);

        orderEditModalForm.setOkButtonEnabled(true);
        orderEditModalForm.show();
    }

    private void initColorField(Order<?> order, List<Color> colors, String locale) {
        final Product product = order.getProduct();
        if (product.getColorRange().size() > 1) {
            colorFieldGroup.setVisible(true);

            Map<Integer, Color> colorsMap = new HashMap<Integer, Color>();
            for (Color color : colors) {
                colorsMap.put(color.getNumber(), color);
            }

            colorField.clear();
            for (int i = 0; i < product.getColorRange().size(); i++) {
                int colorNumber = product.getColorRange().get(i);
                Color color = colorsMap.get(colorNumber);
                colorField.addItem(color.getName().get(locale), color.getId() + "");
                if (order.getColor().getNumber() == colorNumber) {
                    colorField.setSelectedIndex(i);
                }
            }
        } else {
            colorFieldGroup.setVisible(false);
        }
    }

    private void initCoverLamField(Order<?> order, String locale) {
        final Product product = order.getProduct();
        if (product.getCoverLamRange().size() > 1) {
            coverLamFieldGroup.setVisible(true);

            coverLamField.clear();
            for (int i = 0; i < product.getCoverLamRange().size(); i++) {
                int coverLam = product.getCoverLamRange().get(i);
                coverLamField.addItem(CoverLamination.values.get(coverLam).get(locale), coverLam + "");
                if (order.getCoverLamination() == coverLam) {
                    coverLamField.setSelectedIndex(i);
                }
            }
        } else {
            coverLamFieldGroup.setVisible(false);
        }
    }

    private void initPageLamField(Order<?> order, String locale) {
        final Product product = order.getProduct();
        if (product.getPageLamRange().size() > 1) {
            pageLamFieldGroup.setVisible(true);

            pageLamField.clear();
            for (int i = 0; i < product.getPageLamRange().size(); i++) {
                int pageLam = product.getPageLamRange().get(i);
                pageLamField.addItem(PageLamination.values.get(pageLam).get(locale), pageLam + "");
                if (order.getPageLamination() == pageLam) {
                    pageLamField.setSelectedIndex(i);
                }
            }
        } else {
            pageLamFieldGroup.setVisible(false);
        }
    }

    public void hide() {
        orderEditModalForm.hide();
    }

    private Integer fetchPageLam(Product product) {
        return (product.getPageLamRange().size() > 1)
            ? new Integer(pageLamField.getValue(pageLamField.getSelectedIndex())) : null;
    }

    private Integer fetchCoverLam(Product product) {
        return (product.getCoverLamRange().size() > 1)
            ? new Integer(coverLamField.getValue(coverLamField.getSelectedIndex())) : null;
    }

    private Integer fetchColorId(Product product) {
        return (product.getColorRange().size() > 1)
            ? new Integer(colorField.getValue(colorField.getSelectedIndex())) : null;
    }

    public HandlerRegistration addSetOrderParamsEventHandler(SetOrderParamsEventHandler handler) {
        return handlerManager.addHandler(SetOrderParamsEvent.TYPE, handler);
    }
}
