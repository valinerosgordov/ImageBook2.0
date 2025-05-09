package ru.minogin.core.client.util;

import ru.minogin.core.client.text.StringUtil;

public class AddressUtil {
	public static String yandexMapsHref(String country, String city, String street, String home,
			String building) {
		String buildingSearch = StringUtil.prefix("к", building);
		String homeSearch = StringUtil.implode("", home, buildingSearch);
		String search = StringUtil.implode(", ", country, city, street, homeSearch);
		return "http://maps.yandex.ru/?text=" + search;
	}
}
