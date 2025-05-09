/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function(config) {
	config.contentsCss = ['/static/web/css/main.css', '/static/web/css/styles.css'];
	config.bodyClass = 'content editor';
	config.entities = false;

	config.toolbar_SiteEditor =
		[
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
		];
};
