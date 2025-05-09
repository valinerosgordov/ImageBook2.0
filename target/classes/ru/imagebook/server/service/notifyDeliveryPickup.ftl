<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
    <meta name="viewport" content="width=device-width" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>ImagebookEmails</title>
</head>
<body style="margin: 0; padding: 0;">
<table cellpadding="0" cellspacing="0" width="100%" align="center" style="border-collapse: collapse; background-color: #F8F8F8;">
    <tr>
        <td align="center" style="padding: 40px 30px;">
            <table border="0" cellpadding="0" cellspacing="0" width="700" style="border: 1px solid #E7E7E7; background-color: #FFFFFF;">
                <tr>
                    <td style="padding: 20px 20px 0 20px;">
                        <h2 style="color: #222222; font-family: Helvetica, Arial, sans-serif; font-size: 24px; font-weight: normal; line-height: 1.4; margin-top: 0;">

                            Здравствуйте<#if name?has_content>, ${name}</#if>!

                            <p style="color: #444444; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4; margin-bottom: 10px;">
                                Ваш заказ №${bill.id} готов и ожидает Вас в пункте самовывоза по адресу ул. Академика Королева, д.13, подъезд 1, офис 101.
                            </p>
                            <p style="color: #444444; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4; margin-bottom: 10px;">
                                Время работы: по рабочим дням с 10:30 до 18:00.
                                В бюро пропусков нужно попросить пропуск в компанию "Имиджбук", при себе иметь документ удостоверяющий личность.
                            </p>
                        </h2>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 0 20px 0 20px; color: #444444; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4;">

                        <table border="1" cellpadding="5" cellspacing="0" width="100%" align="center"
                               style="border: solid #e7e7e7 1px; border-collapse: collapse; color: #444444; font-size: small; font-family: Helvetica, Arial, sans-serif; line-height: 1.4; margin-top: 10px; margin-bottom: 10px;">
                            <tr align="center" valign="top" style="background-color: #eeeeee;">
                                <th>Номер заказа</th>
                                <th>Заказ</th>
                                <th nowrap>Кол-во<br>стр.</th>
                                <th>Цена, руб.</th>
                                <th nowrap>Кол-во</th>
                                <th>Скидка, руб.</th>
                                <th>Итого со скидкой, руб.</th>
                            </tr>
                        <#assign
                        totalQuantity = 0
                        totalCost = 0
                        >
                        <#list bill.orders as order>
                            <#assign
                            product = order.product
                            totalQuantity = totalQuantity + order.quantity
                            totalCost = totalCost + order.cost
                            >
                            <tr align="right" valign="top">
                                <td align="center">${order.number}</td>
                                <td align="left">${product.name.ru}</td>
                                <td>${order.pageCount}</td>
                                <td>${order.price}</td>
                                <td>${order.quantity}</td>
                                <td>${order.discount}</td>
                                <td>${order.cost}</td>
                            </tr>
                        </#list>
                            <tr align="right" valign="top" style="font-weight: bold">
                                <td></td>
                                <td align="left">Итого</td>
                                <td></td>
                                <td></td>
                                <td>${totalQuantity}</td>
                                <td></td>
                                <td>${totalCost}</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px 20px 20px 20px;">
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px 20px 20px 20px;">
                        <p style="color: #222222; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4; margin-bottom: 10px;">
                            С уважением,<br>Администрация ${data.name}
                        </p>
                    </td>
                </tr>
            </table>
            <table  border="0" cellpadding="0" cellspacing="0" width="700" style="border-collapse: collapse;">
                <tr>
                    <td align="left" valign="top" width="45%" style="padding: 20px;">
                        <p style="color: #222222; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4; margin-top: 0; margin-bottom: 0;">
                        ${data.companyName}<br>
                            <a href="http://${data.site}">www.${data.site}</a>
                        </p>
                    </td>
                    <td align="right" valign="top" width="45%" style="padding: 20px;">
                        <p style="color: #222222; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4; margin-top: 0; margin-bottom: 10px;">
                        <#if data.phone??>Тел.: ${data.phone}<br></#if>
                            E-mail: <a href="mailto:${data.email}">${data.email}</a>
                        </p>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>