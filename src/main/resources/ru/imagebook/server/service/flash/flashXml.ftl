<content width="${normalWidth?c}" height="${normalHeight?c}" swidth="${smallWidth?c}"
		bwidth="${largeWidth?c}" bheight="${largeHeight?c}"
		pnumber="${pageCount?c}"
		hcover="${hardCover}"
		transparency="true" bgColor="0xEAEAEA" startLanguage="rus">
	<#list pages as page>
		<page
			src="http://${flashUrl}${flashContextUrl}/image?a=${sessionId}&b=${page.type?c}&c=1&d=${page.number?c}"
			small="http://${flashUrl}${flashContextUrl}/image?a=${sessionId}&b=${page.type?c}&c=2&d=${page.number?c}"
			big="http://${flashUrl}${flashContextUrl}/image?a=${sessionId}&b=${page.type?c}&c=3&d=${page.number?c}"
			preLoad="false"
		/>
	</#list>
</content>