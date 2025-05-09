package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class OrderType {
	public static final int MANUAL = 100;
	public static final int MPHOTO = 200;
	public static final int EDITOR = 300;
	public static final int EXTERNAL = 400;	
	public static final int BOOK = 500;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(MANUAL, I18n.ms("Создан вручную", null));
		values.put(MPHOTO, I18n.ms("M-Photo", null));
		values.put(EDITOR, I18n.ms("Imagebook Editor", null));
		values.put(EXTERNAL, I18n.ms("Внешний заказ", null));		
		values.put(BOOK, I18n.ms("Онлайн-редактор", null));
	}
}
