package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.gxt.grid.LoadResult;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("question.remoteService")
public interface QuestionService extends RemoteService {
	
	LoadResult<Question> loadQuestions(Integer questionCategoryId, int offset, int limit);
	
	void deleteQuestions(List<Integer> ids);
	
	void saveQuestion(Integer Question, Question question);
	
	void addQuestion(Integer Question, Question question);
	
	List<QuestionCategory> getAllCategories();
	
}
