package ru.imagebook.client.app.view.bonus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.app.view.common.ModalDialog;
import ru.imagebook.client.app.view.common.XTextArea;


public class BonusStatusRequestModalForm implements IsWidget {
    interface BonusStatusRequestUiBinder extends UiBinder<Widget, BonusStatusRequestModalForm> {
    }
    private static BonusStatusRequestUiBinder uiBinder = GWT.create(BonusStatusRequestUiBinder.class);

    @UiField
    ModalDialog bonusStatusRequestModalForm;
    @UiField
    XTextArea requestField;
    @UiField
    Label requestIsEmptyLabel;

    private final HandlerManager handlerManager = new HandlerManager(this);

    @Override
    public Widget asWidget() {
        Widget ui = uiBinder.createAndBindUi(this);

        bonusStatusRequestModalForm.setOkButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean isRequestFieldEmpty = requestField.isEmpty();
                requestField.setStyleName("has-error", isRequestFieldEmpty);
                requestIsEmptyLabel.setVisible(isRequestFieldEmpty);

                if (!isRequestFieldEmpty) {
                    bonusStatusRequestModalForm.setOkButtonEnabled(false);
                    handlerManager.fireEvent(new CreateBonusRequestEvent(requestField.getValue()));
                }
            }
        });

        bonusStatusRequestModalForm.setCancelButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        return ui;
    }

    public void show() {
        requestField.setValue(null);
        requestIsEmptyLabel.setVisible(false);
        bonusStatusRequestModalForm.setOkButtonEnabled(true);
        bonusStatusRequestModalForm.show();
    }

    public void hide() {
        bonusStatusRequestModalForm.hide();
    }

    public HandlerRegistration addBonusRequestEventHandler(CreateBonusRequestEventHandler handler) {
        return handlerManager.addHandler(CreateBonusRequestEvent.TYPE, handler);
    }
}
