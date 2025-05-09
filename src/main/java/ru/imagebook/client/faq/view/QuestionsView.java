package ru.imagebook.client.faq.view;

import ru.imagebook.client.faq.ctl.QuestionPresenter;
import ru.imagebook.shared.model.Question;

import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionsView extends IsWidget {
	
	void setPresenter(QuestionPresenter presenter);
	
	void informAskResult();
	
	void alertTechFailure();
	
	void fetch(Question question);
}
