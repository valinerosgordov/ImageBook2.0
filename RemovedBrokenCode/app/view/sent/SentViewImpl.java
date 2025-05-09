package ru.imagebook.client.app.view.sent;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;


@Singleton
public class SentViewImpl implements SentView {
    interface SentUiBinder extends UiBinder<Widget, SentViewImpl> {
    }
    private static SentUiBinder uiBinder = GWT.create(SentUiBinder.class);

    @UiField
    InlineLabel infoLabel;
    @UiField
    HTMLPanel billsPanel;

	private final SentConstants constants;
    private final SentBillPanelFactory sentBillPanelFactory;

	@Inject
	public SentViewImpl(SentConstants constants, SentBillPanelFactory sentBillPanelFactory) {
		this.constants = constants;
        this.sentBillPanelFactory = sentBillPanelFactory;
	}

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @Override
    public void showNoOrders() {
        infoLabel.setText(constants.emptyOrders());
        infoLabel.setVisible(true);
    }

	@Override
	public void showSentOrders(List<Order<?>> orders) {
        billsPanel.setVisible(true);

        // FIXME duplicated code, refactor
        // group orders by bills
        Map<Bill, Set<Order<?>>> groups = new LinkedHashMap<Bill, Set<Order<?>>>(orders.size());
        for (Order<?> order : orders) {
            Bill bill = order.getBill();
            Set<Order<?>> billOrders = groups.get(bill);
            if (billOrders == null) {
                billOrders = new HashSet<Order<?>>();
                groups.put(bill, billOrders);
            } 
            billOrders.add(order);
        }


        for (Bill bill : groups.keySet()) {
            if (bill == null) {
                continue;
            }

            bill.setOrders(groups.get(bill));
            BillPanel billPanel = sentBillPanelFactory.createBillPanel(bill);
            billsPanel.add(billPanel);
        }
	}
}
