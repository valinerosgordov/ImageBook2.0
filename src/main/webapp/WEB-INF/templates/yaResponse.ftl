<#ftl strip_whitespace="true" strip_text="true">
<#escape x as x?xml>
<?xml version="1.0" encoding="UTF-8"?>
<${action}Response performedDatetime="${today?string("yyyy-MM-dd")}T${today?string("HH:mm:ss")}" code="${code?c}" invoiceId="${invoiceId}" shopId="${shopId?c}"/>
</#escape>