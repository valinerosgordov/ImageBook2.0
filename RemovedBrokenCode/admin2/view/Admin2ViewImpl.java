package ru.imagebook.client.admin2.view;

import ru.imagebook.client.admin2.ctl.Admin2Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Admin2ViewImpl implements Admin2View {
	private Admin2Presenter presenter;

	interface XUiBinder extends UiBinder<Widget, Admin2ViewImpl> {
	}

	private static XUiBinder uiBinder = GWT.create(XUiBinder.class);

	@Override
	public void setPresenter(Admin2Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showMenu() {
		RootPanel panel = RootPanel.get("menu");
		panel.add(uiBinder.createAndBindUi(this));
	}

	@UiHandler("codesImportAnchor")
	public void onCodesImportAnchorClicked(ClickEvent event) {
		presenter.onCodesImportAnchorClicked();
	}

    @UiHandler("feedbacksAnchor")
    public void onFeedbacksAnchorClicked(ClickEvent event) {
        presenter.onFeedbacksAnchorClicked();
    }

    @UiHandler("recomendationsAnchor")
    public void onRecomendationAnchorClicked(ClickEvent event) {
        presenter.onRecomendationsAnchorClicked();
    }

	@UiHandler("bannersAnchor")
	public void onBannersAnchorClicked(ClickEvent event) {
		presenter.onBannersAnchorClicked();
	}

    @UiHandler("exportEmailAnchor")
    public void onExportEmailAnchorClicked(ClickEvent event) {
        presenter.onExportEmailAnchorClicked();
    }
}
