<table>
	<tr>
		<td class="dir1"><a href="${catalogUrl}/all">Все фотокниги</a></td>
	</tr>
	<#list sections as section>
		<tr>
			<td valign="middle" class="dir1"><a href="${catalogUrl}/${section.key}">${section.name}</a></td>
		</tr>
		<#list section.sections as section2>
			<tr>
				<td class="dir2"><a href="${catalogUrl}/${section.key}/${section2.key}">${section2.name}</a></td>
			</tr>
		</#list>
	</#list>
</table>