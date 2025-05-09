require.config({
    paths: {
        angular: '/static/angular/angular',
        angularResource: '/static/angular/angular-resource',
        angularRoute: '/static/angular/angular-route',
        angularBootstrap: '/static/angular/ui-bootstrap',
        jquery: '/static/jquery/jquery-1.11.0.min',
        jqueryFileDownload: '/static/jquery/jquery.fileDownload',
        text: '/static/requirejs/text',
        css: '/static/requirejs/css',
        common: '/static/web/js/common',
        underscore: '/static/underscore/underscore',
        cssFolder: '/static/web/css'
    },
    baseUrl: '/static/web/js/export/exportEmailAdmin',
    shim: {
        angular: {'exports': 'angular'},
        angularResource: {
            deps: ['angular']
        },
        angularRoute: {
            deps: ['angular']
        },
        angularBootstrap: {
            deps: ['angular']
        },
        jqueryFileDownload: {
            deps: ['jquery']
        }
    },
    priority: [
        "angular"
    ]
});

