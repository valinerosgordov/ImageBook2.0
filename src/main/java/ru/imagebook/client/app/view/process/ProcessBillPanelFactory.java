package ru.imagebook.client.app.view.process;

import ru.imagebook.shared.model.Bill;


public interface ProcessBillPanelFactory {
    BillPanel createBillPanel(Bill bill);
}
