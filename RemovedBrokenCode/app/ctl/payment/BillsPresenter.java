package ru.imagebook.client.app.ctl.payment;

import ru.imagebook.shared.model.Bill;


public interface BillsPresenter {
    void onPayBillButtonClicked(Bill bill);

    void onDeleteBillButtonClicked(Bill bill);

    void onDeleteBillConfirmButtonClicked(Bill bill);
}
