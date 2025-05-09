package ru.imagebook.client.admin.view.questions;

import ru.imagebook.shared.model.QuestionCategory;

public interface QuestionCategoryPresenter {
	void addButtonClicked();

	void saveButtonClickedOnAddForm();

	void editButtonClicked();

	void deleteButtonClicked();

	void saveButtonClickedOnEditForm();

	void deleteConfirmed();

	void loadQuestionCategories(int offset, int limit);
	
	void questionCategoryClicked();
	
	QuestionsPresenter getQuestionsPresenter();
	
	public QuestionCategory getDummyCategory();
}
