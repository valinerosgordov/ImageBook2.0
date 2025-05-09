package ru.imagebook.client.faq.service;



import ru.imagebook.shared.model.Question;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("questions.remoteService")
public interface QuestionsService extends RemoteService {
	void askQuestion(Question question, Integer questionCategoryId);

}
