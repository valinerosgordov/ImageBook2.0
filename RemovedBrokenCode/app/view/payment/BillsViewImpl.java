package ru.imagebook.client.app.view.payment;

import java.util.List;

import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.AlertCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.ctl.payment.BillsPresenter;
import ru.imagebook.client.common.view.order.OrderConstants;
import ru.imagebook.shared.model.Bill;
import ru.minogin.core.client.constants.CommonConstants;


@Singleton
public class BillsViewImpl implements BillsView {
    interface BillsUiBinder extends UiBinder<Widget, BillsViewImpl> {
    }
    private static BillsUiBinder uiBinder = GWT.create(BillsUiBinder.class);

    @UiField
    InlineLabel infoLabel;
    @UiField
    HTMLPanel billsPanel;

    private final OrderConstants orderConstants;
    private final CommonConstants commonConstants;
    private final BillPanelFactory billPanelFactory;

    private BillsPresenter presenter;

    @Inject
    public BillsViewImpl(OrderConstants orderConstants, CommonConstants commonConstants,
                         BillPanelFactory billPanelFactory) {
        this.orderConstants = orderConstants;
        this.commonConstants = commonConstants;
        this.billPanelFactory = billPanelFactory;
    }

    @Override
    public void setPresenter(BillsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @Override
    public void showNoBills() {
        infoLabel.setText(orderConstants.noBills());
        infoLabel.setVisible(true);
        billsPanel.clear();
        billsPanel.setVisible(false);
    }

    @Override
    public void showBills(List<Bill> bills) {
        billsPanel.setVisible(true);
        billsPanel.clear();

        int counter = 0;
        for (final Bill bill : bills) {
            BillPanel billPanel = billPanelFactory.createBillPanel(bill);
            billsPanel.add(billPanel);

            Button payButton = new Button(orderConstants.pay(), new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    presenter.onPayBillButtonClicked(bill);
                }
            });
            payButton.setStyleName("btn btn-primary");
            billsPanel.add(payButton);

            // add space between buttons
            billsPanel.add(new HTMLPanel("span", "&nbsp;"));

            Button deleteButton = new Button(orderConstants.delete(), new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    presenter.onDeleteBillButtonClicked(bill);
                }
            });
            deleteButton.setStyleName("btn btn-danger");
            billsPanel.add(deleteButton);

            if (counter < bills.size() - 1) {
                billsPanel.add(new HTMLPanel("hr", ""));
            } else {
                billsPanel.add(new HTMLPanel("br", ""));
                billsPanel.add(new HTMLPanel("br", ""));
            }

            counter++;
        }
    }

    @Override
    public void showDeleteBillConfirmation(final Bill bill) {
        Bootbox.Dialog.create()
            .setMessage(orderConstants.confirmBillDeletion())
            .setCloseButton(false)
            .setTitle(commonConstants.deletionConfirmation())
            .addButton(commonConstants.ok(), "btn-primary", new AlertCallback() {
                @Override
                public void callback() {
                    presenter.onDeleteBillConfirmButtonClicked(bill);
                }})
            .addButton(commonConstants.cancel(), "btn-default")
            .show();
    }
}
