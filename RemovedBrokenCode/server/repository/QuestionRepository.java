package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;

public interface QuestionRepository {
	void deleteQuestions(List<Integer> ids);
	
	List<Question> loadQuestions(Integer questionCategoryId, int offset, int limit);
	
	Question getQuestion(int id);
	
	void saveQuestion(Question question);
	
	QuestionCategory getQuestionCategory(Integer questionCategoryId);
	
	long countQuestions(Integer questionCategoryId);
	
	List<QuestionCategory> loadAllQuestionCategories();

	List<Question> getCategoryQuestions(int categoryId, int offset, int limit);
	
	long countCategoryQuestions(int categoryId);
	
	QuestionCategory getQuestionCategory(int id);
}
