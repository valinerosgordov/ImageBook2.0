package ru.imagebook.client.admin.view.questions;

import ru.imagebook.shared.model.QuestionCategory;

import com.extjs.gxt.ui.client.data.BaseModel;

public class QuestionCategoryModel extends BaseModel{
	private static final long serialVersionUID = -4392020349419447389L;
	
	private final QuestionCategory questionCategory;
	
	public QuestionCategoryModel(QuestionCategory questionCategory) {
		this.questionCategory = questionCategory;

		set(QuestionCategory.NAME, questionCategory.getName());
		set(QuestionCategory.NUMBER, questionCategory.getNumber());
	}
	
	public QuestionCategory getQuestionCategory(){
		return questionCategory;
	}
}
