define(['angular', 'controllers', 'angularRoute', 'text!tmpl/recommendationCntr.html',
        'css!cssFolder/recommendationAdmin', 'common/services',
        'text!common/tmpl/bootstrapNotification.html'],
    function(ng, controllers, ngRoute, recommendationCntrTmpl,
             recommendationAdminCss, commonServices,
             bootstrapNotification) {

        var recommendationAdminApp = angular.module('recommendationApp',
            ['recommendationControllers', 'ngRoute', 'commonServices']);


        recommendationAdminApp.config(['$httpProvider', 'notificationServiceProvider',
            function($httpProvider, notificationServiceProvider) {
                $httpProvider.interceptors.push('errorListenerService');
                notificationServiceProvider.setNotificationTmpl(bootstrapNotification);
        }]);

        recommendationAdminApp.directive('recommendationControllerWrapper', [function() {
            return {
                restrict: 'AEC',
                template: recommendationCntrTmpl,
                controller: 'RecommendationCntr',
                link: function(scope, element, attrs) {

                }
            }
        }]);

        return recommendationAdminApp;
    });

