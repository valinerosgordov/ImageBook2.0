package ru.imagebook.shared.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.imagebook.shared.model.Question;
import ru.minogin.core.shared.model.BaseEntityImpl;
@Entity
@Table(name = "questioncategory")
public class QuestionCategory extends BaseEntityImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3115228499718909386L;
	public static final String NUMBER = "number";
	public static final String NAME = "name";
	public static final String QUESTIONS = "questions";

	private Set<Question> questions;
	private String name;
	private int number;


	public QuestionCategory() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@OneToMany(mappedBy = "questionCategory", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

}
