package ru.imagebook.client.admin.view.questions;

import ru.imagebook.shared.model.QuestionCategory;

public interface QuestionsPresenter {
	void addButtonClicked();

	void saveButtonClickedOnAddForm();

	void editButtonClicked();

	void deleteButtonClicked();

	void saveButtonClickedOnEditForm();

	void deleteConfirmed();

	void loadQuestions(int offset, int limit);
	
	QuestionsView getQuestionsView();

	void setQuestionCategory(QuestionCategory questionCategory);
	
	QuestionCategory getQuestionCategory();
}
