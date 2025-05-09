package ru.imagebook.server.service.qiwi;

import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;

import ru.imagebook.server.service.OrderService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;

public class QiwiServiceImpl implements QiwiService {
	public static final String PASSWORD1 = "XVev69anrU17";
	@Autowired
	private OrderService orderService;

	@Override
	public void qiwiPay(User user, int billId, String userName, Writer writer) {
		Bill bill = orderService.getBill(billId);
		if (!bill.getUser().equals(user))
			throw new AccessDeniedError();

		orderService.setComputedValues(bill);

		FreeMarker freeMarker = new FreeMarker(getClass());

		freeMarker.set("from", "6729");
		freeMarker.set("to", userName);
		freeMarker.set("summ", bill.getTotal());
		freeMarker.set("txn_id", bill.getId());

		freeMarker.process("qiwiPay.ftl", Locales.RU, writer);
	}

	@Override
	public void qiwiFail(Writer writer) {
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.process("qiwiFail.ftl", Locales.RU, writer);
	}

	@Override
	public void qiwiSuccess(Writer writer) {
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.process("qiwiSuccess.ftl", Locales.RU, writer);
	}
}
