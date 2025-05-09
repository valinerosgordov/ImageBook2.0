package ru.imagebook.server.service2.pdf;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class PdfTransferServiceImpl implements PdfTransferService {
	private static Logger logger = Logger.getLogger(PdfTransferService.class);

	@Autowired
	private PdfTransferServiceConfig config;
	@Autowired
	private PdfTransferSubmitter submitter;

	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	@Override
	public void start() {
		logger.debug("PDF transfer service starting");

		submitter.resetOrdersInProgress();

		logger.debug("Starting scheduler");
		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					submitter.submitNewOrders();
				}
				catch (Exception e) {
					logger.error("Submitting new orders failed", e);
				}
			}
		}, 0, config.getDelayMs(), TimeUnit.MILLISECONDS);

		logger.debug("PDF transfer service started");
	}
}
