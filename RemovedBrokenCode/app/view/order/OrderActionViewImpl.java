package ru.imagebook.client.app.view.order;

import java.util.Map;

import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.AlertCallback;

import com.google.common.collect.Maps;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import ru.imagebook.client.app.ctl.order.OrderAction;
import ru.imagebook.client.app.ctl.order.OrderActionPresenter;
import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.client.common.view.order.OrderConstants;
import ru.minogin.core.client.constants.CommonConstants;

/**

 * @since 18.02.2015
 */
public class OrderActionViewImpl implements OrderActionView {
    private final OrderConstants orderConstants;
    private final CommonConstants commonConstants;

    private Map<OrderAction, Button> actionButtonMap = Maps.newEnumMap(OrderAction.class);
    private OrderActionPresenter presenter;
    private FlowPanel buttonsGroupPanel;

    @Inject
    public OrderActionViewImpl(OrderConstants orderConstants, CommonConstants commonConstants) {
        this.orderConstants = orderConstants;
        this.commonConstants = commonConstants;

        buttonsGroupPanel = new FlowPanel();
        buttonsGroupPanel.setStyleName("btn-group-vertical order-actions-group");
    }

    @Override
    public void setPresenter(OrderActionPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        return buttonsGroupPanel;
    }

    @Override
    public void addOrderActionButton(final OrderAction orderAction) {
        String actionButtonLabel = getOrderActionButtonLabel(orderAction);
        final Button actionButton = new Button(actionButtonLabel);
        actionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                actionButton.setEnabled(false);
                switch (orderAction) {
                    case PROCESS:
                        presenter.onProcessOrderButtonClicked();
                        break;
                    case EDIT:
                        presenter.onEditOrderButtonClicked();
                        break;
                    case COPY:
                        presenter.onCopyOrderButtonClicked();
                        break;
                    case DELETE:
                        presenter.onDeleteOrderButtonClicked();
                        break;
                    case PUBLISH:
                        presenter.onPublishOrderButtonClicked();
                        actionButton.setEnabled(true);
                        break;
                }
            }
        });
        actionButton.setStyleName("btn btn-sm");
        actionButton.addStyleName(orderAction == OrderAction.DELETE ? "btn-danger" : "btn-default");
        buttonsGroupPanel.add(actionButton);

        actionButtonMap.put(orderAction, actionButton);
    }

    private String getOrderActionButtonLabel(OrderAction orderAction) {
        String label = null;
        switch (orderAction) {
            case PROCESS:
                label = orderConstants.processOrder();
                break;
            case EDIT:
                label = orderConstants.edit();
                break;
            case COPY:
                label = orderConstants.copy();
                break;
            case DELETE:
                label = orderConstants.delete();
                break;
            case PUBLISH:
                label = orderConstants.publish();
                break;
        }
        return label;
    }

    private Button getOrderActionButton(OrderAction orderAction) {
        return actionButtonMap.get(orderAction);
    }

    @Override
    public void alertProcessOrderFailed() {
        getOrderActionButton(OrderAction.PROCESS).setEnabled(true);
        Notify.error(orderConstants.processOrderFailed());
    }

    @Override
    public void alertEditOrderFailed() {
        getOrderActionButton(OrderAction.EDIT).setEnabled(true);
        Notify.error(orderConstants.editOrderFailed());
    }

    @Override
    public void alertCopyOrderFailed() {
        getOrderActionButton(OrderAction.COPY).setEnabled(true);
        Notify.error(orderConstants.copyOrderFailed());
    }

    @Override
    public void showDeleteOrderConfirmation() {
        Bootbox.Dialog.create()
            .setMessage(orderConstants.confirmOrderDeletion())
            .setCloseButton(false)
            .setTitle(commonConstants.deletionConfirmation())
            .addButton(commonConstants.ok(), "btn-primary", new AlertCallback() {
                @Override
                public void callback() {
                    presenter.onDeleteOrderConfirmButtonClicked();
                }})
            .addButton(commonConstants.cancel(), "btn-default", new AlertCallback() {
                @Override
                public void callback() {
                    getOrderActionButton(OrderAction.DELETE).setEnabled(true);
                }
            })
            .show();
    }

    @Override
    public void alertOrderDeleted() {
        Notify.notify(orderConstants.orderDeletedHeading(), orderConstants.orderDeletedText());
    }

    @Override
    public void alertDeleteOrderFailed() {
        getOrderActionButton(OrderAction.DELETE).setEnabled(true);
        Notify.error(orderConstants.copyOrderFailed());
    }

    @Override
    public void alertPublishOrderFailed() {
        getOrderActionButton(OrderAction.PUBLISH).setEnabled(true);
        Notify.error(orderConstants.publishOrderFailed());
    }
}
