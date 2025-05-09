<form method="POST" action="${url}">
	<input type="hidden" name="shop_id" value="${shop_id}" />
	<input type="hidden" name="currency" value="${currency}" />
	<input type="hidden" name="sum" value="${sum?c}" />
	<input type="hidden" name="issuer_id" value="${issuer_id?c}" />
	<input type="hidden" name="description" value="${description}" />
	<input type="hidden" name="message" value="${message}" />
	<input type="hidden" name="signature" value="${signature}" />
	<input type="hidden" name="encoding" value="${encoding}" />
	<input type="submit" value="Оплатить" />
</form>