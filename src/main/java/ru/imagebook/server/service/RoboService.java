package ru.imagebook.server.service;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Vendor;

public interface RoboService {
	void notifyBillPaid(Vendor vendor, Bill bill);
}
