package ru.imagebook.server.service2.pdf;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository2.PdfTransferRepository;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;

public class PdfTransferSubmitterImpl implements PdfTransferSubmitter {
	private static Logger logger = Logger.getLogger(PdfTransferService.class);

	@Autowired
	private PdfTransferRepository repository;

	@Autowired
	private PdfTransferEngine engine;

	private final ExecutorService transferExecutor;

	@Autowired
	public PdfTransferSubmitterImpl(PdfTransferServiceConfig config) {
		transferExecutor = Executors.newFixedThreadPool(config.getThreads());
	}

	@Transactional
	@Override
	public void resetOrdersInProgress() {
		logger.debug("Resetting orders in progress");
		List<Order<?>> orders = repository.loadOrdersInProgress();
		for (Order<?> order : orders) {
			order.setState(OrderState.READY_TO_TRANSFER_PDF);
		}
	}

	@Transactional
	@Override
	public void submitNewOrders() {
		logger.debug("Submitting new orders");

		List<Order<?>> ordersToTransfer = repository.loadOrdersToTransfer();
		logger.debug("Found " + ordersToTransfer.size() + " orders to transfer");

		for (final Order<?> order : ordersToTransfer) {
			logger.debug("Submitting order: " + order.getNumber());

			order.setState(OrderState.PDF_TRANSFER_IN_PROGRESS);
			try {
				transferExecutor.submit(new Runnable() {
					@Override
					public void run() {
						try {
							engine.transferOrder(order.getId());
						} catch (Exception e) {
							logger.error("Order transfer failed: " + order.getNumber(), e);
							engine.resetOrderState(order.getId());
						}
					}
				});
			} catch (Exception e) {
				logger.error("Submit failed: " + order.getNumber());
				order.setState(OrderState.READY_TO_TRANSFER_PDF);
			}
		}
	}
}
