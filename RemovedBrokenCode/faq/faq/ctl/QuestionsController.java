package ru.imagebook.client.faq.ctl;

import ru.minogin.core.client.app.ApplicationController;
import ru.minogin.core.client.gwt.ClientParametersReader;
import ru.minogin.core.client.gwt.OneWidgetPanel;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class QuestionsController extends ApplicationController{
	@Inject
	private ClientParametersReader parametersReader;
	@Inject
	private QuestionsActivity questionsActivity;
	@Inject
	private PlaceController placeController;

	@Override
	protected void mapActivities() {
		map(new QuestionsPlace(), questionsActivity);
	}

	@Override
	protected AcceptsOneWidget createDisplay() {
		if (parametersReader.hasParam("question"))
			return new OneWidgetPanel("questionForm");
		else
			throw new RuntimeException();
	}

	@Override
	protected void start() {
		if (parametersReader.hasParam("question")) {
			getPlaceController().goTo(new QuestionsPlace());
		}
		else
			throw new RuntimeException();
	}
}
