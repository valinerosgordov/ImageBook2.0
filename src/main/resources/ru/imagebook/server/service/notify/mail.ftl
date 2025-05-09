Здравствуйте<#if name?has_content>, ${name}</#if>!

${text}

С уважением,
Администрация ${data.name}

${data.companyName}
E-mail: ${data.email}
<#if data.phone??>Тел.: ${data.phone}</#if>
www.${data.site}