<#import "layout/layouts.ftl" as layout>

<@layout.main>
	<link href="/static/css/social.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="http://userapi.com/js/api/openapi.js?52"></script>
    <script type="text/javascript">
        VK.init({apiId: 3086097, onlyWidgets: true});
    </script>

    <#if name??><h1>${name}</h1></#if>

	<#--<#if flipper??>-->
        <script language="javascript" src="/static/jquery/jquery-1.11.0.min.js"></script>
        <link href="/static/pageflip/css/pageflip.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="/static/pageflip/js/turn.min.js"></script>
        <script type="text/javascript" src="/static/pageflip/js/zoom.min.js"></script>
        <script type="text/javascript" src="/static/pageflip/js/pageflip.js"></script>

        <div id="zoom-viewport" style="width: 1054px;">
            <div class="book-container">
            <#if flipper??>
            <#--${flipper}-->
                <div id="${id}" class="pageFlip-imagesPanel">
                    <div ignore="1" class="next-button"></div>
                    <div ignore="1" class="previous-button"></div>
                    <div ignore="1" class="top-left-button"></div>
                    <div ignore="1" class="top-right-button"></div>
                    <div ignore="1" class="bottom-left-button"></div>
                    <div ignore="1" class="bottom-right-button"></div>
                </div>
                <div id="thumbnailsPage" class="thumbnails"></div>
            <#else>
                <div id="${id}" class="pageFlip-imagesPanel">
                    <div ignore="1" class="next-button"></div>
                    <div ignore="1" class="previous-button"></div>
                    <div ignore="1" class="top-left-button"></div>
                    <div ignore="1" class="top-right-button"></div>
                    <div ignore="1" class="bottom-left-button"></div>
                    <div ignore="1" class="bottom-right-button"></div>
                </div>
                <div id="thumbnailsPage" class="thumbnails"></div>
            </#if>
            </div>
        </div>

        <script type="text/javascript">
            pageFlip("${id}", ${albumWidth}, ${albumHeight}, ${nPages}, ${nInitialPages}, ${hard?string},
                    function(page) {
                        return loadImage(page, 1); //small
                    },
                    function(page) {
                        return loadImage(page, 3); //large
                    });

            function loadImage(page, pageSize) {
                var b = 1; //String.valueOf(PageType.NORMAL);
                var c = pageSize;
                var nPages = ${nPages};
                var d = page;
                    <#if isSeparateCover>
                        if (page == 1 ||page==2) {
                            b = 2; //String.valueOf(PageType.FRONT);
                        } else if(page==(nPages-1)||page==nPages){
                            b = 3; //String.valueOf(PageType.BACK);
                            if(page==nPages){
                                d=2;
                            } else{
                                d=1;
                            }
                        } else {
                            d=page-2;
                        }
                    </#if>
                return "${imageUrlPattern}"
                        .replace("${"$"}{b}", b)
                        .replace("${"$"}{c}", c)
                        .replace("${"$"}{d}", d);
            }
        </script>
    <#--</#if>-->

    <#--<#if flashParams??>-->
        <#--${clientParameters!}-->

        <#--<link rel="stylesheet" type="text/css" href="/static/css/flash.css">-->
        <#--<script type="text/javascript" src="/static/flash/js/flashobject.js"></script>-->

        <#--<div id="flashDiv" style="background: url(/static/flash/images/noflash.jpg) no-repeat left top; width: ${flashParams.flashWidth?c}px; height: ${flashParams.flashHeight?c}px;">-->
            <#--<div style="color: #0066b3; background-color: #EAEAEA; text-align: left; width: ${flashParams.flashWidth?c}px; height: ${flashParams.flashHeight?c}px; padding-left: 20px;">-->
                <#--<br/>-->
                <#--Для просмотра макетов альбомов необходим флэш-плеер.<br/>-->
                <#--Если вы видите эту надпись, то возможны следующие ситуации:<br/><br/>-->
                <#--1.) У вас не установлен флэш-плеер.<br/><br/>-->
                <#--В этом случае вы можете скачать флэш-плеер здесь:<br/>-->
                <#--<a href="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash&promoid=BUIGP" target="_blank">http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash&promoid=BUIGP</a><br/>-->
                <#--<br/>-->
                <#--2.) Флэш-плеер установлен некорректно.<br/><br/>-->
                <#--В этом случае вам следует удалить флэш-плеер с помощью программы Adobe Uninstaller.<br/>-->
                <#--Скачать Adobe Uninstaller вы можете здесь:<br/>-->
                <#--<a href="http://kb.adobe.com/selfservice/viewContent.do?externalId=tn_14157&sliceId=1" target="_blank">http://kb.adobe.com/selfservice/viewContent.do?externalId=tn_14157&sliceId=1</a><br/>-->
                <#--<br/>-->
                <#--После этого вам нужно будет установить флэш-плеер заново.<br/>-->
                <#--Скачать флэш-плеер вы можете здесь:<br/>-->
                <#--<a href="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash&promoid=BUIGP" target="_blank">http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash&promoid=BUIGP</a>-->
                <#--</p>-->
            <#--</div>-->
        <#--</div>-->

        <#--<script type="text/javascript">-->
            <#--var f = new FlashObject("${flashUrl}/static/flash/flash.swf", "flashDiv", "${flashParams.flashWidth?c}", "${flashParams.flashHeight?c}", "7", "");-->
            <#--f.addParam("FlashVars", "xmlFile=${flashUrl}${flashParams.flashContextUrl}/xml?a=${flashParams.sessionId}");-->
            <#--f.addParam("allowScriptAccess", "sameDomain");-->
            <#--f.addParam("quality", "high");-->
            <#--f.addParam("scale", "noscale");-->
            <#--f.addParam("wmode", "transparent");-->
            <#--f.write("flashDiv");-->
        <#--</script>-->

        <#--<#if showFlashes?? && showFlashes>-->
            <#--<script language="javascript" src="/gwt.flash/gwt.flash.nocache.js"></script>-->
            <#--<div id="app"></div>-->
        <#--</#if>-->
    <#--</#if>-->

    <div style="margin-left: 30px">
        <#if customerUrl??>
            <p><a href="${customerUrl}">Макет для заказчика</a></p>
        <#else>
            <p>Отправьте друзьям ссылку на эту страницу <a href="${url}">${url}</a> или расскажите об альбоме в социальных сетях:</p>

            <table>
                <tr>
                    <td>
                        <div id="vk_like" class="social-like"></div>
                        <script type="text/javascript">
                            VK.Widgets.Like('vk_like', {type: 'full', width: 150, height: 24, pageTitle: 'Моя фотокнига "${name!""}"', pageDescription: 'Создайте свою фотокнигу в сервисе ${vendor.name}', pageImage: '${imageUrl}' });
                        </script>
                    </td>
                    <td width="150px">
                        <div class="social-like">
                            <a target="_blank" class="mrc__plugin_uber_like_button" href="http://connect.mail.ru/share" data-mrc-config="{'cm' : '1', 'ck' : '1', 'sz' : '20', 'st' : '1', 'tp' : 'combo'}">Нравится</a>
                            <script src="http://cdn.connect.mail.ru/js/loader.js" type="text/javascript" charset="UTF-8"></script>
                        </div>
                    </td>
                    <td>
                        <div class="social-like">
                            <a href="https://twitter.com/share" class="twitter-share-button" data-text='Моя фотокнига "${name!""}"' data-lang="ru">Твитнуть</a>
                            <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
                        </div>
                    </td>
                </tr>
            </table>

            <table border="0" style="width: 100%;">
                <tr>
                    <td style="vertical-align: top;">
                        <img src="/static/web/images/publish_hint.png" />
                    </td>
                    <td style="width: 100px;">&nbsp;</td>
                    <td>
                        <br/>
                        <h2>Понравился альбом?</h2>
                        <p>В нашем сервисе вы можете напечатать фотоальбом со своими фотографиями и текстами. К вашим услугам дизайны для детей, свадеб, фотоальбомы из путешествий, для любимого человека и многие другие.</p>
                        <p><a class="blue" href="/">Создать фотокнигу своими руками!</a></p>
                    </td>
                </tr>
            </table>
        </#if>
    </div>
</@layout.main>