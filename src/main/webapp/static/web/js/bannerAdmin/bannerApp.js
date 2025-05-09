define(['angular', 'controllers', 'angularRoute',
        'text!tmpl/bannerController.html', 'css!cssFolder/bannerAdmin',
        'common/services', 'text!common/tmpl/bootstrapNotification.html'],
    function(ng, controllers, ngRoute,
             bannerControllerTmpl, bannerAdminCss,
             commonServices, bootstrapNotification) {

        var bannerApp = angular.module('bannerApp',
            ['bannerControllers', 'ngRoute', 'commonServices']);

        bannerApp.config(['$httpProvider', 'notificationServiceProvider',
            function($httpProvider, notificationServiceProvider) {
                $httpProvider.interceptors.push('errorListenerService');
                notificationServiceProvider.setNotificationTmpl(bootstrapNotification);
        }]);

        bannerApp.directive('bannerControllerWrapper', ['$log', function($log) {
            return {
                template: bannerControllerTmpl,
                controller: 'BannerController',
                link: function(scope, element, attrs) {
                    $log.debug('bannerControllerWrapper started');
                }
            }
        }]);

        return bannerApp;
    });

