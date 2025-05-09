package ru.minogin.bill.shared.mailru;

/**
 * Cтатус ошибки обработки
 *
 * S0001 - Техническая ошибка на стороне Магазина (например, недоступна база данных).
 *         Повторять уведомления.
 * S0002 - Некорректный формат уведомления (например, не получен item_number). Остановить
 *         уведомления
 * S0003 - Ошибка проверки цифровой подписи. Остановить уведомления.
 * S0004 - Уведомление с указанным item_number уже обработано. Остановить уведомления
 * S0005 - Платеж не может быть зачислен, нужно вернуть деньги плательщику (если Магазину
 *         разрешена такая возможность) и остановить уведомления.
 *
 * @see <a href="https://money.mail.ru/img/partners/dmr_light_v1.2.pdf">MailRu Money 'Light' scheme</a>
 */
public enum MailruMoneyErrorCode {
    SYSTEM("S0001"),
    WRONG_FORMAT("S0002"),
    INVALID_SIGNATURE("S0003"),
    ALREADY_PROCESSED("S0004");

    private String code;

    private MailruMoneyErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
