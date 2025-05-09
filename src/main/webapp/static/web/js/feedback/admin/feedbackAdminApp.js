define(['angular', '../admin/controllers', 'angularRoute',
        'text!tmpl/feedbackAdminController.html', 'ckeditor',
        'css!cssFolder/feedbackAdmin', 'common/services',
        'text!common/tmpl/bootstrapNotification.html'],
    function(ng, controllers, ngRoute,
             feedbackAdminControllerTmpl, ckeditor,
             feedbackAdminCss, commonServices,
             bootstrapNotification) {

        var feedbackAdminApp = angular.module('feedbackAdminApp',
            ['feedbackControllers', 'ngRoute', 'commonServices']);

        feedbackAdminApp.config(['$httpProvider', 'notificationServiceProvider',
            function($httpProvider, notificationServiceProvider) {
                $httpProvider.interceptors.push('errorListenerService');
                notificationServiceProvider.setNotificationTmpl(bootstrapNotification);
        }]);

        feedbackAdminApp.directive('feedbackAdminControllerWrapper', ['$log', function($log) {
            return {
                template: feedbackAdminControllerTmpl,
                controller: 'FeedbackAdminController',
                link: function(scope, element, attrs) {
                    $log.debug('feedbackAdminControllerWrapper started');
                }
            }
        }]);

        return feedbackAdminApp;
    });
