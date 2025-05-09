package ru.imagebook.server.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;

public class RoboServiceImpl implements RoboService {
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private MessageSource messages;

	@Override
	public void notifyBillPaid(Vendor vendor, Bill bill) {
		String subject = messages.getMessage("roboBillPaid",
				new Object[] { bill.getId() }, new Locale(Locales.RU));
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("bill", bill);
		String html = freeMarker.process("roboBillPaid.ftl", Locales.RU);
		notifyService.notifyVendorAdmin(vendor, subject, html);
	}
}
