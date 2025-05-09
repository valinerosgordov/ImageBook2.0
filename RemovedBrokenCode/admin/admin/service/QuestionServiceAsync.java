package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.gxt.grid.LoadResult;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuestionServiceAsync {
	
	void deleteQuestions(List<Integer> ids, AsyncCallback<Void> callback);
	
	void loadQuestions(Integer questionCategoryId, int offset, int limit, AsyncCallback<LoadResult<Question>> callback);
	
	void saveQuestion(Integer questionCategoryId, Question question, AsyncCallback<Void> callback);	
	
	void addQuestion(Integer questionCategoryId, Question question, AsyncCallback<Void> callback); 

	void getAllCategories(AsyncCallback<List<QuestionCategory>> callback);
}
