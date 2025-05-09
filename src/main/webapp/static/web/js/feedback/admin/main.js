require.config({
    paths: {
        angular: '/static/angular/angular',
        angularResource: '/static/angular/angular-resource',
        angularRoute: '/static/angular/angular-route',
        angularSanitize: '/static/angular/angular-sanitize',
        angularBootstrap: '/static/angular/ui-bootstrap',
        jquery: '/static/jquery/jquery-1.11.0.min',
        jqueryBxslider: '/static/jquery/jquery.bxslider',
        text: '/static/requirejs/text',
        css: '/static/requirejs/css',
        common: '/static/web/js/common',
        underscore: '/static/underscore/underscore',
        ckeditor: '/static/ckeditor/ckeditor',
        cssFolder: '/static/web/css'
    },
    baseUrl: '/static/web/js/feedback/admin',
    shim: {
        angular: {'exports': 'angular'},
        angularResource: {
            deps: ['angular']
        },
        angularRoute: {
            deps: ['angular']
        },
        angularSanitize: {
            deps: ['angular']
        },
        angularBootstrap: {
            deps: ['angular']
        }
    },
    priority: [
        "angular"
    ]
});

