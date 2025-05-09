define(['angular', 'services', 'common/services', 'underscore', 'common/confirmModal',
        'angularBootstrap', 'common/directives'],
    function (angular, services, commonServices, _, confirmModal,
              angularBootstrap, commonDirectives) {

        var controllers = angular.module('recommendationControllers',
            ['recommendationServices', 'commonServices', 'confirmModal',
                'ui.bootstrap', 'commonDirectives']);

    controllers.controller('RecommendationCntr',
        ['$scope', 'context', 'Recommendation', '$filter', 'notificationService',
            function($scope, context, Recommendation, $filter, notificationService) {

            context.start('/admin2/recommendation/initApp', function (models) {
                $scope.recommendations = models.recommendations;
            });

            $scope.create = function(recommendation) {
                recommendation.date = $filter('date')(recommendation.date, 'dd/MM/yyyy');
                $scope.saving = true;
                Recommendation.upload(recommendation, function (recommendationFromServer) {
                    $scope.recommendations.push(recommendationFromServer);
                    $scope.creating = false;
                    delete $scope.recommendation;
                    $scope.saving = false;
                    notificationService.addSuccess("Рекомендация успешно сохранена.");
                }, function() {
                    $scope.saving = false;
                });
            };

            $scope.cancel = function () {
                $scope.creating = false;
                delete $scope.recommendation;
            };

            $scope.fileSelect = function (recommendation, files) {
                recommendation.file = files[0];
            };

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened = true;
            };

        }]);

        controllers.controller('RecommendationItemCntr',
            ['$scope', 'context', 'Recommendation', 'confirmModal', '$filter',
                '$modal', 'notificationService',
                function($scope, context, Recommendation, confirmModal, $filter,
                         $modal, notificationService) {

                    $scope.edit = function (recommendation) {
                        $scope.updating = true;
                        $scope.updatedRecommendation = angular.extend({}, recommendation, {});
                    };

                    $scope.cancel = function() {
                        $scope.updating = false;
                        angular.extend($scope.recommendation, $scope.oldRecommendation);
                        delete $scope.oldRecommendation;
                    };

                    $scope.view = function (recommendation) {
                        var modalInstance = $modal.open({
                            templateUrl: 'viewRecommendationTemplate',
                            controller: 'ViewRecommendationCtrl',
                            backdrop: 'static',
                            resolve: {
                                recommendations: function () {
                                    return $scope.recommendations;
                                },
                                recommendation: function () {
                                    return recommendation;
                                }
                            }
                        });
                    };

                    $scope.save = function(updatedRecommendation, recommendation){
                        $scope.saving = true;
                        Recommendation.update(updatedRecommendation, function(recommendationFromServer) {
                            //TODO | need to refactoring. neet to do without omit()
                            angular.extend(recommendation, _.omit(recommendationFromServer, '$promise', '$resolved'));
                            $scope.updating = false;
                            $scope.saving = false;
                            notificationService.addSuccess("Рекомендация успешно сохранена.");
                        }, function() {
                            $scope.cancel();
                            $scope.saving = false;
                        });
                    };

                    $scope.remove = function(recommendation) {
                        var modalInstance = confirmModal.open({
                            message: 'Вы уверены, что вы хотите удалить рекомендацию?'
                        });
                        modalInstance.okClick = function () {
                            Recommendation.deleteById(recommendation.id, function () {
                                $scope.$parent.$parent.recommendations = _.without($scope.recommendations, recommendation);
                                modalInstance.close();
                                notificationService.addSuccess("Рекомендация успешно удалена.");
                            }, function () {
                                modalInstance.dismiss('cancel');
                            });
                        };
                    };

                    $scope.open = function($event) {
                        $event.preventDefault();
                        $event.stopPropagation();

                        $scope.opened = true;
                    };
            }]);

        controllers.controller('ViewRecommendationCtrl', ['$scope', '$modalInstance', 'recommendations', 'recommendation',
            function($scope, $modalInstance, recommendations, recommendation) {
                recommendation.active = true;
                $scope.recommendations = recommendations;
                $scope.close = function () {
                    $modalInstance.dismiss('cancel');
                    delete recommendation.active;
                };
        }]);

});