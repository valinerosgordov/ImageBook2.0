<!doctype html>

<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<#if page.keywords??><meta name="keywords" content="${page.keywords}"></#if>
	<#if page.description??><meta name="description" content="${page.description}"></#if>
	<meta name='gwt:property' content='locale=ru'>
	<title><#if page.title??>${page.title}<#else>Imagebook - печать фотокниг</#if></title>
	<link href="/static/web/css/main.css" rel="stylesheet" type="text/css" />
	<link href="/static/web/css/styles.css" rel="stylesheet" type="text/css" />
	<#if index || page.wide>
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
                    webvisor:true
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

	<#if passer??>${passer}</#if>
	<div id="topbg"></div>
	<div id="CLOAK">
		<div id="TOP">
	    <div id="topnav">
	        <div class="bg1">
	            <div class="bg2">
	            	<#list topSections as topSection>
	            		<a href="${topSection.url_}" <#if topSection.targetBlank>target="_blank"</#if>>${topSection.name}</a>
	            	</#list>
	            </div>
	        </div>
	    </div>
	    <div class="clear"></div>
	    <div id="logo">
	    	<a href="/" title="На главную"></a>
	    	<span class="tel"><strong>(+7 495)</strong> 958-24-25</span>
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
	
		<#if index>
			<div id="instruction">
				<p>1. Скачайте программу Imagebook</p>
		    <p>2. Создайте фотокнигу из своих<br/>любимых фотографий</p>
		    <p class="last">3. Получите книгу по почте или с<br/>курьером</p>
			</div>
		</#if>
	
		<div class="clear"></div>
		<div class="CENTER-WRAP" id="left-bg">
			<div class="MAIN">
				<div class="CENTER">
					<div class="content">
						<#if !index>
							<div id="links">
								<#list links as link>		
									<#if link_has_next><a href="${link.url}"></#if>${link.name}<#if link_has_next></a> / </#if>
								</#list>
							</div>
						</#if>
		        <#if page.h1??><h1>${page.h1}</h1></#if>
						<#if page.content_??>${page.content_}</#if>
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
				&copy; 2007&ndash;2010 ООО «ИМИДЖБУК»<br/><br/>
				<table>
					<tr>
						<td><a href="http://iisw.ru" target="_blank"><img src="/static/images/iis2.png" /></a></td>
						<td style="padding-left: 5px; vertical-align: bottom;">
							<a href="http://iisw.ru" target="_blank">Разработка информационных систем</a><br/>
						</td>
					</tr>
				</table>
				<br/>
			</div>
			<div id="foot-2">
				<span class="tel"><strong>(+7 495)</strong> 958-24-25</span>
		    <a href="mailto:mail@imagebook.ru">mail@imagebook.ru</a>
		  </div>
			<div id="foot-3">
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
</body>
</html>
