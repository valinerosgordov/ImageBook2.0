define(['angular', 'angularBootstrap', 'text!common/tmpl/confirmModal.html'],
    function(ng, angularBootstrap, confirmModalTmpl) {
    var confirmModal = angular.module('confirmModal', ['ui.bootstrap']);

    confirmModal.factory('confirmModal', ['$modal', function ($modal) {
        return {
            open: function(options){
                var modalInstance = $modal.open({
                    template: confirmModalTmpl,
                    controller: 'ConfirmModalCtrl'
                });
                modalInstance.options = options;
                return modalInstance;
            }
        }
    }]);

    confirmModal.controller('ConfirmModalCtrl',
        ['$scope', '$modalInstance',
            function($scope, $modalInstance) {
                $scope.title = $modalInstance.options.title;
                $scope.message = $modalInstance.options.message;
                $scope.ok = function () {
                    $modalInstance.okClick();
                };

                $scope.cancel = function () {
                    $modalInstance.cancelClick &&  $modalInstance.cancelClick();
                    $modalInstance.dismiss('cancel');
                };
            }
        ]);

    return confirmModal;
});
