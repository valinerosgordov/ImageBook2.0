<!doctype html>
 
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  	<link rel="stylesheet" type="text/css" href="/static/css/price.css" />
    <title>Imagebook price</title>
  </head>
  <body>
  	<#list products as product>
  		<div>
  			<h2>${product.typeName.ru} [${product.type}] - ${product.name.ru} [${product.number}], ${product.blockFormat}</h2>
  			<table class="price-table">
  				<tr>
  					<th colspan="3"></th>
  					<th colspan="${product.pages?size}">Количество страниц</th>
  				</tr>
  				<tr>
  					<th>Цвет обложки</th>
  					<th>Ламинация обложки</th>
  					<th>Ламинация страниц</th>
  					<#list product.pages as page>
  						<th>${page.count}</th>
  					</#list>
  				</tr>
  				<#list product.colors as color>
  					<#list color.coverLams as coverLam>
  						<#list coverLam.pageLams as pageLam>
				  			<tr>
				  				<td>${color.color.name.ru}</td>
				  				<td>${coverLam.name.ru}</td>
				  				<td>${pageLam.name.ru}</td>
				  				
				  				<#list pageLam.pages as page>
				  					<td align="right">${page.price}</td>
				  				</#list>		
				  			</tr>	
				  		</#list>	
			  		</#list>	
  				</#list>
  			</table>
				<br/>
				<br/>  						
  		</div>
  	</#list>
  </body>
</html>