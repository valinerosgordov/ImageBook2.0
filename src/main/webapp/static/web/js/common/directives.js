define(['angular', 'common/services',
        'text!common/tmpl/notification.html',
        'text!common/tmpl/vkComments.html'],
    function(ng, commonServices,
             notificationTmpl,
             vkCommentsTmpl){
    var directives = angular.module('commonDirectives', ['commonServices']);

    directives.directive('ckEditor', ['$log', function($log){
        return {
            require: '?ngModel',
            link: function ($scope, elm, attr, ngModel) {
                $log.debug('creating ckEditor');

                CKEDITOR.config.toolbar = [
                    ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
                    ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat','-','Templates','-','Source','-','About'],
                    '/',
                    ['Bold','Italic','Underline','Strike'],
                    ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
                    ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
                    ['TextColor','BGColor'], ['Subscript','Superscript'], ['BidiLtr', 'BidiRtl' ],
                    '/',
                    ['Format','Font','FontSize'],
                    ['Link','Unlink','Anchor'],
                    ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe'],
                    ['Maximize', 'ShowBlocks']
                ] ;

                var ck = CKEDITOR.replace(elm[0], {});

                ck.on('pasteState', function () {
                    $scope.$apply(function () {
                        ngModel.$setViewValue(ck.getData());
                    });
                });

                ngModel.$render = function (value) {
                    ck.setData(ngModel.$modelValue);
                };

                $scope.$on('$destroy', function() {
                    ck.destroy();
                });
            }
        };
    }]);

        directives.directive('iNotification', ['notificationService',
            function (notificationService) {
                return {
                    restrict: 'E',
                    template: notificationService.getNotificationTmpl(),
                    scope: {},
                    link: function (scope, elem, attrs) {
                        notificationService.setUpdateListener(function (messages) {
                            setTimeout(function(){
                                scope.$apply(function () {
                                    scope.messages = messages;
                                });
                            });
                        });
                        scope.close = function (message) {
                            notificationService.removeMessage(message);
                        }
                    }
                }
            }]);

        directives.directive('iUnsafe', function() {
            return {
                restrict: 'A',
                scope: {
                    iUnsafe: '='
                },
                link: function(scope, elem, attr) {
                    scope.$watch('iUnsafe', function(){
                        elem.html(scope.iUnsafe);
                    });
                }
            };
        });

        directives.directive('number', function() {
            return {
                restrict: 'A',
                require: 'ngModel',
                link: function(scope, elem, attr, ctrl) {
                    var pattern = /^[\+]?[0-9]+$/;
                    ctrl.$parsers.unshift(function(value) {
                        if(!value) {
                            ctrl.$setValidity('number', true);
                            return '';
                        }
                        var valid = pattern.test(value);
                        ctrl.$setValidity('number', valid);
                        return valid ? value : undefined;
                    });
                }
            };
        });

        directives.directive('iVkComments', ['$log', function($log){
            return {
                template: vkCommentsTmpl,
                restrict: 'E',
                scope: {
                    apiId: "=",
                    limit: "=",
                    width: "="
                },
                link: function ($scope, elm, attr) {
                    var initialized = false;
                    $scope.$watch('apiId', function () {
                        if(initialized || !$scope.apiId) return;
                        if(VK) {
                            VK.init({apiId: $scope.apiId, onlyWidgets: true});
                            VK.Widgets.Comments("vk_comments", {
                                limit: $scope.limit,
                                width: $scope.width,
                                attach: "*"
                            });
                            initialized = true;
                        } else {
                            $log.error("VK comments widget wasn't be loaded");
                        }
                    });


                }
            };
        }]);

        directives.directive('iFbComments', [function(){
            return {
                restrict: 'E',
                scope: {
                    href: '=',
                    width: '=',
                    numposts: '='
                },
                link: function ($scope, elm, attr) {
                    elm.append('<div id="fb-root"></div>');
                    elm.append('<div class="fb-comments" ' +
                        'data-href="'+$scope.href+'" ' +
                        'data-width="'+$scope.width+'" ' +
                        'data-numposts="'+$scope.numposts+'" ' +
                        'data-colorscheme="light"> </div>');

                    (function(d, s, id) {
                        var js, fjs = d.getElementsByTagName(s)[0];
                        if (d.getElementById(id)) return;
                        js = d.createElement(s); js.id = id;
                        js.src = "//connect.facebook.net/ru_RU/sdk.js#xfbml=1&version=v2.0";
                        fjs.parentNode.insertBefore(js, fjs);
                    }(document, 'script', 'facebook-jssdk'));
                }
            };
        }]);

        directives.directive('iValidFile', function () {
            return {
                require: 'ngModel',
                link: function (scope, el, attrs, ngModel) {
                    el.bind('change', function () {
                        scope.$apply(function () {
                            ngModel.$setViewValue(el.val());
                            ngModel.$render();
                        });
                    });
                    el.bind('click', function () {
                        scope.$apply(function () {
                            ngModel.$setViewValue();
                            ngModel.$render();
                        });
                    });
                }
            }
        });

});