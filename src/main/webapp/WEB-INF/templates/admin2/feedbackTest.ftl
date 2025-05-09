<script src="/static/requirejs/require.js"></script>
<link rel="stylesheet" type="text/css" href="/static/web/css/bootstrap/bootstrap.min.css" />
<link href="/static/web/css/common.css" rel="stylesheet" type="text/css">

<div id="feedbackAdminApp"></div>

<script>
    require(['/static/web/js/feedback/admin/main.js'], function(main) {
        require(['angular', 'feedbackAdminApp', 'jquery'], function(angular, app, $) {

            var $appContainer = $('#feedbackAdminApp');
            $appContainer.append('<div feedback-admin-controller-wrapper></div>');

            angular.element().ready(function () {
                angular.bootstrap($appContainer, [app.name]);
            });
        });
    })
</script>