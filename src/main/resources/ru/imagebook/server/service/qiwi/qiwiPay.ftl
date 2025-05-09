<!doctype html>

<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  	<link rel="stylesheet" type="text/css" href="/static/css/robo.css" />
    <title>Imagebook - оплата счета через платежный сервис QIWI</title>
  </head>
  <body>
  	Сейчас вы будете перенаправлены на сервис QIWI для оплаты счета.<br/>
  	Если перенаправление не произошло автоматически, нажмите кнопку "Оплатить счет".<br/><br/>
  	<form action="http://www.mobw.ru/setInetBill_utf.do" method="POST">
    	<input type="hidden" name="from" value="${from}" />
   	  <input type="hidden" name="to" value="${to}" />
			<input type="hidden" name="summ" value="${summ?c}" />
			<input type="hidden" name="com" value="Оплата заказа на печатную продукцию Imagebook" />
			<input type="hidden" name="check_agt" value="false" />
			<input type="hidden" name="txn_id" value="${txn_id?c}" />
    	<input type="submit" value="Оплатить счет" />
		</form>

  	<script language="javascript">
  		document.forms[0].submit();
  	</script>
  </body>
</html>