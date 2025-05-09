<script src="/static/requirejs/require.js"></script>
<link rel="stylesheet" type="text/css" href="/static/web/css/bootstrap/bootstrap.min.css" />
<link href="/static/web/css/common.css" rel="stylesheet" type="text/css">

<div id="recommendationApp"></div>


<script>
require(['/static/web/js/recommendationAdmin/main.js'], function(){
    require(['angular', 'recommendationApp', 'jquery'], function(angular, recommendationApp, $) {

        var $appContainer = $('#recommendationApp');
        $appContainer.append('<div recommendation-controller-wrapper></div>');

        angular.element().ready(function () {
            angular.bootstrap($appContainer, [recommendationApp.name]);
        });
    });
});
</script>