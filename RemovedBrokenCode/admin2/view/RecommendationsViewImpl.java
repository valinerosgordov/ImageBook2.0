package ru.imagebook.client.admin2.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;
import ru.imagebook.client.admin2.ctl.RecommendationsPresenter;

public class RecommendationsViewImpl implements RecommendationsView {
	interface XUiBinder extends UiBinder<Widget, RecommendationsViewImpl> {}

	private static XUiBinder uiBinder = GWT.create(XUiBinder.class);
	
	private RecommendationsPresenter presenter;

	@Override
	public Widget asWidget() {
		return uiBinder.createAndBindUi(this);
	}

	@Override
	public void setPresenter(RecommendationsPresenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void show() {
        setUp();
    }

    public static native void setUp()/*-{
        $wnd.require(['/static/web/js/recommendationAdmin/main.js'], function(){
            $wnd.require(['angular', 'recommendationApp', 'jquery'], function(angular, recommendationApp, $) {

                var $appContainer = $('#recommendationApp');
                $appContainer.append('<div recommendation-controller-wrapper></div>');

                $wnd.angular.element().ready(function () {
                    $wnd.angular.bootstrap($appContainer, [recommendationApp.name]);
                });
            });
        });
    }-*/;

}
