define(['angular', 'controllers', 'angularRoute',
        'text!tmpl/feedbackController.html', 'common/services',
        'text!common/tmpl/notification.html'],
    function(ng, controllers, ngRoute,
             feedbackControllerTmpl, commonServices,
             notification) {

    var feedbackApp = angular.module('feedbackApp',
        ['controllers', 'ngRoute', 'commonServices']);

    feedbackApp.config(['$httpProvider', 'notificationServiceProvider',
        function($httpProvider, notificationServiceProvider) {
            $httpProvider.interceptors.push('errorListenerService');
            notificationServiceProvider.setNotificationTmpl(notification);
    }]);

    feedbackApp.config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/', {
            template : feedbackControllerTmpl,
            controller : 'feedbackController'
        }).otherwise({
            'redirectTo' : '/'
        });
    }]);

    return feedbackApp;
});
