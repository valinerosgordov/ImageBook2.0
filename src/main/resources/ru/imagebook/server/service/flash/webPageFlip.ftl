document.write('<div id=\"zoom-viewport-${id}\" style=\"width: 1054px;\"> \
<div class=\"book-container\"> \
    <div id=\"${id}\" class=\"pageFlip-site-imagesPanel\"> \
        <div ignore=\"1\" class=\"next-button\"></div> \
        <div ignore=\"1\" class=\"previous-button\"></div> \
        <div ignore=\"1\" class=\"top-left-button\"></div> \
        <div ignore=\"1\" class=\"top-right-button\"></div> \
        <div ignore=\"1\" class=\"bottom-left-button\"></div> \
        <div ignore=\"1\" class=\"bottom-right-button\"></div> \
    </div> \
    <div id=\"thumbnailsPage\" class=\"thumbnails\"></div> \
</div></div> \
<script type=\"text/javascript\"> \
    pageFlipSite(\"${id}\", ${albumWidth}, ${albumHeight}, ${nPages}, ${nInitialPages}, ${hard?string}, \
            function(page) { \
                var b = 1; \
                var c = 1; \
                var nPages = ${nPages}; \
                var d = page; \
                            <#if isSeparateCover>
                                if (page == 1 || page==2) { \
                                    b = 2; \
                                } else if(page==(nPages-1)||page==nPages){ \
                                    b = 3; \
                                    if(page==nPages){ \
                                        d=2; \
                                    } else{ \
                                        d=1; \
                                    } \
                                } else { \
                                    d=page-2; \
                                } \
                            </#if>
                return \"${webPrefix}/webImage?a=${sessionId}&b=${r"${b}"}&c=${r"${c}"}&d=${r"${d}"}\" \
                        .replace(\"${"$"}{b}\", b) \
                        .replace(\"${"$"}{c}\", c) \
                        .replace(\"${"$"}{d}\", d); \
            }, \
            function(page) { \
                var b = 1; \
                var c = 3; \
                var nPages = ${nPages}; \
                var d = page; \
                            <#if isSeparateCover>
                                if (page == 1 || page==2) { \
                                    b = 2; \
                                } else if(page==(nPages-1)||page==nPages){ \
                                    b = 3; \
                                    if(page==nPages){ \
                                        d=2; \
                                    } else{ \
                                        d=1; \
                                    } \
                                } else { \
                                    d=page-2; \
                                } \
                            </#if>
                return \"${webPrefix}/webImage?a=${sessionId}&b=${r"${b}"}&c=${r"${c}"}&d=${r"${d}"}\" \
                        .replace(\"${"$"}{b}\", b) \
                        .replace(\"${"$"}{c}\", c) \
                        .replace(\"${"$"}{d}\", d); \
    }); \
</script>');