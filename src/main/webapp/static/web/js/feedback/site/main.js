require.config({
    paths: {
        angular: '/static/angular/angular',
        angularResource: '/static/angular/angular-resource',
        angularRoute: '/static/angular/angular-route',
        angularCarousel: '/static/angular/angular-carousel',
        angularTouch: '/static/angular/angular-touch',
        jquery: '/static/jquery/jquery-1.11.0.min',
        text: '/static/requirejs/text',
        css: '/static/requirejs/css',
        common: '/static/web/js/common',
        underscore: '/static/underscore/underscore'
    },
    baseUrl: '/static/web/js/feedback/site',
    shim: {
        angular: {'exports': 'angular'},
        angularResource: {
            deps: ['angular']
        },
        angularRoute: {
            deps: ['angular']
        },
        angularCarousel: {
            deps: ['angular']
        },
        angularTouch: {
            deps: ['angular']
        }
    },
    priority: [
        "angular"
    ]
});
