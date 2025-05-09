package ru.imagebook.client.admin2.ctl;

import ru.imagebook.client.admin2.view.Admin2View;
import ru.minogin.undo.client.ctl.UndoController;
import ru.minogin.undo.client.view.UndoViewImpl;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;

public class Admin2Controller implements Admin2Presenter {
	private Admin2View view;
	private PlaceController placeController;

	public Admin2Controller(Admin2View view) {
		this.view = view;
		view.setPresenter(this);
	}

	public void start() {
		EventBus eventBus = new SimpleEventBus();

		UndoController undoController = new UndoController(new UndoViewImpl(), eventBus);
		undoController.start();

		placeController = new PlaceController(eventBus);

		Admin2ActivityMapper activityMapper = new Admin2ActivityMapper(placeController);

		ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
		SimplePanel contentPanel = new SimplePanel();
		RootPanel.get("content").add(contentPanel);
		view.showMenu();
		activityManager.setDisplay(contentPanel);

		Admin2PlaceHistoryMapper historyMapper = GWT.create(Admin2PlaceHistoryMapper.class);
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, new CodesImportPlace());

		historyHandler.handleCurrentHistory();
	}

	@Override
	public void onCodesImportAnchorClicked() {
		placeController.goTo(new CodesImportPlace());
	}

    @Override
    public void onFeedbacksAnchorClicked() {
        placeController.goTo(new FeedbacksPlace());
    }

    @Override
    public void onRecomendationsAnchorClicked() {
        placeController.goTo(new RecommendationsPlace());
    }

	@Override
	public void onBannersAnchorClicked() {
		placeController.goTo(new BannersPlace());
	}

    @Override
    public void onExportEmailAnchorClicked() {
        placeController.goTo(new ExportEmailPlace());
    }
}