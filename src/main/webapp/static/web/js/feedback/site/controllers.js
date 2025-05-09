define(['angular', '../services', 'common/services', 'common/directives', '../feedbackDirectives',
        'underscore', 'angularCarousel', 'angularTouch'],
    function (angular, services, commonServices, commonDirectives, feedbackDirectives,
              _, angularCarousel, angularTouch) {

        var controllers = angular.module('controllers',
            ['feedbackServices', 'commonServices', 'commonDirectives', 'feedbackDirectives',
                'angular-carousel']);


        controllers.controller('feedbackController',
            ['$scope', 'Feedback', 'context', 'notificationService', '$http',
                function($scope, Feedback, context, notificationService, $http) {
                    var self = this;

                    context.start('/site/feedback/initApp', function(models) {
                        $scope.feedbacks = models.feedbacks;
                        $scope.config = models.clientConfig;
                        $scope.feedbackText = models.feedbackText;
                        $scope.recommendations = models.recommendations;
                        $scope.feedbackUser = models.feedbackUser;
                    });

                    $scope.create = function () {
                        $scope.creating = true;
                        $scope.updateExpression();
                        $scope.newFeedback = {
                            feedbackUser : _.omit($scope.feedbackUser, 'name', 'email', 'phone', 'internalUserId'),
                        };
                        angular.extend($scope.newFeedback,
                            _.pick($scope.feedbackUser, 'name', 'email', 'phone', 'internalUserId'));
                    };

                    $scope.updateExpression = function () {
                        $scope.check = '';
                        $scope.expression = {
                            first: self._getRandom(),
                            second: self._getRandom()
                        };
                    };

                    this._getRandom = function () {
                        return Math.floor((Math.random() * 10) + 1);
                    };


                    $scope.cancel = function () {
                        delete $scope.newFeedback;
                        $scope.creating = false;
                    };

                    $scope.save = function(newFeedback) {
                        $scope.saving = true;
                        var feedback = new Feedback(newFeedback);
                        feedback.$saveView().then(function (newFeedbackFromServer) {
                            $scope.feedbacks.push(newFeedbackFromServer);
                            if($scope.feedbackUser) {
                                $scope.feedbackUser = newFeedbackFromServer.feedbackUser;
                            }
                            $scope.saving = false;
                            $scope.cancel();
                            notificationService.addSuccess("Спасибо за ваш отзыв! Ваш отзыв опубликован");
                        }, function () {
                            $scope.saving = false;
                            $scope.cancel();
                        });

                    };


                    $scope.load = function () {
                        if($scope.nothingLoad) return;
                        $scope.loadingFeedback = true;
                        var from = $scope.feedbacks.length;
                        var to = from + context.get('clientConfig').pageSize;
                        Feedback.getRange(from, to, function (feedbacks) {
                            if(!feedbacks.length) $scope.nothingLoad = true;
                            Array.prototype.push.apply($scope.feedbacks, feedbacks);
                            $scope.loadingFeedback = false;
                        }, function () {
                            $scope.loadingFeedback = false;
                        });
                    };

                    $scope.openRecommendationView = function (index) {
                        $scope.recommendationView = true;
                        $scope.currentRecommendation = index
                    };

                    $scope.closeRecommendationView = function ($event) {
                        if($($event.target).is('img')) return;
                        $scope.recommendationView = false;
                    };

        }]);

});
