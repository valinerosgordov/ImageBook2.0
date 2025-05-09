<!doctype html>

<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  	<link rel="stylesheet" type="text/css" href="/static/css/delivery.css" />
    <title>Imagebook - доставка</title>
  </head>
  <body>
  	<h1>Доставка ${typeName} ${date?date}</h1>
  	<table class="tbl">
  		<tr>
  			<th>Номер счета</th>
  			<th>Номер заказа</th>
  			<th>Кол-во</th>
  			<th>Адрес</th>
  			<th>Имя</th>
  			<th>Телефон</th>
  			<th>E-mail</th>
  			<th>Комментарий</th>
  		</tr>
  		<#list orders as order>
  			<tr>
  				<td><#if order.billId??>${order.billId?c}</#if></td>
  				<td>${order.number}</td>
  				<td align="right">${order.quantity!"-"}</td>
  				<#if order.addressText??>
  					<td>${order.addressText}</td>
  					<td>${order.name}</td>
  					<td>${order.phone!"-"}</td>
  				<#else>
  					<td colspan="3">Адрес не указан</td>
  				</#if>
  				<td>${order.email!"-"}</td>
  				<td>${order.deliveryComment!?replace("\n", "<br/>")}</td>
  			</tr>
  		</#list>
  	</table>
  </body>
</html>