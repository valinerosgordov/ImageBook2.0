package ru.imagebook.client.admin2.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.admin2.ctl.ExportEmailPresenter;


public class ExportEmailViewImpl implements ExportEmailView {
	interface XUiBinder extends UiBinder<Widget, ExportEmailViewImpl> {}

	private static XUiBinder uiBinder = GWT.create(XUiBinder.class);
	
	private ExportEmailPresenter presenter;

	@Override
	public Widget asWidget() {
		return uiBinder.createAndBindUi(this);
	}

	@Override
	public void setPresenter(ExportEmailPresenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void show() {
        setUp();
    }

    public static native void setUp()/*-{
        $wnd.require(['/static/web/js/export/exportEmailAdmin/main.js'], function(main){
            $wnd.require(['angular', 'exportEmailApp', 'jquery'], function(angular, exportEmailApp, $) {

                var $appContainer = $('#exportEmailApp');
                $appContainer.append('<div export-email-controller-wrapper></div>');

                $wnd.angular.element().ready(function () {
                    $wnd.angular.bootstrap($appContainer, [exportEmailApp.name]);
                });
            });
        });
    }-*/;

}
