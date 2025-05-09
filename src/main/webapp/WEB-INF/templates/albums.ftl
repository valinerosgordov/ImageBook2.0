<#import "/layout/layouts.ftl" as layout>

<@layout.main>
	<table class="dir">
		<#list albums as album>
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
							<td><a href="${prefix}/type_cover">${album.coverText}</a></td>
						</tr>
						<tr>
							<td>Формат:</td>
							<td><a href="${prefix}/format_size">${album.blockFormat}</a></td>
						</tr>
						<tr>
							<td>Бумага:</td>
							<td>${album.paperText}</td>
						</tr>
						<tr>
							<td>Варианты ламинации обложки:</td>
							<td>${album.coverLaminationText}</td>
						</tr>
						<tr>
							<td>Варианты ламинации страниц:</td>
							<td>${album.pageLaminationText}</td>
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