package ru.minogin.core.client.flow;

public interface Widgets {
	public <T> T get(String id);
	
	public void add(String id, Object widget);

	void remove(String id);
}
