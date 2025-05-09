package ru.imagebook.client.app.view.sent;

import ru.imagebook.shared.model.Bill;


public interface SentBillPanelFactory {
    BillPanel createBillPanel(Bill bill);
}
