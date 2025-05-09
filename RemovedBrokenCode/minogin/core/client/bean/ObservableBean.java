package ru.minogin.core.client.bean;

public interface ObservableBean extends Bean {
	void addPropertyChangeListener(PropertyChangeListener listener);
	
	<X> X directSet(String name, X value);
}
