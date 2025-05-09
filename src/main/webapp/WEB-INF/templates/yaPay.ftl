<!doctype html>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="/static/css/robo.css" />
	<title>${vendor.name} - оплата счета через Яндекс.Касса</title>
</head>
<body>
  	Сейчас вы будете перенаправлены на сервис Яндекс.Касса для оплаты счета.<br/>
  	Если перенаправление не произошло автоматически, нажмите кнопку "Оплатить счет".<br/><br/>
  	<form action="https://money.yandex.ru/eshop.xml" method="POST">
		<input type="hidden" name="shopId" value="${vendor.yandexShopId?c}" />
		<input type="hidden" name="scid" value="${vendor.yandexScid?c}" />
		<input type="hidden" name="sum" value="${sum?c}" />
		<input type="hidden" name="customerNumber" value="${customerNumber}" />
		<input type="hidden" name="paymentType" value="" />
		<input type="hidden" name="orderNumber" value="${orderNumber?c}" />
		<input type="hidden" name="cps_email" value="${cps_email!}" />
		<input type="hidden" name="cps_phone" value="${cps_phone!}" />
        <input type="hidden" name="ym_merchant_receipt"
			   value='{
			     "customerContact": "${customerContact}",
				 "taxSystem": 2,
				 "items":[{
					"quantity": 1,
					"price": {
						"amount": ${sum?c}
					},
					"tax": 1,
					"text": "Оплата заказа на печатную продукцию ${vendor.name}"
				 }]
			   }'/>
		<input type="submit" value="Оплатить счет" />
  	</form>

  	<script language="javascript">
  		document.forms[0].submit();
  	</script>
</body>
</html>