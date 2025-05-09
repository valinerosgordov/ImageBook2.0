define(['angular'], function(ng){
    var filters = angular.module('commonFilters', []);

    filters.filter('phone', function() {
        return function(phone) {
            var first = phone.substring(0, phone.length-10);
            var main = phone.substring(phone.length-7, phone.length);
            var code = phone.substring(phone.length-10, phone.length-7);
            return first + ' ' + code + ' ' + main;
        }
    });

    return filters;
});