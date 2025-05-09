package ru.imagebook.client.admin.view.questions;

import java.util.Date;
import java.util.List;

import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;

import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionsView extends IsWidget {
	void updateQuestions();

	void showQuestions(List<Question> questions, int offset, int totalCount);

	void showAddForm();

	String getName();

	void setName(String name);

	void hideAddForm();

	Question getSelectedQuestion();

	void showEditForm();

	void hideEditForm();

	void confirmDelete();

	List<Question> getSelectedQuestions();

	void alertSelectDeleteQuestions();

	void alertSelectEditQuestions();

	void setPresenter(QuestionsPresenter presenter);
	
	void setQuestionCategory(QuestionCategory questionCategory);

	void fetch(Question question);
	
	void reload();
	
	void reloadFull();
	
	void setNewQuestionFieldValues(Date date);

	void fillCategorySelect(List<QuestionCategory> categories);
	
	void setFormValues(Question question);
}
