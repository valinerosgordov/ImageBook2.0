package ru.minogin.ui.client.field;

public interface Validator<T> {
	/**
	 * @param value - value for validation
	 * @return <code>true</code> if value is valid, <code>false</code> otherwise.
	 */
	boolean validate(T value);
}
