package ru.minogin.core.client.bean;

import java.util.Collection;
import java.util.Map;

import ru.minogin.core.client.rpc.Transportable;

public interface Bean extends Transportable {
	<X> X get(String name);

	<X> X set(String name, X value);
	
	<X> X setTransient(String name, X value);
	
	Collection<String> getPropertyNames();
	
	Map<String, Object> getProperties();
	
	<X> X remove(String name);

	boolean has(String name);
}
