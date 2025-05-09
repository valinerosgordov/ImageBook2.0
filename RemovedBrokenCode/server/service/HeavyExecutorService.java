package ru.imagebook.server.service;

public interface HeavyExecutorService {
	void submit(Runnable runnable);
}
