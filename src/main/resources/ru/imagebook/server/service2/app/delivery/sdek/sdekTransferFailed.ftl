Не удалось создать отправление в СДЭК.

Счет: ${billId?c}
Тип доставки: ${deliveryType}
<#if pickupPointId??>Идентификатор точки самовывоза: ${pickupPointId}</#if>
<#if pickupPointAddress??>Адрес точки самовывоза: ${pickupPointAddress}</#if>

<#if errorMsg??>Текст ошибки: ${errorMsg}</#if>