define(['angular', 'text', 'angularResource', 'common/services', 'angularFileUpload'],
    function(ng, t, ngResource, commonServices, angularFileUpload){

    var services = angular.module('recommendationServices',
        ['ngResource', 'commonServices', 'angularFileUpload']);

    services.factory('Recommendation',
        ['$resource', 'baseResource', '$upload', '$log',
        function($resource, baseResource, $upload, $log) {

        var Recommendation = baseResource.extend('/api/recommendation/:id', {}, {});

        Recommendation.upload = function(recommendation, success, error){
            $upload.upload({
                url: '/api/recommendation/save',
                method: 'POST',
                data: recommendation,
                file: recommendation.file
            }).then(function (response) {
                success && success(response.data);
            }, error);
        };

        return Recommendation;
    }]);

});
