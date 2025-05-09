define(['angular', '../services', 'common/directives',
        'underscore', 'common/services', 'common/filters', 'common/confirmModal'],
    function (angular, services, commonDirectives,
        _, commonServices, commonFilters, angularBootstrap, confirmModal) {

        var controllers = angular.module('feedbackControllers',
            ['feedbackServices', 'commonDirectives', 'commonServices',
                'commonFilters', 'confirmModal']);

    controllers.controller('FeedbackAdminController',
        ['$scope', '$window', 'feedbackTextService',
            'Feedback', 'FeedbackAnswer', 'context', '$modal', '$log', 'notificationService',
            function($scope, $window, feedbackTextService,
                     Feedback, FeedbackAnswer, context, $modal, $log, notificationService) {

                var self = this;

                this.pages = [];

                context.start('/admin2/feedback/initApp', function(models) {
                    $scope.feedbacks = models.feedbacks;
                    self.pages[1] = models.feedbacks;
                    $scope.feedbackText = models.feedbackText;
                    $scope.config = models.clientConfig;
                });

                $scope.currentPage = 1;
                $scope.$watch('currentPage', function () {
                    if(!$scope.contextStarted) return;
                    $scope.errorPageLoading = false;
                    var pageFromCache = self.pages[$scope.currentPage];
                    if(pageFromCache) {
                        $scope.feedbacks = pageFromCache;
                    } else {
                        var current = $scope.currentPage;
                        var pageSize = $scope.config.pageSize;
                        var from = (current - 1) * pageSize;
                        var to = current * pageSize;
                        Feedback.getRange(from, to, function (recommendations) {
                            self.pages[current] = recommendations;
                            $scope.feedbacks = recommendations;
                        }, function () {
                            $scope.errorPageLoading = true;
                            $scope.feedbacks = [];
                        });
                    }
                    $window.scrollTo(0, 0);
                });

                $scope.edit = function() {
                    $scope.updatedFeedbackText = context.get('feedbackText');
                };

                $scope.cancel = function() {
                    $scope.editingText = false;
                };

                $scope.saveText = function(updatedFeedbackText) {
                    feedbackTextService.update(updatedFeedbackText, function (data) {
                        $scope.feedbackText = updatedFeedbackText;
                        context.put('feedbackText', updatedFeedbackText);
                        $scope.editingText = false;
                        notificationService.addSuccess("Текст успешно обновлен.");
                    });
                };

    }]);

        controllers.controller('FeedbackItemAdminController',
            ['$scope', 'feedbackTextService',
            'Feedback', 'FeedbackAnswer', 'context', '$modal', '$log', 'confirmModal',
                'notificationService',
                function($scope, feedbackTextService,
                        Feedback, FeedbackAnswer, context, $modal, $log, confirmModal,
                        notificationService) {

                    $scope.deleteFeedback = function (feedback) {
                        var modalInstance = confirmModal.open({
                            message: 'Вы уверены, что вы хотите удалить отзыв?'
                        });
                        modalInstance.okClick = function () {
                            Feedback.deleteById(feedback.id, function(){
                                $scope.$parent.$parent.feedbacks = _.without($scope.feedbacks, feedback);
                                modalInstance.close();
                                notificationService.addSuccess("Отзыв успешно удален.");
                            }, function() {
                                modalInstance.dismiss('cancel');
                            });
                        };
                    };

                    $scope.deleteFeedbackAnswer = function(feedbackAnswer, feedback) {
                        var modalInstance = confirmModal.open({
                            message: 'Вы уверены, что вы хотите удалить ответ на отзыв?'
                        });
                        modalInstance.okClick = function () {
                            FeedbackAnswer.deleteById(feedbackAnswer.feedbackId, function() {
                                delete feedback.feedbackAnswer;
                                modalInstance.close();
                                notificationService.addSuccess("Ответ успешно удален.");
                            }, function (){
                                modalInstance.close();
                            });
                        };
                    };

                    $scope.saveFeedbackAnswer = function(feedback, answerMessage, answeringOnFeedback) {
                        var answer = {
                            answer: answerMessage,
                            feedback : {
                                id : feedback.id
                            }
                        };
                        answeringOnFeedback = false;
                        FeedbackAnswer.save(answer, function(answerFromServer) {
                            //TODO | need to refactoring. neet to do without omit()
                            feedback.feedbackAnswer = _.omit(answerFromServer, '$promise', '$resolved');
                            $scope.answeringOnFeedback = false;
                            notificationService.addSuccess("Ответ успешно сохранен.");
                        });
                    };

                    $scope.editFeedbackAnswer = function(feedbackAnswer) {
                        $scope.editingFeedbackAnswer = true;
                        $scope.updatedFeedbackAnswer = feedbackAnswer.answer;
                    };

                    $scope.cancelEditFeedbackAnswer = function() {
                        $scope.editingFeedbackAnswer = false;
                    };

                    $scope.updateFeedbackAnswer = function(updatedFeedbackAnswer, feedbackAnswer) {
                        feedbackAnswer.answer = updatedFeedbackAnswer;
                        var feedbackAnswerBean = new FeedbackAnswer(feedbackAnswer);
                        feedbackAnswerBean.$update().then(function() {
                            $scope.editingFeedbackAnswer = false;
                            notificationService.addSuccess("Ответ успешно обновлен.");
                        }, function() {
                            $scope.editingFeedbackAnswer = false;
                        });
                    }
        }]);

});
