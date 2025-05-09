package ru.imagebook.client.admin2.ctl;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import ru.imagebook.client.admin2.view.RecommendationsView;

public class RecommendationsActivity extends AbstractActivity implements RecommendationsPresenter {
	private final RecommendationsView view;
	private EventBus eventBus;
	private final PlaceController placeController;

	public RecommendationsActivity(PlaceController placeController, RecommendationsView view) {
		this.placeController = placeController;
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		panel.setWidget(view);
        view.show();
	}


}
