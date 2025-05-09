define(['angular'],
    function (angular) {

        var feedbackDirectives = angular.module('feedbackDirectives', []);

        feedbackDirectives.directive('iLoader', ['$log', function($log){
            return {
                restrict: 'E',
                link: function(scope, elem, attr, ctrl) {
                    $(window).scroll(function(){
                        if(!scope.feedbacks) return;
                        var scroll = $(this).scrollTop() + $(window).height();
                        if(elem.position().top < scroll) {
                            $log.info('invoke load');
                            scope.load();
                        }
                    });
                }
            }
        }]);

        feedbackDirectives.directive('expression', function() {
            return {
                restrict: 'A',
                require: 'ngModel',
                link: function(scope, elem, attr, ctrl) {
                    ctrl.$parsers.unshift(function(value) {
                        if(!value) {
                            ctrl.$setValidity('expression', true);
                            return value;
                        }
                        var expr = scope.expression;
                        var valid = value == expr.first + expr.second;
                        ctrl.$setValidity('expression', valid);

                        return valid ? value : undefined;
                    });
                }
            };
        });

        feedbackDirectives.directive('iLast', function() {
            return {
                scope: {
                    callback: '&'
                },
                link: function(scope, element, attrs) {
                    if (scope.$last){
                        scope.callback();
                    }
                }
            }
        });

        feedbackDirectives.directive('iViewSlider', function() {
            return function(scope, element, attrs) {
                if (scope.$last){
                    element.parent().bxSlider({
                        pager: false,
                        speed: 3000,
                        startSlide: scope.currentRecommendation
                    });
                }
            };
        });

        return feedbackDirectives;
    });