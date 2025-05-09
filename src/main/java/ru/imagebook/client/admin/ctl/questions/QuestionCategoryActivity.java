package ru.imagebook.client.admin.ctl.questions;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.service.QuestionCategoryServiceAsync;
import ru.imagebook.client.admin.service.QuestionServiceAsync;
import ru.imagebook.client.admin.view.questions.QuestionCategoryConstants;
import ru.imagebook.client.admin.view.questions.QuestionCategoryPresenter;
import ru.imagebook.client.admin.view.questions.QuestionCategoryView;
import ru.imagebook.client.admin.view.questions.QuestionsPresenter;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.app.failure.XAsyncCallback;
import ru.minogin.core.client.gxt.grid.LoadResult;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class QuestionCategoryActivity extends AbstractActivity implements
	QuestionCategoryPresenter {
	@Inject
	private QuestionCategoryView view;
	@Inject
	private QuestionCategoryServiceAsync service;

	private QuestionsPresenter questionsController;
	
	private QuestionCategory questionCategory;
	private QuestionCategory dummyCategory;
	
    private QuestionCategoryConstants constants = GWT
	    .create(QuestionCategoryConstants.class);
	
	@Inject
	public QuestionCategoryActivity(QuestionCategoryView view, QuestionServiceAsync auxService
		, QuestionCategoryServiceAsync service) {
		view.setPresenter(this);
		this.view = view;
		this.questionsController = new QuestionsController(auxService);
		this.service = service;
		
		dummyCategory = new QuestionCategory();
		dummyCategory.setName(constants.categoryNotSelected());
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
	}

	@Override
	public void addButtonClicked() {
		view.showAddForm();
	}

	@Override
	public void saveButtonClickedOnAddForm() {
		QuestionCategory questionCategory = new QuestionCategory();
		view.fetch(questionCategory);
		service.addQuestionCategory(questionCategory, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideAddForm();
				view.updateQuestionCategories();
			}
		});
	}

	@Override
	public void editButtonClicked() {
		if (view.getSelectedQuestionCategories().isEmpty()) {
			view.alertSelectEditCountries();
		}
		else {
		    	questionCategory = view.getSelectedQuestionCategory();
			if (questionCategory.getId() == null) {
				return; // cannot edit dummy category.
			}
			view.showEditForm();
			view.setFormValues(questionCategory);
		}
	}

	@Override
	public void deleteButtonClicked() {
		if (view.getSelectedQuestionCategories().isEmpty())
			view.alertSelectDeleteCountries();
		else
			view.confirmDelete();
	}

	@Override
	public void saveButtonClickedOnEditForm() {
		view.fetch(questionCategory);
		service.saveQuestionCategory(questionCategory, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideEditForm();
				view.updateQuestionCategories();
			}
		});
	}

	@Override
	public void deleteConfirmed() {
		List<QuestionCategory> questionCategories = view.getSelectedQuestionCategories();
		List<Integer> ids = new ArrayList<Integer>();
		for (QuestionCategory questionCategory : questionCategories) {
			ids.add(questionCategory.getId());
		}
		service.deleteQuestionCategories(ids, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.updateQuestionCategories();
				view.getQuestionsView().setQuestionCategory(null);
			}
		});
	}

	@Override
	public void loadQuestionCategories(final int offset, final int limit) {
		service.loadQuestionCategories(offset, limit, new XAsyncCallback<LoadResult<QuestionCategory>>() {
			@Override
			public void onSuccess(LoadResult<QuestionCategory> countries) {
				view.showQuestionCategories(countries.getObjects(), offset, (int) countries.getTotal());
			}
		});
	}

	@Override
	public void questionCategoryClicked() {
		if (!view.getSelectedQuestionCategories().isEmpty() && view.getSelectedQuestionCategories().size() == 1) {
			questionsController.setQuestionCategory(view.getSelectedQuestionCategory());
		}
	}

	@Override
	public QuestionsPresenter getQuestionsPresenter() {
		return questionsController;
	}
	
	@Override
	public QuestionCategory getDummyCategory() {
		return dummyCategory;
	}
}
