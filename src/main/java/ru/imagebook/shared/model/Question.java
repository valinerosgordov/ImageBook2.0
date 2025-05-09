package ru.imagebook.shared.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.bean.BaseEntityBean;
import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
@Table(name = "question")
public class Question extends BaseEntityImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2487216389145651384L;
	public static final String NAME = "name";
	public static final String DATE = "date";
	public static final String EMAIL = "email";
	public static final String QUESTION = "question";
	public static final String ANSWER = "answer";
	public static final String QUESTION_CATEGORY = "questionCategory";
	public static final String PUBL = "publ";
	public static final String IS_ANSWERED = "isAnswered";

	private String name;
	private String email;
	private Date date;
	private String question;
	private String answer;
	private boolean publ;
	private QuestionCategory questionCategory;


	public Question() {
		setDate(new Date());
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	@NotNull	
	@Type(type = "text")	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
		
	@Type(type = "text")	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public boolean isPubl() {
		return publ;
	}

	public void setPubl(boolean publ) {
		this.publ = publ;
	}	
	
    @ManyToOne(optional=true)
	@JoinColumn(name="category")
	public QuestionCategory getQuestionCategory() {
		return questionCategory;
	}

	public void setQuestionCategory(QuestionCategory questionCategory) {
		this.questionCategory = questionCategory;
	}


}
