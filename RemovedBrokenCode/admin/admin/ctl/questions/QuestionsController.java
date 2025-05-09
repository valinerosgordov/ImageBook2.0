package ru.imagebook.client.admin.ctl.questions;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.service.QuestionServiceAsync;
import ru.imagebook.client.admin.view.questions.QuestionsPresenter;
import ru.imagebook.client.admin.view.questions.QuestionsView;
import ru.imagebook.client.admin.view.questions.QuestionsViewImpl;
import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.app.failure.XAsyncCallback;
import ru.minogin.core.client.gxt.grid.LoadResult;

public class QuestionsController implements QuestionsPresenter {
	private QuestionsView view;
	private QuestionServiceAsync service;
	private QuestionCategory questionCategory;
	private Question question;
	
	
	public QuestionsController(QuestionServiceAsync service) {
		view = new QuestionsViewImpl();
		view.setPresenter(this);
		this.service = service;
	}

	@Override
	public void loadQuestions(final int offset, int limit) {
		view.setQuestionCategory(questionCategory);
		if (questionCategory != null) {
			service.loadQuestions(questionCategory.getId(), offset, limit, new XAsyncCallback<LoadResult<Question>>() {
				@Override
				public void onSuccess(LoadResult<Question> result) {
					view.showQuestions(result.getObjects(), offset, (int) result.getTotal());
				}
			});
		} else {
			view.showQuestions(new ArrayList<Question>(), offset, 0);
		}
	}

	@Override
	public QuestionsView getQuestionsView() {
		return view;
	}

	@Override
	public void addButtonClicked() {
		if(questionCategory != null){
			question = new Question();
			
			service.getAllCategories(new XAsyncCallback<List<QuestionCategory>>() {
				@Override
				public void onSuccess(List<QuestionCategory> result) {
					view.showAddForm();
					view.fillCategorySelect(result);
					view.setNewQuestionFieldValues(question.getDate());
				}
			});
		}
	}

	@Override
	public void editButtonClicked() {
		if (view.getSelectedQuestions().isEmpty()) {
			view.alertSelectEditQuestions();
		} else {
			service.getAllCategories(new XAsyncCallback<List<QuestionCategory>>() {
				@Override
				public void onSuccess(List<QuestionCategory> result) {
					question = view.getSelectedQuestion();
					view.showEditForm();
					view.fillCategorySelect(result);
					view.setFormValues(question);
				}
			});
		}
	}

	@Override
	public void deleteButtonClicked() {
		if (view.getSelectedQuestions().isEmpty())
			view.alertSelectDeleteQuestions();
		else
			view.confirmDelete();
	}

	@Override
	public void deleteConfirmed() {
		List<Question> questions = view.getSelectedQuestions();
		List<Integer> ids = new ArrayList<Integer>();
		for (Question question : questions) {
			ids.add(question.getId());
		}
		service.deleteQuestions(ids, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.updateQuestions();
			}
		});
	}

	@Override
	public void saveButtonClickedOnEditForm() {		
		view.fetch(question);
		service.saveQuestion(questionCategory.getId(), question, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideEditForm();
				view.updateQuestions();
			}
		});
	}

	@Override
	public void saveButtonClickedOnAddForm() {
		Question question = new Question();
		view.fetch(question);
		service.addQuestion(question.getQuestionCategory().getId(), question, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideAddForm();
				view.updateQuestions();
			}
		});
	}
	
	@Override
	public void setQuestionCategory(QuestionCategory questionCategory){
		this.questionCategory = questionCategory;
		view.reloadFull();
	}
	
	@Override
	public QuestionCategory getQuestionCategory(){
		return this.questionCategory;
	}
}
