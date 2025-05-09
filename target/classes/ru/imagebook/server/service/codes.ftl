<!doctype html>

<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  	<title>Бонусные коды</title>
  </head>
  <body>
  	<h1>${action.name}</h1>
  	<table>
  	<#list action.codes as code>
	  	<tr>
	  		<td width="100px">${code.number + 1}</td>
	  		<td>${code.code}</td>
	  	</tr>
  	</#list>
  	</table>
  </body>
</html>