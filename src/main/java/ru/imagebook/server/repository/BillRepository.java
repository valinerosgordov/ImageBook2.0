package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.BillFilter;
import ru.imagebook.shared.model.User;

public interface BillRepository {
	Bill getBill(int billId);

	List<Bill> loadBills(int offset, int limit, BillFilter billFilter);

	long countBills(BillFilter billFilter);

	List<Bill> loadBills(List<Integer> ids);

	List<Bill> loadPaidBills(User user);

    void saveBill(Bill bill);

	List<Bill> loadReadyToTransferToPickPoint();

	List<Bill> loadReadyToTransferToSDEK();
}
