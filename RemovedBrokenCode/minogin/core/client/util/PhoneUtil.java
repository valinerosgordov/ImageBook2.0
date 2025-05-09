package ru.minogin.core.client.util;

public class PhoneUtil {
	public static String skype(String phone) {
		phone = phone.trim();
		phone = phone.replaceAll("[\\s\\+\\-()]", "");
		if (!phone.startsWith("+"))
			phone = "+" + phone;
		return phone;
	}
}
