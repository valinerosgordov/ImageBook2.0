package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class ProductType {
	public static final int EVERFLAT_WHITE_MARGINS = 1;
	public static final int EVERFLAT_FULL_PRINT = 2;
	public static final int PANORAMIC = 3;
	public static final int HARD_COVER_WHITE_MARGINS = 4;
	public static final int HARD_COVER_FULL_PRINT = 5;
	public static final int STANDARD = 6;
	public static final int SPRING = 7;
	public static final int CLIP = 8;
	public static final int TRIAL = 9;
	public static final int CONGRATULATORY = 10;
	public static final int TABLET = 11;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(EVERFLAT_WHITE_MARGINS, I18n.ms("Панорамный в твердой обложке с белыми полями", null));
		values.put(EVERFLAT_FULL_PRINT, I18n.ms("Панорамный в твердой обложке с полной запечаткой", null));
		values.put(PANORAMIC, I18n.ms("Панорамный (кожа/тряпка/синтетика)", null));
		values.put(HARD_COVER_WHITE_MARGINS, I18n.ms("Твердая обложка с белыми полями", null));
		values.put(HARD_COVER_FULL_PRINT, I18n.ms("Твердая обложка с полной запечаткой", null));
		values.put(STANDARD, I18n.ms("Стандартный (кожа/тряпка/синтетика)", null));
		values.put(SPRING, I18n.ms("Переплет на пружине", null));
		values.put(CLIP, I18n.ms("Переплет на скрепке", null));
		values.put(TRIAL, I18n.ms("Пробные альбомы", null));
		values.put(CONGRATULATORY, I18n.ms("Поздравительные альбомы", null));
		values.put(TABLET, I18n.ms("Планшет", null));
	}

	public static String getProductTypeName(int type, String locale) {
		MultiString typeName = values.get(type);
		return typeName == null ? null : typeName.get(locale);
	}
}
