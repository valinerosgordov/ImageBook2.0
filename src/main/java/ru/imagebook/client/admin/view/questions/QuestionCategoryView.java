package ru.imagebook.client.admin.view.questions;

import java.util.List;

import ru.imagebook.shared.model.QuestionCategory;

import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionCategoryView extends IsWidget {
	void updateQuestionCategories();

	void showQuestionCategories(List<QuestionCategory> questionCategories, int offset, int totalCount);

	void showAddForm();

	void fetch(QuestionCategory questionCategory);

	void hideAddForm();

	QuestionCategory getSelectedQuestionCategory();

	void showEditForm();

	void hideEditForm();

	void confirmDelete();

	List<QuestionCategory> getSelectedQuestionCategories();

	void alertSelectDeleteCountries();

	void alertSelectEditCountries();

	void setPresenter(QuestionCategoryPresenter presenter);
	
	QuestionsView getQuestionsView();
	
	void setFormValues(QuestionCategory questionCategory);
}
