package ru.imagebook.server.service;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Vendor;

public interface MailruService {
	void notifyBillPaid(Vendor vendor, Bill bill);
}
