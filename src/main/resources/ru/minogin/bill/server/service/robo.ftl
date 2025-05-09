<form method="POST" action="${url}">
	<input type="hidden" name="MrchLogin" value="${info.username}" />
	<input type="hidden" name="OutSum" value="${info.sum?c}" />
	<input type="hidden" name="InvId" value="${info.orderId?c}" />
	<input type="hidden" name="Desc" value="${info.desc}" />
	<input type="hidden" name="SignatureValue" value="${info.crc}" />
	<input type="hidden" name="IncCurrLabel" value="" />
	<input type="hidden" name="Culture" value="ru" />
	<input type="submit" value="Оплатить" />
</form>