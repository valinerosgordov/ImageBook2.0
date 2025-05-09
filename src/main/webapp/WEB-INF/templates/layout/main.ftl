<#macro main>
<!doctype html>

<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<#if page.keywords??><meta name="keywords" content="${page.keywords}"></#if>
	<#if page.description??><meta name="description" content="${page.description}"></#if>
	<meta name='gwt:property' content='locale=ru'>
	<title><#if page.title??>${page.title}<#else><#if page.h1??>${page.h1}<#else>Imagebook - печать фотокниг</#if></#if></title>
	<link rel="shortcut icon" type="image/x-icon" href="/static/web/images/favicon.gif">
	<link href="/static/web/css/main.css" rel="stylesheet" type="text/css" />
	<link href="/static/web/css/styles.css" rel="stylesheet" type="text/css" />
	<link href="/static/web/css/blue_bttn.css" rel="stylesheet" type="text/css" />

    <script type="text/javascript" src="/static/web/js/jquery/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="/static/web/js/jquery/jquery.cookie.min.js"></script>

    <!-- Add fancyBox main JS and CSS files -->
    <script type="text/javascript" src="/static/web/js/fancybox/jquery.fancybox.pack.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/web/css/jquery.fancybox.css" media="screen" />

    <!-- Add Button helper (this is optional) -->
    <link rel="stylesheet" type="text/css" href="/static/web/css/fancybox/helpers/jquery.fancybox-buttons.css" />
    <script type="text/javascript" src="/static/web/js/fancybox/helper/jquery.fancybox-buttons.js"></script>
	    
	<#if index?? || page.wide>
		<link href="/static/web/css/1col.css" rel="stylesheet" type="text/css" />
	<#else>
		<link href="/static/web/css/2cols.css" rel="stylesheet" type="text/css" />
	</#if>
	<link href="/static/web/css/dir.css" rel="stylesheet" type="text/css" />
	<#if calc??>
		<link rel="stylesheet" type="text/css" href="/static/css/calc.css">
		<script language="javascript" src="/calc/calc.nocache.js"></script>
	</#if>
	<script type="text/javascript">
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-18754681-1']);
	  _gaq.push(['_trackPageview']);

	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();
	</script>
</head>

<body>

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

    <script type="text/javascript">
        jQuery(document).ready(function() {
            jQuery("a").fancybox();
        });
    </script>

	${clientParameters!}
	<div id="topbg"></div>
	<div id="CLOAK">
		<div id="TOP">
	    <div id="topnav">
	        <div class="bg1">
	            <div class="bg2">
	            	<#list topSections as topSection>
	            		<a href="${topSection.url_}" <#if topSection.targetBlank>target="_blank"</#if>>${topSection.name}</a>
	            	</#list>
                    <a href="/site/checkCertificate">Проверка сертификата по коду</a>
	            </div>
	        </div>
	    </div>
	    <div class="clear"></div>
	    <div id="logo">
	    	<a href="/" id="${logoImageId!"logo-img"}" title="На главную"></a>
	    	<span class="tel">
	    		<strong>8 495</strong> 765-76-74<br/>
	    		<strong>8 800</strong> 250-76-74
	    	</span>
	    </div>
	    <#list banners as banner>
		    <div class="topbanner">
		    	<#if banner.title??>
		    		<h2>
		    			<#if banner.url??><a href="${banner.url}" <#if banner.targetBlank>target="_blank"</#if>></#if>
		    			${banner.title}
		    			<#if banner.url??></a></#if>
		    		</h2>
		    	</#if>
		    	${banner.content}
		    </div>
	    </#list>
	    <div class="clear"></div>
	    <div id="main-nav">
	        <div class="bg1">
	            <table class="bg2">
	            	<tr>
	            		<#list sections1 as section>
		                <td
		                	<#if section_index == 0>id="first"</#if>
		                	<#if !section_has_next>class="last"</#if>
		                >
		                	<a href="${section.url_}" <#if section.targetBlank>target="_blank"</#if>>${section.name}</a>
		                </td>
	                </#list>
	                </tr>
	            </table>
	      </div>
	    </div>
		</div>

		<#if index??>
			<div id="instruction">
				<p>1. Выберите один из <a href="${prefix}/download">редакторов фотокниг</a></p>
		    <p>2. Создайте фотокнигу из своих<br/>любимых фотографий</p>
		    <p class="last">3. Получите книгу по почте или с<br/>курьером</p>
			</div>
		</#if>

		<div class="clear"></div>
		<div class="CENTER-WRAP" id="left-bg">
			<div class="MAIN">
				<div class="CENTER">
					<div class="content">
						<#if !index??>
							<div id="links">
								<#list page.breadcrumbs as breadcrumb>
									<#if breadcrumb_has_next><a href="${breadcrumb.url}"></#if>${breadcrumb.name}<#if breadcrumb_has_next></a> / </#if>
								</#list>
							</div>
						</#if>
		        <#if page.h1??><h1>${page.h1}</h1></#if>
						<#nested>
					</div>
				</div>
				<div class="RIGHT">
	      </div>
			</div>

	   	<div class="LEFT">
	   		<#if sections2??>
					<ul id="L-nav">
						<#list sections2 as section>
				  		<li>
				  			<a href="${prefix}/${section.key}" <#if section.selected??>class="selected"</#if>>${section.name}</a>
				    		<#if (section.sections3??)>
				    			<ul>
				    				<#list section.sections3 as section>
				    					<li><a href="${prefix}/${section.key}" <#if section.selected??>class="selected"</#if>>${section.name}</a></li>
				    				</#list>
				    			</ul>
				    		</#if>
				    	</li>
				  	</#list>
				  </ul>
				</#if>

				<#if dir??>
					<ul id="L-nav">
						<li>
			  			<a href="${catalogUrl}">Фотокниги</a>
			  			<ul>
			  				<#list dirSections as dirSection>
						  		<li>
						  			<a href="${catalogUrl}/${dirSection.key}" <#if dirSection.selected??>class="selected"</#if>>${dirSection.name}</a>
						  			<#if (dirSection.sections2??)>
							  			<ul>
							  				<#list dirSection.sections2 as dirSection2>
										  		<li>
										  			<a href="${catalogUrl}/${dirSection.key}/${dirSection2.key}" <#if dirSection2.selected??>class="selected"</#if>>${dirSection2.name}</a>
										    	</li>
										  	</#list>
							  			</ul>
							  		</#if>
						    	</li>
						  	</#list>
			  			</ul>
			    	</li>
				  </ul>
				</#if>
	    </div>

		</div>
		<div id="FOOTER">
			<div id="foot-1">
				&copy; 2007&ndash;${year?c} ООО «ИМИДЖБУК» ${page.footer!?replace("\n", "<br/>")}<br/><br/>
				<a href="/soglashenie">Соглашение об обработке персональных данных и политика конфиденциальности</a><br/><br/>
				<a href="/var/files/doc/offer.pdf" title="Публичная оферта Имиджбук">Публичная оферта Имиджбук</a><br/><br/>
				<a href="/partners" title="Партнеры">Партнеры</a><br/>
				<br/><br/>
                <a href="http://minogin.ru/napravleniya/poligrafiya" target="_blank"><img src="/static/images/mt-logo.png"></a><br>
				<a href="http://minogin.ru/napravleniya/poligrafiya" target="_blank">Сайты, сервисы и автоматизация для полиграфии</a><br/><br/>
				<a href="http://blog.copy-write.ru/avtor">Продвижение и реклама — бюро Баканева</a><br/><br/>&nbsp;
			</div>
			<div id="foot-2">
				<a href="http://www.facebook.com/pages/Imagebook/197177200411144" target="_blank"><img src="/static/web/images/facebook.png" /></a>
				<a href="http://vk.com/imagebook" target="_blank"><img src="/static/web/images/vk.gif" /></a>
				<a href="https://twitter.com/IMAGEBOOK_Ltd" target="_blank"><img src="/static/web/images/twitter.png" /></a>
			</div>
			<div id="foot-3">
				<span class="tel">
					<strong>8 495</strong> 765-76-74<br/> 
					<strong>8 800</strong> 250-76-74<br/> 
				</span>
		    <a href="mailto:mail@imagebook.ru">mail@imagebook.ru</a>
		    <br/><br/>
				<!--LiveInternet counter--><script type="text/javascript"><!--
				document.write("<a href='http://www.liveinternet.ru/click' "+
				"target=_blank><img src='//counter.yadro.ru/hit?t52.1;r"+
				escape(document.referrer)+((typeof(screen)=="undefined")?"":
				";s"+screen.width+"*"+screen.height+"*"+(screen.colorDepth?
				screen.colorDepth:screen.pixelDepth))+";u"+escape(document.URL)+
				";"+Math.random()+
				"' alt='' title='LiveInternet: показано число просмотров и"+
				" посетителей за 24 часа' "+
				"border='0' width='88' height='31'><\/a>")
				//--></script><!--/LiveInternet-->

	   		<!--<a href="#" id="skype">Skype</a>
	   		<a href="#" id="vk">В Контакте</a><br />
	   		<a href="#" id="icq">ICQ</a>
	   		<a href="#" id="lj">LiveJournal</a>-->
	    </div>
		</div>
	</div>

	<div id="cookieNoticeBox" class="cookieNotice">
		<span id="cookieNoticeClose" class="cookieNotice-close">×</span>
		Продолжая использовать сайт <a href="http://imagebook.ru">imagebook.ru</a>, а также сервисы на всех поддоменах <a href="http://imagebook.ru">imagebook.ru</a>,<br/>
		Вы даете согласие на обработку файлов cookie и пользовательских данных.
	</div>
	<script type="text/JavaScript">
		$(document).ready(function (){
			if (!$.cookie('was')) {
				$('#cookieNoticeBox').show();
			}
		});

        $("#cookieNoticeClose").click(function () {
            $("#cookieNoticeBox").hide();
            $.cookie('was', true, { expires: 365, path: '/' });
        });
	</script>

	<!-- BEGIN JIVOSITE CODE {literal} -->
	<script type='text/javascript'>
	(function(){ var widget_id = '82244';
	var s = document.createElement('script'); s.type = 'text/javascript'; s.async = true; s.src = '//code.jivosite.com/script/widget/'+widget_id; var ss = document.getElementsByTagName('script')[0]; ss.parentNode.insertBefore(s, ss);})();</script>
	<!-- {/literal} END JIVOSITE CODE -->

</body>
</html>
</#macro>