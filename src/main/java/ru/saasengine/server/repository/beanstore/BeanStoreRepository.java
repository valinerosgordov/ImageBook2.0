package ru.saasengine.server.repository.beanstore;

public interface BeanStoreRepository {
	String getJSON(String id);

	void saveJSON(String id, String json);
}
