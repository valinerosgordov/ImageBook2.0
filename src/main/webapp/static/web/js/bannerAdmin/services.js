define(['angular', 'text', 'angularResource', 'common/services'],
    function(ng, t, ngResource, commonServices){

    var services = angular.module('bannerServices', ['ngResource', 'commonServices']);

    services.factory('bannerTextService', ['$resource', 'propertyService',
        function($resource, propertyService) {

        var bannerTextService = {
            updateAppBannerText : function(value, success, error) {
                propertyService.update('banner.app.text', value, success, error);
            },
            updateAppPaymentDeliveryBannerText : function(value, success, error) {
                propertyService.update('banner.app.payment.delivery.text', value, success, error);
            },
            updateEditorBannerText : function(value, success, error) {
                propertyService.update('banner.editor.text', value, success, error);
            }
        };

        return bannerTextService;
    }]);
});
