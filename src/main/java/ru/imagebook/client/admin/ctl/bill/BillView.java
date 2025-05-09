package ru.imagebook.client.admin.ctl.bill;

import java.util.List;

import ru.imagebook.shared.model.Bill;

public interface BillView {
	void showSection();

	void showBills(List<Bill> bills, int offset, int total, String locale);

	void reload();

	void showBill(Bill bill);

	void hideEditForm();

	void showFilter();
}
