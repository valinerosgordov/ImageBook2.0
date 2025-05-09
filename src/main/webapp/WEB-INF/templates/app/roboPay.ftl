<!doctype html>

<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  	<link rel="stylesheet" type="text/css" href="/static/css/robo.css" />
    <title>${vendor.name} - оплата счета через ROBOKASSA</title>
  </head>
  <body>
  	Сейчас вы будете перенаправлены на сервис ROBOKASSA для оплаты счета.<br/>
  	Если перенаправление не произошло автоматически, нажмите кнопку "Оплатить счет".<br/><br/>
  	<form action="https://merchant.roboxchange.com/Index.aspx" method="POST">
  		<input type="hidden" name="MrchLogin" value="${login}" />
  		<input type="hidden" name="OutSum" value="${sum?c}" />
  		<input type="hidden" name="InvId" value="${billId?c}" />
  		<input type="hidden" name="Desc" value="Оплата заказа на печатную продукцию ${vendor.name}" />
  		<input type="hidden" name="SignatureValue" value="${crc}" />
  		<input type="hidden" name="IncCurrLabel" value="PCR" />
  		<input type="hidden" name="Culture" value="ru" />
  		<input type="submit" value="Оплатить счет" />
  	</form>

  	<script language="javascript">
  		document.forms[0].submit();
  	</script>
  </body>
</html>