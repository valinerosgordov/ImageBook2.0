define(['angular', 'angularResource'], function (ng, angularResource) {

    var commonService = angular.module('commonServices', []);

    commonService.factory("context", ["$resource", "$rootScope",
        function($resource, $rootScope) {

            function createInitResource(initAppUrl) {
            return $resource(initAppUrl, {}, {
                get: {
                    method: 'GET'
                }
            });
        }

        var models;

        return {
            start: function(initAppUrl, success) {
                models = createInitResource(initAppUrl).get(function(models){
                    $rootScope.contextStarted = true;
                    if(success) success(models);
                });
            },
            getModels : function () {
                return models;
            },
            get : function (key) {
                return models[key];
            },
            put : function (key, value) {
                models[key] = value;
            }
        }
    }]);

    commonService.factory('baseResource', ['$resource', function($resource) {
        var baseSettings = {
            _deleteById: {
                method: 'DELETE'
            },
            update: {
                method: 'PUT'
            }
        };

        var baseMethods = {
            deleteById : function(id, success, error) {
                this._deleteById({'id': id}, success, error);
            }
        };

        return {
            extend: function (url, settings) {
                var resultSettings = angular.extend(settings || {}, baseSettings);
                var baseResource = $resource(url, {}, resultSettings);
                angular.extend(baseResource, baseMethods);
                return baseResource;
            }
        }
    }]);

    commonService.provider('notificationService', function() {
        var _notificationTmpl;
        var messages = [];
        var updateListener;
        return {
            setNotificationTmpl: function (notificationTmpl) {
                _notificationTmpl = notificationTmpl;
            },
            $get: function() {
                return {
                    getNotificationTmpl: function () {
                        return _notificationTmpl;
                    },

                    setUpdateListener: function (listener) {
                        updateListener = listener;
                    },
                    /**
                     *
                     * @param type can be: 'danger', 'success'
                     * @param text
                     */
                    addMessage: function (type, text) {
                        var message = {
                            text: text,
                            type: type
                        };
                        messages.push(message);
                        updateListener(messages);
                        setTimeout(function () {
                            messages.splice(messages.indexOf(message), 1);
                            updateListener(messages);
                        }, 4000); //TODO 4000 should be in constants
                    },
                    addDanger: function (message) {
                        this.addMessage('danger', message)
                    },
                    addSuccess: function (message) {
                        this.addMessage('success', message)
                    },
                    getMessages: function () {
                        return messages;
                    },
                    removeMessage: function (message) {
                        messages.splice(messages.indexOf(message), 1);
                    }
                };
            }
        };
    });

    commonService.factory('errorListenerService',
        ['$q', '$injector', 'notificationService',
            function($q, $injector, notificationService) {
                var errorListenerService = {
                    responseError: function(response) {
                        notificationService.addDanger("Извините, возникла ошибка на уровне сервера. " +
                            "Обратитесь к администратору или выполните действие позже.")
                        return $q.reject(response);
                    }
                };
                return errorListenerService;
            }
        ]
    );

    commonService.factory('propertyService', ['$resource', function ($resource) {
        var Property = $resource('/api/property', {}, {
            _get: {
                method: 'GET'
            },
            _update: {
                method: 'PUT'
            }
        });

        Property.get = function (key) {
            return Property._get({key: key});
        };

        Property.update = function (key, value, success, error) {
            return Property._update({}, {
                key: key,
                value: value
            }, success, error);
        };

        return Property;
    }]);

    return commonService;
});