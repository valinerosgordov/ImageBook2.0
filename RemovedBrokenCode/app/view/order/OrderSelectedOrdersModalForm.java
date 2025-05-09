package ru.imagebook.client.app.view.order;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.app.view.common.ModalDialog;
import ru.imagebook.client.app.view.common.XCheckBox;

/**
 * Created by Rifat on 20.12.2016.
 */
public class OrderSelectedOrdersModalForm implements IsWidget {
    interface OrderSelectedOrdersUiBinder extends UiBinder<Widget, OrderSelectedOrdersModalForm> {
    }

    private static OrderSelectedOrdersUiBinder uiBinder = GWT.create(OrderSelectedOrdersUiBinder.class);

    @UiField
    ModalDialog orderSelectedOrdersModalForm;

    @UiField
    XCheckBox acceptXCheckBox;

    private final HandlerManager handlerManager = new HandlerManager(this);

    @Override
    public Widget asWidget() {
        Widget ui = uiBinder.createAndBindUi(this);
        acceptXCheckBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                orderSelectedOrdersModalForm.setOkButtonEnabled(acceptXCheckBox.getValue());
            }
        });
        orderSelectedOrdersModalForm.setOkButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handlerManager.fireEvent(new OrderSelectedOrdersEvent());
            }
        });
        orderSelectedOrdersModalForm.setCancelButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        return ui;
    }

    public void show() {
        acceptXCheckBox.setValue(false);
        orderSelectedOrdersModalForm.setOkButtonEnabled(false);
        orderSelectedOrdersModalForm.show();
    }

    public void hide() {
        orderSelectedOrdersModalForm.hide();
    }

    public HandlerRegistration addOrderSelectedOrdersEventHandler(OrderSelectedOrdersEventHandler handler) {
        return handlerManager.addHandler(OrderSelectedOrdersEvent.TYPE, handler);
    }
}
