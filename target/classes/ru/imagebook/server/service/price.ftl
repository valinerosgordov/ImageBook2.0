<!doctype html>
 
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  	<link rel="stylesheet" type="text/css" href="/static/css/price.css" />
    <title>Imagebook - прайс-лист</title>
  </head>
  <body>
  	<table>
  		<tr>
  			<td>
  				<table>
  					<tr>	
	  					<td width="100px"><a href="http://imagebook.ru"><img src="/static/images/logo.jpg" /></a></td>
  						<td valign="top">
  							<div class="title">Imagebook</div>
  							<div class="title2">Прайс-лист</div>
  						</td>
  					</tr>
  				</table>
  			</td>
  		</tr>
  		<#if showNavigation>
  		<tr>
  			<td>
  				<a name="contents"></a>
  				<h2>Альбомы</h2>
  				<#list types as type>
  					<span class="contents-section1">${type.name.ru}</span><br/>
  					<#list type.albums as album>
  						<a href="#album_${album.id}" class="contents-section2">${album.name.ru}</a><br/>
  					</#list>
  				</#list>
  				<br/><br/><br/>
  			</td>
  		</tr>
  		</#if>
  		<tr>
  			<td>
			  	<#list products as product>
			  		<div>
			  			<a name="album_${product.id}"></a>
			  			<h2>${product.name.ru}</h2>
			  			
			  			<#if showNavigation>
			  			<a href="#contents">Перейти к списку альбомов</a><br/><br/>
			  			</#if>
			  			
		  				Формат: ${product.blockFormat}<br/>
		  				Бумага: ${product.paperName.ru}<br/>
		  				Указана цена за один альбом в рублях РФ.<br/>
		  				<br/>
			  			<table class="price-table">
			  				<tr>
			  					<th colspan="5"></th>
			  					<th colspan="${product.pages?size}" align="left">Количество страниц</th>
			  				</tr>
			  				<tr>
			  					<th>Обложка</th>
			  					<th>Ламинация<br/>обложки</th>
			  					<th>Ламинация<br/>страниц</th>
			  					<th>Кол-во заказываемых<br/>экземпляров</th>
			  					<th>Бонусный<br/>статус</th>
			  					<#list product.pages as page>
			  						<th>${page.count}</th>
			  					</#list>
			  				</tr>
			  				<#list product.colors as color>
			  					<#list color.coverLams as coverLam>
			  						<#list coverLam.pageLams as pageLam>
			  							<#list pageLam.quantities as quantity>
								  			<tr>
								  				<td class="cell${pageLam.even}">${color.colorName}</td>
								  				<td class="cell${pageLam.even}">${coverLam.name.ru}</td>
								  				<td class="cell${pageLam.even}">${pageLam.name.ru}</td>
								  				<td class="cell${pageLam.even}">${quantity.minQuantity} - ${quantity.maxQuantity}</td>
								  				<td class="cell${pageLam.even}" align="center">${quantity.level}</td>
								  				<#list quantity.pages as page>
								  					<td class="cell${pageLam.even}" align="right">${page.price}</td>
								  				</#list>		
								  			</tr>	
							  			</#list>
							  		</#list>	
						  		</#list>	
			  				</#list>
			  			</table>
							<br/>
							<br/>  						
			  		</div>
			  	</#list>
  			</td>
  		</tr>
  	</table>
  </body>
</html>