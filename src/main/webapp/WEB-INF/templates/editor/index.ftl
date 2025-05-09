<!doctype html>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta name='gwt:property' content='locale=ru'>
		<title>Онлайн-сборщик фотокниг ${serviceName}</title>
    <link rel="stylesheet" type="text/css" href="/static/gxt/css/gxt-all.css" />
		<link rel="stylesheet" type="text/css" href="/static/core/css/core.css">
		<link rel="stylesheet" type="text/css" href="/static/engine/css/engine.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/main.css">
		<link rel="stylesheet" type="text/css" href="/static/css/editor.css">
    <script language="javascript" src="/gwt.editor/gwt.editor.nocache.js"></script>
  </head>
  <body>
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
  </body>
</html>