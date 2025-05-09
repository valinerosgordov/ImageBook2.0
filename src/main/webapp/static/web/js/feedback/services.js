define(['angular', 'text', 'angularResource', 'common/services'],
    function(ng, t, ngResource, commonServices){

    var services = angular.module('feedbackServices', ['ngResource', 'commonServices']);

    services.factory('feedbackTextService', ['$resource', 'propertyService',
        function ($resource, propertyService) {

            var feedbackTextService = {
                get : function () {
                    return propertyService.get('feedback.text');
                },
                update : function (value, success, error) {
                    propertyService.update('feedback.text', value, success, error);
                }
            };

            return feedbackTextService;
    }]);

    services.factory('Feedback', ['$resource', 'baseResource',
        function ($resource, baseResource) {

        var Feedback = baseResource.extend('/api/feedback/:id', {
            _range: {
                url: '/api/feedback/range/:from/:to',
                method: 'GET',
                isArray: true
            },
            saveView: {
                url: '/api/feedback/saveView',
                method: 'POST'
            }
        });

        Feedback.getRange = function(from, to, success, error){
            var params = {
                from: from,
                to: to
            };
            return this._range(params, success, error);
        };

        return Feedback;
    }]);

    services.factory('FeedbackAnswer',
        ['$resource', 'baseResource',
        function ($resource, baseResource) {

        var FeedbackAnswer = baseResource.extend('/api/feedback_answer/:id');

        return FeedbackAnswer;
    }]);


});
