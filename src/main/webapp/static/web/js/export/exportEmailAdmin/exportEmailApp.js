define(['angular', 'controllers', 'angularRoute',
        'text!tmpl/exportEmailController.html', 'css!cssFolder/exportEmailAdmin', 'common/services',
        'text!common/tmpl/bootstrapNotification.html'],
    function(ng, controllers, ngRoute,
             exportEmailControllerTmpl, exportEmailAdminCss, commonServices,
             bootstrapNotification) {

        var exportEmailApp = angular.module('exportEmailApp',
            ['exportEmailControllers', 'ngRoute', 'commonServices']);

        exportEmailApp.config(['$httpProvider', 'notificationServiceProvider',
            function($httpProvider, notificationServiceProvider) {
                $httpProvider.interceptors.push('errorListenerService');
                notificationServiceProvider.setNotificationTmpl(bootstrapNotification);
        }]);

        exportEmailApp.directive('exportEmailControllerWrapper', ['$log', function($log) {
            return {
                template: exportEmailControllerTmpl,
                controller: 'ExportEmailController',
                link: function(scope, element, attrs) {
                    $log.debug('exportEmailControllerWrapper started');
                }
            }
        }]);

        return exportEmailApp;
    });
