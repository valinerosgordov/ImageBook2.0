package ru.imagebook.server.service;

import java.util.List;

import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.gxt.grid.LoadResult;


import com.google.gwt.user.client.rpc.RemoteService;

public interface QuestionService {
	
	int QUESTIONS_ON_PAGE = 10;
	
	LoadResult<Question> loadQuestions(Integer questionCategoryId, int offset, int limit);
	
	void deleteQuestions(List<Integer> ids);
	
	void saveQuestion(Integer Question, Question question);
	
	void addQuestion(Integer Question, Question question);
	
	List<QuestionCategory> loadAllQuestionCategories();
	
	List<Question> getCategoryQuestions(int categoryId, int page);
	
	int countCategoryQuestions(int categoryId);
	
	QuestionCategory getQuestionCategory(int id);

	List<QuestionCategory> getAllCategories();
	
}
