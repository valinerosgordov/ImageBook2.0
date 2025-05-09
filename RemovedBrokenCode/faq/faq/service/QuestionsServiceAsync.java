package ru.imagebook.client.faq.service;



import ru.imagebook.shared.model.Question;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuestionsServiceAsync {
	void askQuestion(Question question, Integer questionCategoryId, AsyncCallback<Void> callback);
}
