<script src="/static/requirejs/require.js"></script>
<link rel="stylesheet" type="text/css" href="/static/web/css/bootstrap/bootstrap.min.css" />
<link href="/static/web/css/common.css" rel="stylesheet" type="text/css">

<div id="bannerApp"></div>


<script>
require(['/static/web/js/bannerAdmin/main.js'], function(){
    require(['angular', 'bannerApp', 'jquery'], function(angular, bannerApp, $) {

        var $appContainer = $('#bannerApp');
        $appContainer.append('<div banner-controller-wrapper></div>');

        angular.element().ready(function () {
            angular.bootstrap($appContainer, [bannerApp.name]);
        });
    });
});
</script>