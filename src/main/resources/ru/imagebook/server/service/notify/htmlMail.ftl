<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
    <meta name="viewport" content="width=device-width" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>ImagebookEmails</title>
</head>
<body style="margin: 0; padding: 0;">
<table cellpadding="0" cellspacing="0" width="100%" align="center" style="border-collapse: collapse; background-color: #F8F8F8;">
    <tr>
        <td align="center" style="padding: 40px 30px;">
            <table border="0" cellpadding="0" cellspacing="0" width="700" style="border: 1px solid #E7E7E7; background-color: #FFFFFF;">
                <tr>
                    <td style="padding: 20px 20px 0 20px;">
                        <h2 style="color: #222222; font-family: Helvetica, Arial, sans-serif; font-size: 24px; font-weight: normal; line-height: 1.4; margin-top: 0;">
                            Здравствуйте<#if user.helloName?has_content>, ${user.helloName}</#if>!
                        </h2>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 0 20px 0 20px; color: #444444; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4;">
                        ${text}
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px 20px 20px 20px;">
                        <p style="color: #222222; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4; margin-bottom: 10px;">
                            С уважением,<br>Администрация ${vendor.name}
                        </p>
                    </td>
                </tr>
            </table>
            <table  border="0" cellpadding="0" cellspacing="0" width="700" style="border-collapse: collapse;">
                <tr>
                    <td align="left" valign="top" width="45%" style="padding: 20px;">
                        <p style="color: #222222; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4; margin-top: 0; margin-bottom: 0;">
                            ${vendor.companyName}<br>
                            <a href="http://${vendor.site}">www.${vendor.site}</a>
                            <#if unsubscribeCode??>
                                <br><br>
                                <a href="http://${vendor.officeUrl}/unsubscribe?userId=${user.id}&hash=${unsubscribeCode}">Отписаться от рассылки</a>
                            </#if>
                        </p>
                    </td>
                    <td align="right" valign="top" width="45%" style="padding: 20px;">
                        <p style="color: #222222; font-family: Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.4; margin-top: 0; margin-bottom: 10px;">
                            <#if vendor.phone??>Тел.: ${vendor.phone}<br></#if>
                            E-mail: <a href="mailto:${vendor.email}">${vendor.email}</a>
                        </p>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>