<#import "layout/layouts.ftl" as layout>

<@layout.main>
	<div id="toAskQuestion" <#if !toAsk??>style="display:none;"</#if>></div>

	<#list questionCategories as questionCategory>
		<div class="question-category">
     	<a href="${questionCategory.url}">${questionCategory.name}</a>
     	<#if (questionCategory.numberOfAnswers > 0)>(${questionCategory.numberOfAnswers})</#if>
    </div>
	</#list>

	<br/>
	<script language="javascript" src="/gwt.faq/gwt.faq.nocache.js"></script>
	<div id="askQuestion"></div>
	<div id="questionResult"></div>
	<div id="questionForm"></div><br/>
	<br/><br/>
</@layout.main>
