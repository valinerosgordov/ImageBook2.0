package ru.imagebook.client.app.view.payment;

import ru.imagebook.shared.model.Bill;


public interface BillPanelFactory {
    BillPanel createBillPanel(Bill bill);
}
