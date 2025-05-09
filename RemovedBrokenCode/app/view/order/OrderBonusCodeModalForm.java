package ru.imagebook.client.app.view.order;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.app.view.common.ModalDialog;
import ru.imagebook.client.app.view.common.XTextBox;
import ru.imagebook.client.common.view.order.OrderConstants;
import ru.imagebook.shared.model.Vendor;


public class OrderBonusCodeModalForm implements IsWidget {
    interface OrderBonusCodeFormUiBinder extends UiBinder<Widget, OrderBonusCodeModalForm> {
    }

    @UiField
    ModalDialog orderBonusCodeModalForm;
    @UiField
    HTMLPanel bonusCodeFieldGroup;
    @UiField
    LabelElement bonusCodeFieldLabel;
    @UiField
    XTextBox bonusCodeField;
    @UiField
    HTMLPanel deactivationCodeFieldGroup;
    @UiField
    XTextBox deactivationCodeField;

    private static OrderBonusCodeFormUiBinder uiBinder = GWT.create(OrderBonusCodeFormUiBinder.class);

    private final OrderConstants orderConstants = GWT.create(OrderConstants.class);
    private final HandlerManager handlerManager = new HandlerManager(this);

    private int orderId;
    private boolean isNoBonusCheck;

    @Override
    public Widget asWidget() {
        Widget ui = uiBinder.createAndBindUi(this);
        orderBonusCodeModalForm.setOkButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean codeIsEmpty = bonusCodeField.isEmpty();
                boolean deactivationCodeIsEmpty = deactivationCodeField.isEmpty();

                if (isNoBonusCheck) {
                    bonusCodeFieldGroup.setStyleName("has-error", codeIsEmpty);
                    deactivationCodeFieldGroup.setStyleName("has-error", deactivationCodeIsEmpty);

                    if (!codeIsEmpty && !deactivationCodeIsEmpty) {
                        setOkButtonEnabled(false);
                        handlerManager.fireEvent(new PrepareToApplyCodeEvent(orderId, bonusCodeField.getValue(),
                            deactivationCodeField.getValue()));
                    }
                } else {
                    if (!codeIsEmpty) {
                        setOkButtonEnabled(false);
                        handlerManager.fireEvent(new PrepareToApplyCodeEvent(orderId, bonusCodeField.getValue()));
                    }
                }
            }
        });
        orderBonusCodeModalForm.setCancelButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        return ui;
    }

    public void setOkButtonEnabled(boolean enabled) {
        orderBonusCodeModalForm.setOkButtonEnabled(enabled);
    }

    public void show(final int orderId, final Vendor vendor) {
        this.orderId = orderId;
        this.isNoBonusCheck = vendor.isNoBonusCheck();

        if (isNoBonusCheck) {
            bonusCodeFieldLabel.setInnerText(orderConstants.numberField());
            bonusCodeFieldGroup.addStyleName("required");
            deactivationCodeFieldGroup.setVisible(true);
        } else {
            bonusCodeFieldLabel.setInnerText(orderConstants.codeField());
            bonusCodeFieldGroup.removeStyleName("required");
            deactivationCodeFieldGroup.setVisible(false);
        }

        // clear fields values
        bonusCodeField.setValue(null);
        deactivationCodeField.setValue(null);

        orderBonusCodeModalForm.setOkButtonEnabled(true);
        orderBonusCodeModalForm.show();
    }

    public void hide() {
        orderBonusCodeModalForm.hide();
    }

    public HandlerRegistration addPrepareToApplyCodeEventHandler(PrepareToApplyCodeEventHandler handler) {
        return handlerManager.addHandler(PrepareToApplyCodeEvent.TYPE, handler);
    }
}
