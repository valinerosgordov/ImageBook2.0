<#import "/layout/layouts.ftl" as layout>

<@layout.main>
	<table class="dir">
		<tr>
			<td colspan="2 valign="top">
				${section2.preview}
			</td>
		</tr>
		<#list section2.albums as album>
			<tr>
				<td colspan="2" class="album"><h2><a href="${catalogUrl}/book/${album.id}">${album.name.ru}</a></h2></td>
			</tr>
			<tr>
				<td>
					<a href="${catalogUrl}/book/${album.id}"><img src="${itemImageUrl}/${album.id}.jpg" /></a>
				</td>
				<td valign="top">
					<table>
						<tr>
							<td>Тип:</td>
							<td><a href="${prefix}/cover#${album.cover}">${album.coverText}</a></td>
						</tr>
						<tr>
							<td>Формат:</td>
							<td><a href="${prefix}/format">${album.blockFormat}</a></td>
						</tr>
						<tr>
							<td>Бумага:</td>
							<td><a href="${prefix}/paper#${album.paper}">${album.paperText}</a></td>
						</tr>
						<tr>
							<td>Варианты ламинации обложки:</td>
							<td><a href="${prefix}/lamination">${album.coverLaminationText}</a></td>
						</tr>
						<tr>
							<td>Варианты ламинации страниц:</td>
							<td><a href="${prefix}/lamination">${album.pageLaminationText}</a></td>
						</tr>
						<tr>
							<td>Количество страниц:</td>
							<td>
								<#if album.minPageCount == album.maxPageCount>
									${album.minPageCount}
								<#else>
									${album.minPageCount} — ${album.maxPageCount}
								</#if>
							</td>
						</tr>
						<tr>
							<td>Стоимость без учета скидки:</td>
							<td>${album.minPrice} руб. — ${album.maxPrice} руб.</td>
						</tr>
					</table>
				</td>
			</tr>
		</#list>
	</table>
</@layout.main>