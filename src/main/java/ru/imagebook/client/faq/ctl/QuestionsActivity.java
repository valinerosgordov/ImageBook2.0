package ru.imagebook.client.faq.ctl;

import ru.imagebook.client.faq.service.QuestionsServiceAsync;
import ru.imagebook.client.faq.view.QuestionsView;
import ru.imagebook.shared.model.Question;
import ru.minogin.core.client.app.failure.XAsyncCallback;
import ru.minogin.core.client.gwt.ClientParametersReader;
import ru.minogin.core.client.push.PushMessage;
import ru.minogin.core.client.push.mvp.PushEvent;
import ru.minogin.core.client.push.mvp.PushEventHandler;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class QuestionsActivity extends AbstractActivity implements
		PushEventHandler, QuestionPresenter {
	@Inject
	private ClientParametersReader parametersReader;
	@Inject
	private QuestionsServiceAsync service;

	private final QuestionsView view;

	@Inject
	public QuestionsActivity(QuestionsView view) {
		view.setPresenter(this);
		this.view = view;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		eventBus.addHandler(PushEvent.TYPE, this);
	}

	@Override
	public void addQuestionButtonClicked() {
		Integer questionCategoryId = parametersReader
				.getParam("questionCategoryId").toString().matches("[0-9]{1,8}") ? Integer
				.valueOf(parametersReader.getParam("questionCategoryId").toString())
				: null;

		Question question = new Question();
		view.fetch(question);
		service.askQuestion(question, questionCategoryId,
				new XAsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						view.informAskResult();
					}

					@Override
					public void onFailure(Throwable caught) {
						view.alertTechFailure();
					}
				});
	}

	@Override
	public void onPush(PushMessage message) {
		// TODO Auto-generated method stub
	}

}
