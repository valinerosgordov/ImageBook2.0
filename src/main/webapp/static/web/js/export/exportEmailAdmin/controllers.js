define(['angular', '../services', 'common/directives',
        'underscore', 'common/services', 'common/filters', 'common/confirmModal'],
    function (angular, services, commonDirectives,
        _, commonServices, commonFilters, angularBootstrap, confirmModal) {

        var controllers = angular.module('exportEmailControllers',
            ['exportEmailServices', 'commonDirectives', 'commonServices', 'commonFilters', 'confirmModal']);

        controllers.controller('ExportEmailController',
        ['$scope', 'exportEmailService', 'context', '$modal', '$log', 'notificationService',
            function($scope, exportEmailService, context, $modal, $log, notificationService) {

                    context.start('/admin2/export/exportEmail/initApp', function (models) {
                        $scope.vendors = models.vendorList;
                        $scope.commonUsers = true;
                        $scope.photographers = true;
                        setExportStarted(false);

                        // initial selection
                        for (var i = 0; i < $scope.vendors.length; i++) {
                            if ($scope.vendors[i].id == models.currentVendor.id) {
                                $scope.selectedVendor = $scope.vendors[i];
                                break;
                            }
                        }
                    });

                    $scope.$watch('[commonUsers, photographers]', function () {
                        $scope.userTypeNotSelected = !$scope.commonUsers && !$scope.photographers;
                    }, true);

                    $scope.exportEmails = function () {
                        var params = {
                            vendorId: $scope.selectedVendor.id,
                            commonUsers: $scope.commonUsers,
                            photographers: $scope.photographers
                        };

                        setExportStarted(true);
                        exportEmailService._export(params, function () {
                            setExportStarted(false);
                            notificationService.addSuccess("Выгрузка успешно завершена.");
                        }, function() {
                            setExportStarted(false);
                            notificationService.addDanger("Извините, возникла ошибка на уровне сервера. " +
                                "Обратитесь к администратору или выполните действие позже.");
                        });
                    };

                    function setExportStarted(isExporting) {
                        if (isExporting) {
                            $scope.exporting = true;
                            $scope.buttonText = 'Выгрузка данных ...';
                        } else {
                            $scope.exporting = false;
                            $scope.buttonText = 'Выгрузить';
                        }
                    }
                }
            ]);
});
