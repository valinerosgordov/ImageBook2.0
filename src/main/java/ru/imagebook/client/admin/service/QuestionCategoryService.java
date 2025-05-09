package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.gxt.grid.LoadResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("questions.remoteService")
public interface QuestionCategoryService extends RemoteService {
	
	LoadResult<QuestionCategory> loadQuestionCategories(int offset, int limit);
	
	void deleteQuestionCategories(List<Integer> ids);
	
	void saveQuestionCategory(QuestionCategory questionCategory);
	
	void addQuestionCategory(QuestionCategory questionCategory); 
	
	Long countQuestionCategories();
}
