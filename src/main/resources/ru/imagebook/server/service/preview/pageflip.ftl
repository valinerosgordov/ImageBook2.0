<!doctype html>

<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name='gwt:property' content='locale=ru'>
    <title>Макет альбома</title>
    <script language="javascript" src="/static/jquery/jquery-1.11.0.min.js"></script>
    <link href="/static/pageflip/css/pageflip.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/static/pageflip/js/turn.min.js"></script>
    <script type="text/javascript" src="/static/pageflip/js/zoom.min.js"></script>
    <script type="text/javascript" src="/static/pageflip/js/pageflip.js"></script>
</head>
<body style="background-color: #f0f0f0">
    <#if title??><h3>${title}</h3></#if>

    <div id="zoom-viewport" style="width: 1054px;">
        <div class="book-container">
            <#--<#if flipper??>-->
                <#--&lt;#&ndash;${flipper}&ndash;&gt;-->
                <#--<div id="${id}" class="pageFlip-imagesPanel">-->
                    <#--<div ignore="1" class="next-button"></div>-->
                    <#--<div ignore="1" class="previous-button"></div>-->
                    <#--<div ignore="1" class="top-left-button"></div>-->
                    <#--<div ignore="1" class="top-right-button"></div>-->
                    <#--<div ignore="1" class="bottom-left-button"></div>-->
                    <#--<div ignore="1" class="bottom-right-button"></div>-->
                <#--</div>-->
                <#--<div id="thumbnailsPage" class="thumbnails"></div>-->
            <#--<#else>-->
                <div id="${id}" class="pageFlip-imagesPanel">
                    <div ignore="1" class="next-button"></div>
                    <div ignore="1" class="previous-button"></div>
                    <div ignore="1" class="top-left-button"></div>
                    <div ignore="1" class="top-right-button"></div>
                    <div ignore="1" class="bottom-left-button"></div>
                    <div ignore="1" class="bottom-right-button"></div>
                </div>
                <div id="thumbnailsPage" class="thumbnails"></div>
            <#--</#if>-->
        </div>
    </div>

    <#--<#if flipper??>-->
        <#--<script type="text/javascript">-->
            <#--pageFlip("${id}", ${albumWidth}, ${albumHeight}, ${nPages}, ${nInitialPages}, ${hard?string}, function(page) {-->
                <#--return "${imageUrlPattern}".replace("${"$"}{page}", page);-->
            <#--});-->
        <#--</script>-->
    <#--<#else>-->
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
</body>
</html>