<#import "/layout/layouts.ftl" as layout>

<@layout.main>
	<table>
		<tr>
			<td valign="top">
				<img src="${itemImageUrl}/${album.id}.jpg" />
			</td>
			<td valign="top" style="padding-left: 10px">
				<table>
					<tr>
						<td>Тип:</td>
						<td><a href="/books">${album.coverText}</a></td>
					</tr>
					<tr>
						<td>Формат:</td>
						<td>${album.blockFormat}</td>
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
		<tr>
			<td colspan="2" class="album-section">
				<h2>Стоимость</h2>
				<div id="webcalc"></div>
			</td>
		</tr>

		<#if images??>
			<tr>
				<td colspan="2" class="album-section album-photos">
					<h2>Фото</h2>
					<#list images as image>
						<img src="${itemImageUrl}/${album.id}/${image}" />
						<#if image_index % 2 == 1><br/></#if>
					</#list>
				</td>
			</tr>
		</#if>

		<#if flash??>
			<tr>
				<td colspan="2" class="album-section">
					<h2>Примеры</h2>
					${flash}
				</td>
			</tr>
		</#if>
	</table>
</@layout.main>