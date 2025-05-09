package ru.imagebook.client.app.ctl.payment;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ru.imagebook.client.app.service.BillRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.payment.BillsView;
import ru.imagebook.shared.model.Bill;
import ru.minogin.core.client.bean.BaseBean;


public class BillsActivity extends AbstractActivity implements BillsPresenter {
    private final BillsView view;
    private final BillRemoteServiceAsync billService;
    private final PlaceController placeController;

    @Inject
    public BillsActivity(BillsView view, BillRemoteServiceAsync billService, PlaceController placeController) {
        this.view = view;
        view.setPresenter(this);
        this.billService = billService;
        this.placeController = placeController;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view);
        loadBills();
    }

    private void loadBills() {
        billService.loadBills(new AsyncCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean billsBean) {
                List<Bill> bills = billsBean.get("bills");
                if (bills.isEmpty()) {
                    view.showNoBills();
                } else {
                    view.showBills(bills);
                }
            }
        });
    }

    @Override
    public void onPayBillButtonClicked(Bill bill) {
        placeController.goTo(new DeliveryPlace(bill));
    }

    @Override
    public void onDeleteBillButtonClicked(Bill bill) {
        view.showDeleteBillConfirmation(bill);
    }

    @Override
    public void onDeleteBillConfirmButtonClicked(Bill bill) {
        billService.deleteBill(bill.getId(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadBills();
            }
        });
    }
}
