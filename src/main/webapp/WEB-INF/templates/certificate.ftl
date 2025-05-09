<#import "/layout/layouts.ftl" as layout>

<@layout.main>
<link href="/static/web/css/common.css" rel="stylesheet" type="text/css"/>
<div class="form">
    <form name='f' action='${checkCertificateUrl}/check' method='POST'>
        <p>
            Пожалуйста, укажите номер сертификата для проверки:
			<input type='text' class="i-input" name='bonusCode' value='<#if bonusCode??>${bonusCode}<#else></#if>' maxlength="200" width="400px"/>
			<@emailIsEmptyError/>
            <input type="submit" value="Проверить"/>
        </p>
		<#if isBonusCodeCheckSuccessfull?? && isBonusCodeCheckSuccessfull!false>
			<div class="i-alert">
				<@actionCodeInfo action/>
			</div>
		<#elseif isBonusCodeCheckSuccessfull?? && !isBonusCodeCheckSuccessfull>
			<div class="i-alert-warning">
				К сожалению, нам не удалось найти сертификат по указанному вами коду. Проверьте, правильно ли вы указали код или обратитесь к администратору.
			</div>
		</#if>
    </form>
</div>
</@layout.main>
<#macro actionCodeInfo actionCode>
	<#if (action.getDiscount1() > 0 )>
		<#local bonusCodeInfo>
		 	<#if (action.getDateEnd()??)>
				Сертификат на скидку ${action.getDiscount1()} %, действующий до ${action.getDateEnd()?date}
			<#else>
				Сертификат на скидку ${action.getDiscount1()} %.
		 	</#if>
		</#local>
	<#else>
		<#local bonusCodeInfo>
			<#if (action.getDateEnd()??)>
            Сертификат на сумму ${action.getDiscountSum()} руб., действующий до ${action.getDateEnd()?date}
			<#else>
            Сертификат на сумму ${action.getDiscountSum()} руб.
			</#if>
		</#local>
	</#if>
 ${bonusCodeInfo}
</#macro>
<#macro emailIsEmptyError>
<div id="email-empty-error" class="i-alert-danger"
     style="display: <#if isEmptyCodeError??>block<#else>none</#if>">
    Номер сертификата должен быть заполнен!
</div>
</#macro>