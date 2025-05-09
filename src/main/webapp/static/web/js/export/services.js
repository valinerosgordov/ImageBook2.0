define(['angular', 'text', 'angularResource', 'common/services', 'jqueryFileDownload'],
    function(ng, t, ngResource, commonServices, jqueryFileDownload){

    var services = angular.module('exportEmailServices', ['ngResource', 'commonServices']);

    services.factory('exportEmailService', function () {
        return {
            _export : function(params, success, error) {
                $.fileDownload('/api/export/exportEmail', {
                    httpMethod : "POST",
                    data : params
                }).done(function() {
                    success && success();
                }).fail(function(){
                    error && error();
                });
            }
        };
    });
});
