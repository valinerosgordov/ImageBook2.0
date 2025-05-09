package ru.imagebook.client.admin2.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.admin2.ctl.BannersPresenter;


public class BannersViewImpl implements BannersView {
	interface XUiBinder extends UiBinder<Widget, BannersViewImpl> {}

	private static XUiBinder uiBinder = GWT.create(XUiBinder.class);
	
	private BannersPresenter presenter;

	@Override
	public Widget asWidget() {
		return uiBinder.createAndBindUi(this);
	}

	@Override
	public void setPresenter(BannersPresenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void show() {
        setUp();
    }

    public static native void setUp()/*-{
        $wnd.require(['/static/web/js/bannerAdmin/main.js'], function(main){
            $wnd.require(['angular', 'bannerApp', 'jquery'], function(angular, bannerApp, $) {

                var $appContainer = $('#bannerApp');
                $appContainer.append('<div banner-controller-wrapper></div>');

                $wnd.angular.element().ready(function () {
                    $wnd.angular.bootstrap($appContainer, [bannerApp.name]);
                });
            });
        });
    }-*/;

}
