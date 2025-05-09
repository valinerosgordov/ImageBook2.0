package ru.imagebook.server.servlet.orderImport;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class ImportErrorCodes {
	public static final int ERR001 = 1;
	public static final int ERR002 = 2;
	public static final int ERR003 = 3;
	public static final int ERR004 = 4;	
	public static final int ERR005 = 5;
	public static final int ERR006 = 6;
	public static final int ERR007 = 7;
	public static final int ERR008 = 8;
	public static final int ERR009 = 9;	
	public static final int ERR010 = 11;
	public static final int ERR011 = 11;
	public static final int ERR012 = 12;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(ERR001, I18n.ms("Не задан один из параметров vendor или password", null));
		values.put(ERR002, I18n.ms("Неверный логин или пароль вендора (ошибка авторизации)", null));
		values.put(ERR003, I18n.ms("Некорректный XML (ошибка парсинга)", null));
		values.put(ERR004, I18n.ms("Не указан один из тегов XML, errorData содержит название тега", null));
		values.put(ERR005, I18n.ms("Некорректное или пустое значение одного из параметров XML, errorData содержит название параметра", null));
		values.put(ERR006, I18n.ms("Не удалось создать пользователя", null));
		values.put(ERR007, I18n.ms("Не найден продукт", null));
		values.put(ERR008, I18n.ms("Не удалось соединиться с FTP-сервером для загрузки макетов", null));
		values.put(ERR009, I18n.ms("Некорректный набор макетов (например, не хватает какого-то файла)", null));
		values.put(ERR010, I18n.ms("Не удалось загрузить макеты", null));
		values.put(ERR011, I18n.ms("Не удалось создать заказ", null));
		values.put(ERR012, I18n.ms("Неизвестная ошибка", null));
	}
}
