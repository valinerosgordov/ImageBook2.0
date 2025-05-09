package ru.imagebook.server.service.load;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ru.minogin.core.server.ServiceLogger;

public class LoadServiceImpl implements LoadService {
	public static final int 	DELAY_SEC = 60;

	private LoadTxService service;
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private ExecutorService executor = Executors.newCachedThreadPool();

	public LoadServiceImpl(LoadTxService service) {
		this.service = service;
	}

	@Override
	public void startAsync() {
		service.init();

		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				load();
			}
		}, 0, DELAY_SEC, TimeUnit.SECONDS);
	}

	private void load() {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					service.load();
				}
				catch (Throwable t) {
					ServiceLogger.log(t);
				}
			}
		});
	}
}
