package ru.minogin.core.server.test;

public class Value<T> {
	private T t;

	public Value(T t) {
		this.t = t;
	}

	public T get() {
		return t;
	}

	public void set(T t) {
		this.t = t;
	}
}
