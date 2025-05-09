<!doctype html>

<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name='gwt:property' content='locale=ru'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${serviceName} - личный кабинет</title>
    <link rel="stylesheet" type="text/css" href="/static/css/app.css">
    <script language="javascript" src="/static/es5shim/es5-shim.min.js"></script>
    <script language="javascript" src="/static/jquery/jquery-1.11.0.min.js"></script>
    <script language="javascript" src="/static/jquery/jquery.inputmask.js"></script>
    <script type="text/javascript" src="/static/JSON-js/json2.js"></script>
    <#-- Pickpoint widget -->
    <script type="text/javascript" src="https://pickpoint.ru/select/postamat.js" charset="utf-8"></script>
    <script type="text/javascript" src="/static/imagebook/app/pickpoint.js" charset="utf-8"></script>
    <#-- SDEK widget -->
    <script id="ISDEKscript" type="text/javascript" src="https://widget.cdek.ru/widget/widjet.js"></script>
    <#-- Album preview -->
    <link href="/static/pageflip/css/pageflip.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/static/pageflip/js/turn.min.js"></script>
    <script type="text/javascript" src="/static/pageflip/js/zoom.min.js"></script>
    <script type="text/javascript" src="/static/pageflip/js/pageflip.js"></script>
    <#-- Flash -->
    <link rel="stylesheet" type="text/css" href="/static/css/flash.css">
    <script type="text/javascript" src="/static/flash/js/flashobject.js"></script>
    <script type="text/javascript" src="/static/jquery/jquery-scrollto.js"></script>
    <#-- App -->
    <script language="javascript" src="/gwt.app/gwt.app.nocache.js"></script>
    <#-- Bootstrap -->
    <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/static/bootstrap/js/bootstrap.min.js" charset="utf-8"></script>
    ${jivosite!""}
</head>
<body>
    <iframe src="javascript:''"
        id="__gwt_historyFrame"
        style="position:absolute;width:0;height:0;border:0"></iframe>
<#if yandexMetrika??>
    <!-- Yandex.Metrika counter -->
    <script type="text/javascript">
        (function (d, w, c) {
            (w[c] = w[c] || []).push(function() {
                try {
                    w.yaCounter1591621 = new Ya.Metrika({
                        id:1591621,
                        clickmap:true,
                        trackLinks:true,
                        accurateTrackBounce:true,
                        webvisor:true,
                        trackHash:true,
                        ecommerce:"dataLayer"
                    });
                } catch(e) { }
            });

            var n = d.getElementsByTagName("script")[0],
                    s = d.createElement("script"),
                    f = function () { n.parentNode.insertBefore(s, n); };
            s.type = "text/javascript";
            s.async = true;
            s.src = "https://mc.yandex.ru/metrika/watch.js";

            if (w.opera == "[object Opera]") {
                d.addEventListener("DOMContentLoaded", f, false);
            } else { f(); }
        })(document, window, "yandex_metrika_callbacks");
    </script>
    <noscript><div><img src="https://mc.yandex.ru/watch/1591621" style="position:absolute; left:-9999px;" alt="" /></div></noscript>
    <!-- /Yandex.Metrika counter -->
</#if>
${clientParameters!}
</body>
</html>