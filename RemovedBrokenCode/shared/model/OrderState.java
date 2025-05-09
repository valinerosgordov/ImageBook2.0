package ru.imagebook.shared.model;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class OrderState {
	public static final int NEW = 100;
	public static final int FLASH_GENERATION = 200;
	public static final int FLASH_REGENERATION = 250;
	public static final int FLASH_REGENERATION_ERROR = 251;
	public static final int FLASH_GENERATED = 300;
	public static final int REJECTED = 400;
	public static final int BASKET = 500;
	public static final int BILL = 600;
	public static final int PAID = 700;
	public static final int PDF_GENERATION = 710;
	public static final int PDF_ERROR = 720;
	public static final int PDF_REGENERATION = 721;
	public static final int READY_TO_TRANSFER_PDF = 730;
	public static final int PDF_TRANSFER_IN_PROGRESS = 740;
	public static final int PRINTING = 750;
	public static final int FINISHING = 800;
	public static final int PRINTED = 850;
	public static final int DELIVERY = 900;
	public static final int SENT = 1000;
	public static final int DELETED = 1100;
	public static final int JPEG_GENERATION = 1200;
    public static final int JPEG_ONLINE_GENERATION = 1250;
    public static final int JPEG_BOOK_GENERATION = 1260;
	public static final int JPEG_GENERATION_ERROR = 1300;
	public static final int OLD_VERSION = 1400;

	public static final List<Integer> FLASH_JPEG_GENERATION_STATES =
        Arrays.asList(FLASH_GENERATION, JPEG_GENERATION, JPEG_ONLINE_GENERATION, JPEG_BOOK_GENERATION);

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(NEW, I18n.ms("Новый", null));
		values.put(FLASH_GENERATION, I18n.ms("Формирование макета", null));
		values.put(FLASH_REGENERATION, I18n.ms("Переформирование макета", null));
		values.put(FLASH_REGENERATION_ERROR, I18n.ms("Ошибка при переформировании макета", null));
		values.put(FLASH_GENERATED, I18n.ms("Макет сформирован", null));
		values.put(REJECTED, I18n.ms("Отклонен", null));
		values.put(BASKET, I18n.ms("В корзине", null));
		values.put(BILL, I18n.ms("Выставлен счет", null));
		values.put(PAID, I18n.ms("Оплачен", null));
		values.put(PDF_GENERATION, I18n.ms("Формирование PDF", null));
		values.put(PDF_ERROR, I18n.ms("Не удалось сформировать PDF", null));
		values.put(PDF_REGENERATION, I18n.ms("Переформирование PDF", null));
		values.put(READY_TO_TRANSFER_PDF, I18n.ms("Готов к отправке в типографию", null));
		values.put(PDF_TRANSFER_IN_PROGRESS, I18n.ms("Отправка в типографию", null));
		values.put(PRINTING, I18n.ms("Печать", null));
		values.put(FINISHING, I18n.ms("Отделка", null));
		values.put(PRINTED, I18n.ms("Отпечатан", null));
		values.put(DELIVERY, I18n.ms("На отправке", null));
		values.put(SENT, I18n.ms("Отправлен", null));
		values.put(DELETED, I18n.ms("Удален", null));
		values.put(JPEG_GENERATION, I18n.ms("Формирование макета (онлайн-сборщик)", null));
        values.put(JPEG_ONLINE_GENERATION, I18n.ms("Формирование макета (Пикбук)", null));
        values.put(JPEG_BOOK_GENERATION, I18n.ms("Формирование макета (онлайн-редактор)", null));
		values.put(JPEG_GENERATION_ERROR, I18n.ms("Ошибка при формировании JPEG", null));
		values.put(OLD_VERSION, I18n.ms("Старая версия", null));
	}
}
