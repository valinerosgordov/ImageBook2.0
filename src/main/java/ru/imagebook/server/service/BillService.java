package ru.imagebook.server.service;

import java.util.List;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.BillFilter;

public interface BillService {
	Bill getBill(int billId);

	List<Bill> loadBills(int offset, int limit, BillFilter billFilter);

	long countBills(BillFilter billFilter);

	void markPaid(List<Integer> ids);

	void markPaid(int billId);

	void markPaid(int billId, boolean notify);

    void updateBill(Bill bill);

	void deleteBills(List<Integer> ids);

    Integer computeMailruDiscountPc(Integer billId);

    void saveBill(Bill bill);
}
