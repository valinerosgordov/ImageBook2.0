package ru.minogin.core.client.format;

public interface PhoneFormat {
	public static final String PATTERN = "\\+?(7\\d{10}|[0-68-9]\\d+)";
	public static final String COUNTRY_PHONE = "\\d{10}";
}
