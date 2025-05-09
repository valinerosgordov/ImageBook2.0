package ru.imagebook.client.faq;

import ru.imagebook.client.faq.ctl.QuestionsController;
import ru.imagebook.client.faq.view.QuestionsView;
import ru.imagebook.client.faq.view.QuestionsViewImpl;
import ru.minogin.core.client.app.ApplicationController;
import ru.minogin.core.client.app.ApplicationModule;

public class FaqModule extends ApplicationModule {
	@Override
	protected void configure() {
		super.configure();
		
		bind(ApplicationController.class).to(QuestionsController.class);
		bind(QuestionsView.class).to(QuestionsViewImpl.class);
	}
}
