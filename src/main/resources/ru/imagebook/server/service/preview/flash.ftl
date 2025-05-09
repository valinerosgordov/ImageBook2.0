<!doctype html>

<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name='gwt:property' content='locale=ru'>
    <title>Макет альбома</title>
    <link rel="stylesheet" type="text/css" href="/static/css/flash.css">
    <script type="text/javascript" src="/static/flash/js/flashobject.js"></script>
</head>
<body>
    <#if title??><h3>${title}</h3></#if>

    <div id="flashDiv" style="background: url(/static/flash/images/noflash.jpg) no-repeat left top; width: ${flashParams.flashWidth?c}px; height: ${flashParams.flashHeight?c}px;">
        <div style="color: #0066b3; background-color: #EAEAEA; text-align: left; width: ${flashParams.flashWidth?c}px; height: ${flashParams.flashHeight?c}px; padding-left: 20px;">
            <br/>
            Для просмотра макетов альбомов необходим флэш-плеер.<br/>
            Если вы видите эту надпись, то возможны следующие ситуации:<br/><br/>
            1.) У вас не установлен флэш-плеер.<br/><br/>
            В этом случае вы можете скачать флэш-плеер здесь:<br/>
            <a href="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash&promoid=BUIGP" target="_blank">http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash&promoid=BUIGP</a><br/>
            <br/>
            2.) Флэш-плеер установлен некорректно.<br/><br/>
            В этом случае вам следует удалить флэш-плеер с помощью программы Adobe Uninstaller.<br/>
            Скачать Adobe Uninstaller вы можете здесь:<br/>
            <a href="http://kb.adobe.com/selfservice/viewContent.do?externalId=tn_14157&sliceId=1" target="_blank">http://kb.adobe.com/selfservice/viewContent.do?externalId=tn_14157&sliceId=1</a><br/>
            <br/>
            После этого вам нужно будет установить флэш-плеер заново.<br/>
            Скачать флэш-плеер вы можете здесь:<br/>
            <a href="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash&promoid=BUIGP" target="_blank">http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash&promoid=BUIGP</a>
            </p>
        </div>
    </div>

    <script type="text/javascript">
        var f = new FlashObject("${flashUrl}/static/flash/flash.swf", "flashDiv", "${flashParams.flashWidth?c}", "${flashParams.flashHeight?c}", "7", "");
        f.addParam("FlashVars", "xmlFile=${flashUrl}${flashParams.flashContextUrl}/xml?a=${flashParams.sessionId}");
        f.addParam("allowScriptAccess", "sameDomain");
        f.addParam("quality", "high");
        f.addParam("scale", "noscale");
        f.addParam("wmode", "transparent");
        f.write("flashDiv");
    </script>
</body>
</html>