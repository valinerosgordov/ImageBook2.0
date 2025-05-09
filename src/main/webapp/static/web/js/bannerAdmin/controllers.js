define(['angular', 'services', 'common/directives',
        'underscore', 'common/services', 'angularBootstrap'],
    function (angular, services, commonDirectives, _, commonServices, angularBootstrap) {

        var controllers = angular.module('bannerControllers',
            ['bannerServices', 'commonDirectives', 'commonServices']);

        controllers.controller('BannerController', ['$scope', 'context', 'bannerTextService', 'notificationService',
            function($scope, context, bannerTextService, notificationService) {

            context.start('/admin2/banner/initApp', function (models) {
                $scope.appBannerText = models.appBannerText;
                $scope.appPaymentDeliveryBannerText=models.appPaymentDeliveryBannerText;
                $scope.editorBannerText = models.editorBannerText;
            });

            $scope.updateBanners = function(appBannerText, appPaymentDeliveryBannerText, editorBannerText) {
                bannerTextService.updateAppBannerText(appBannerText, function () {
                    notificationService.addSuccess("Основной баннер для Личного кабинета успешно сохранен.");
                });
                bannerTextService.updateAppPaymentDeliveryBannerText(appPaymentDeliveryBannerText, function () {
                    notificationService.addSuccess("Баннер доставки для Личного кабинета успешно сохранен.");
                });
                bannerTextService.updateEditorBannerText(editorBannerText, function() {
                    notificationService.addSuccess("Баннер для Онлайн-сборщика успешно сохранен.");
                });
            }
        }]);
});