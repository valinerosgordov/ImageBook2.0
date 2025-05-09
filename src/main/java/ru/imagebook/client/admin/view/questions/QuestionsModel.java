package ru.imagebook.client.admin.view.questions;

import ru.imagebook.shared.model.Question;

import com.extjs.gxt.ui.client.data.BaseModel;

public class QuestionsModel extends BaseModel{
	private static final long serialVersionUID = -4392020349419447389L;
	
	private final Question question;
	
	public QuestionsModel(Question question){
		this.question = question;
		set(Question.ANSWER, question.getAnswer());
		set(Question.DATE, question.getDate());
		set(Question.EMAIL, question.getEmail());
		set(Question.NAME, question.getName());
		set(Question.PUBL, question.isPubl());
		set(Question.QUESTION, question.getQuestion());
		set(Question.QUESTION_CATEGORY, question.getQuestionCategory());
		if(question.getAnswer() != null)
			set(Question.IS_ANSWERED, true);
		else
			set(Question.IS_ANSWERED, false);
		set(Question.PUBL, question.isPubl());
	}
	
	public Question getQuestion(){
		return question;
	}
}
