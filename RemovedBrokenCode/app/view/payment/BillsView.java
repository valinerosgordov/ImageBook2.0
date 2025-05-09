package ru.imagebook.client.app.view.payment;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.app.ctl.payment.BillsPresenter;
import ru.imagebook.shared.model.Bill;


public interface BillsView extends IsWidget {
    void setPresenter(BillsPresenter presenter);

    void showNoBills();

    void showBills(List<Bill> bills);

    void showDeleteBillConfirmation(Bill bill);
}
