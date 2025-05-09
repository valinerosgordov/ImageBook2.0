package ru.minogin.bill.shared.mailru;

/**
 * Статус обработки уведомления:
 * ACCEPTED — обработка прошла успешно
 * REJECTED — при обработке уведомления произошла ошибка
 *
 * @see <a href="https://money.mail.ru/img/partners/dmr_light_v1.2.pdf">MailRu Money 'Light' scheme</a>
 */
public enum MailruMoneyStatus {
    ACCEPTED, REJECTED
}
