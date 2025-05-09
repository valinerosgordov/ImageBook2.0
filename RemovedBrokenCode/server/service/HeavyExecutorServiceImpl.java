package ru.imagebook.server.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class HeavyExecutorServiceImpl implements HeavyExecutorService {
	public static final Logger LOGGER = Logger.getLogger(HeavyExecutorServiceImpl.class);

	private static final int N_CORES = 24;
	private static final int FREE_CORES = 6;

	private final ExecutorService executor;

	public HeavyExecutorServiceImpl() {
		int nThreads = N_CORES - FREE_CORES; 
		executor = Executors.newFixedThreadPool(nThreads);
		LOGGER.warn("HeavyExecutorServiceImpl started with " + nThreads + " threads pool.");
	}

	@Override
	public void submit(Runnable runnable) {
		executor.submit(runnable);
	}
}
