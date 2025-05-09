<p>
${header}
</p>
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
<#assign totalQuantity = 0 totalCost = 0>
<#list bill.orders as order>
    <#assign product = order.product>
    <#assign totalCost = totalCost + order.cost>
    <#assign totalQuantity = totalQuantity + order.quantity>
    <tr align="right" valign="top">
        <td align="center">${order.number}</td>
        <td align="left">${product.name.ru}
            <#if product.isFlyleafs()>
                <#assign flyleaf = order.flyleaf>
                <br>
                <div style="vertical-align: middle; margin-top: 5px;">
                    Цветной форзац:
                    <div style="background-color: #${flyleaf.colorRGB}; border: 1px solid black; width: 20px; height: 20px; display: inline-block; vertical-align: middle;"></div>
                    ${flyleaf.name} <#if flyleaf.id != defaultFlyleafId>- ${order.flyleafPrice} руб.</#if>
                </div>
            </#if>
            <#if product.isSupportsVellum()>
                <div style="vertical-align: middle; margin-top: 5px;">
                    Калька:
                    <#if order.vellum??>
                        <#assign vellum = order.vellum>
                        <div style="background-color: #${vellum.colorRGB}; border: 1px solid black; width: 20px; height: 20px; display: inline-block; vertical-align: middle;"></div>
                        ${vellum.name} - ${order.vellumPrice} руб.
                    <#else>
                        Без кальки
                    </#if>
                </div>
            </#if>
        </td>
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