package ru.imagebook.server.service2.app.delivery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.server.repository.BillRepository;
import ru.imagebook.server.service.BillService;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DsSendState;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Boykov
 */
public class PickPointTransferSendingServiceImpl implements PickPointTransferSendingService {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private BillRepository billRepository;
	@Autowired
	private BillService billService;
	@Autowired
	private PickPointHelper pickPointHelper;
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private MessageSource messages;

	@Transactional
	@Override
	public void transfer() {
		logger.debug("Transfer of ready bills to PickPoint is starting...");

		List<Bill> readyBills = billRepository.loadReadyToTransferToPickPoint();

		if (readyBills.isEmpty()) {
			logger.debug("Haven't found any ready bill for transfer, service will exit.");
			return;
		} else {
			logger.debug(String.format("Found %s bills to transfer.", readyBills.size()));
		}

		List<Bill> processedBills = pickPointHelper.createSendings(readyBills);

		int sent = 0, failed = 0;
		for (Bill bill : processedBills) {
			try {
				if (DsSendState.SENT == bill.getDsSendState()) {
					sent++;
				} else if (DsSendState.FAILURE == bill.getDsSendState()) {
					failed++;
					notifyAdminAboutBillTransferFailure(bill);
				} else {
					logger.error(String.format(
							"Unknown state [%s] for bill.id=%s", bill.getDsSendState(), bill.getId()));
				}
				billService.saveBill(bill);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		logger.info(String.format(
				"Transfer of bills to PickPoint finished. " +
						"Successfully transferred - %s bills, failed to transfer - %s, total bills - %s.",
				sent, failed, processedBills.size()));
	}

	private void notifyAdminAboutBillTransferFailure(Bill bill) {
		String subject = messages.getMessage(
				"billTransferToPickPointFailedSubject", new Object[]{bill.getId()}, new Locale(Locales.RU));

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("billId", bill.getId());
		freeMarker.set("errorMsg", bill.getDsErrorMessage());
		freeMarker.set("postamateId", bill.getPickpointPostamateID());
		freeMarker.set("postamateAddress", bill.getPickpointAddress());
		String html = freeMarker.process("pickpoint_transfer_failed.ftl", Locales.RU);

		notifyService.notifyAdmin(subject, html);
	}
}
