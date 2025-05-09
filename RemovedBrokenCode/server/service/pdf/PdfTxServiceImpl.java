package ru.imagebook.server.service.pdf;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.PdfRepository;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.server.freemarker.FreeMarker;

public class PdfTxServiceImpl implements PdfTxService {
	@Autowired
	private PdfRepository repository;

	@Autowired
	private MessageSource messages;

	@Autowired
	private NotifyService notifyService;

	@Transactional
	@Override
	public List<Order<?>> loadPaidOrders() {
		return repository.loadPaidOrders();
	}

	@Transactional
	@Override
	public List<Page> getPagesFromLayout(final int layoutId) {
		return repository.getPagesFromLayout(layoutId);
	}

	@Transactional
	@Override
	public void setPdfGenerated(Integer orderId) {
		Order<?> order = repository.getOrder(orderId);
		order.setState(OrderState.READY_TO_TRANSFER_PDF);
	}

	@Transactional
	@Override
	public void setPdfErrorState(Integer orderId) {
		Order<?> order = repository.getOrder(orderId);
		order.setState(OrderState.PDF_ERROR);

		notifyAdminOrderPdfFailed(order);
	}

	@Transactional
	@Override
	public void updateOrdersState(Collection<Integer> orderIds, int orderState) {
		List<Order<?>> orders = repository.loadOrders(orderIds);
		for (Order<?> order : orders) {
			order.setState(orderState);
		}
	}

	private void notifyAdminOrderPdfFailed(Order<?> order) {
		User user = order.getUser();
		Locale locale = new Locale(user.getLocale());
		String subject = messages.getMessage("orderPdfFailedSubject", new Object[] { order.getNumber() }, locale);

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("orderNumber", order.getNumber());
		freeMarker.set("orderUsername", user.getUserName());
		String html = freeMarker.process("orderPdfFailed.ftl", user.getLocale());

		notifyService.notifyAdmin(subject, html);
	}
}