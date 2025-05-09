package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.gxt.grid.LoadResult;



import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuestionCategoryServiceAsync {
	
	void deleteQuestionCategories(List<Integer> ids, AsyncCallback<Void> callback);

	void loadQuestionCategories(int offset, int limit, AsyncCallback<LoadResult<QuestionCategory>> callback);
	
	void saveQuestionCategory(QuestionCategory questionCategory, AsyncCallback<Void> callback);	
	
	void addQuestionCategory(QuestionCategory questionCategory, AsyncCallback<Void> callback);
	
	void countQuestionCategories(AsyncCallback<Long> callback);
}
