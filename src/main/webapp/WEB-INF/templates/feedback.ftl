<#import "layout/layouts.ftl" as layout>

<@layout.main>
    <link href="/static/web/css/feedback.css" rel="stylesheet" type="text/css">
    <link href="/static/web/css/jquery.bxslider.css" rel="stylesheet" type="text/css">
    <link href="/static/web/css/common.css" rel="stylesheet" type="text/css">
    <link href="/static/web/css/carousel.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="//vk.com/js/api/openapi.js?115"></script>

    <div id="feedbackApp">
        <div ng-view></div>
    </div>

    <script src="/static/requirejs/require.js"></script>
<script>
    require(['/static/web/js/feedback/site/main.js'], function(main) {
        require(['angular', 'app', 'jquery'], function(angular, app, $) {
            var $html = angular.element($('#feedbackApp')[0]);

            angular.element().ready(function () {
                angular.bootstrap($html, [app['name']]);
            });
        });
    })
</script>
</@layout.main>
