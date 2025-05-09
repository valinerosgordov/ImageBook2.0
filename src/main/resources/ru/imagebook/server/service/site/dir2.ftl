<table class="dir">
	<#list sections2 as section2>
		<tr>
			<td colspan="2">
				<h2><a href="${catalogUrl}/${section1.key}/${section2.key}">${section2.name}</a></h2>
			</td>
		</tr>
		<tr>
			<td valign="top"><a href="${catalogUrl}/${section1.key}/${section2.key}"><img src="${sectionImageUrl}/${section2.key}.jpg" /></a></td>
			<td valign="top">
				${section2.preview}
				<br/><br/>
			</td>
		</tr>
	</#list>
</table>