<#import "layout/layouts.ftl" as layout>

<@layout.main>
		<div id="toAskQuestion" <#if !toAsk??>style="display:none;"</#if>></div>
		<#list questions as question>
		<div class="question">
			<p class="date">${question.date?date}</p>
			<p class="question">
				${question.question?html?replace("\n", "<br/>")}
			</p>
			<p class="name">${question.name!}</p>
			<p class="answer">
				${question.answer}
			</p>
		</div>
		</#list>
		<script language="javascript" src="/gwt.faq/gwt.faq.nocache.js"></script>

		<div id="askQuestion"></div>
		<div id="questionResult"></div>
		<div id="questionForm"></div><br/>

		<table class="pager">
			<tr>
				<td>Страницы: </td>
				<#list pagerItems as pagerItem>
					<td>
						<#if pagerItem.url??><a href="${pagerItem.url}"></#if>
							${pagerItem.name}
						<#if pagerItem.url??></a></#if>
					</td>
				</#list>
			</tr>
		</table>
</@layout.main>