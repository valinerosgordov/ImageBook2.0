package ru.imagebook.server.repository;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;

import java.util.Date;
import java.util.List;

public interface CleanRepository {
	List<Order<?>> loadStorageOrders();

    List<Bill> loadNotPaidBillsCreatedLessThan(Date date);
}
