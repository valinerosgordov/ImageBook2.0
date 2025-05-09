package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.QuestionCategory;

public interface QuestionCategoryRepository {
	
	void deleteQuestionCategories(List<Integer> ids);
	
	List<QuestionCategory> loadQuestionCategories(int offset, int limit);
	
	List<QuestionCategory> loadAllQuestionCategories();
	
	QuestionCategory getQuestionCategory(int id);
	
	void saveQuestionCategory(QuestionCategory questionCategory);	
	
	long countQuestionCategories();
}
