<script src="/static/requirejs/require.js"></script>
<link rel="stylesheet" type="text/css" href="/static/web/css/bootstrap/bootstrap.min.css" />
<link href="/static/web/css/common.css" rel="stylesheet" type="text/css">

<div id="exportEmailApp"></div>


<script>
require(['/static/web/js/export/exportEmailAdmin/main.js'], function(){
    require(['angular', 'exportEmailApp', 'jquery'], function(angular, exportEmailApp, $) {

        var $appContainer = $('#exportEmailApp');
        $appContainer.append('<div export-email-controller-wrapper></div>');

        angular.element().ready(function () {
            angular.bootstrap($appContainer, [exportEmailApp.name]);
        });
    });
});
</script>