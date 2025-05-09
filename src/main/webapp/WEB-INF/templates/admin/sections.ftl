<!doctype html>

<#macro showSection section>
	<div style="margin: 10px 0px 10px ${(section.level * 20)?c}px;">
		<b><a href="http://imagebook.ru/${section.key!}" target="section">${section.name}</a></b><br/>
		<form method="POST" action="/admin/saveSection" target="saveSection">
			<input type="hidden" name="id" value="${section.id?c}" />
			<table>
				<tr>
					<td>H1:</td>
					<td><input type="text" name="h1" value="${section.h1!}" size="80" /></td>
				</tr>
				<tr>
					<td>Ключ:</td>
					<td><input type="text" name="key" value="${section.key!}" size="80" /></td>
				</tr>
				<tr>
					<td>Title:</td>
					<td><input type="text" name="title" value="${section.title!}" size="80" /></td>
				</tr>
				<tr>
					<td valign="top">Подвал:</td>
					<td><textarea name="footer" cols="120" rows="6">${section.footer!}</textarea></td>
				</tr>
				<tr>
					<td valign="top">Тег:</td>
					<td>
						<select name="tagId">
							<option value="0">-</option>
							<#list tags as tag>
								<option value="${tag.id?c}" <#if section.tag?? && section.tag == tag>selected="true"</#if>>${tag.name}</option>
							</#list>
						</select>
					</td>
				</tr>
			</table>
			<input type="submit" value="Сохранить" style="cursor: pointer" />
		</form>
	</div>
	<hr/>

	<#list section.children as child>
		<@showSection section=child />
	</#list>
</#macro>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta name='gwt:property' content='locale=ru'>
		<title>Imagebook - Система управления</title>
  </head>
  <body>
  	<@showSection section=section />
	</body>
</html>