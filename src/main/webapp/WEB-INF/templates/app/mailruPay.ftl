<!doctype html>

<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  	<link rel="stylesheet" type="text/css" href="/static/css/robo.css" />
    <title>${vendor.name} - оплата счета через Деньги@Mail.Ru</title>
  </head>
  <body>
  	Сейчас вы будете перенаправлены на сервис Деньги@Mail.RU для оплаты счета.<br/>
  	Если перенаправление не произошло автоматически, нажмите кнопку "Оплатить".<br/><br/>
  	<#include "/mailruPayForm.ftl" />
  	
  	<script language="javascript">
  		document.forms[0].submit();
  	</script>
  </body>
</html>