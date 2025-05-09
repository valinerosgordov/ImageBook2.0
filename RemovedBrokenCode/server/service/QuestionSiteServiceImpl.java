package ru.imagebook.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.QuestionCategoryRepository;
import ru.imagebook.server.repository.QuestionRepository;
import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.gxt.grid.LoadResult;
import ru.minogin.core.server.hibernate.Dehibernate;

public class QuestionSiteServiceImpl implements QuestionSiteService {
	@Autowired
	private QuestionRepository repository;

	@Autowired
	private QuestionCategoryRepository categoryRepository;
	
	@Transactional
	@Dehibernate
	@Override
	public LoadResult<Question> loadQuestions(Integer questioncategoryId, int offset, int limit) {
		List<Question> questions = repository.loadQuestions(questioncategoryId, offset, limit);
		long total = repository.countQuestions(questioncategoryId);
		return new LoadResult<Question>(questions, offset, total);
		
	}
	
	@Transactional
	@Override	
	public void deleteQuestions(List<Integer> ids){
		if (ids.isEmpty())
			return;
		repository.deleteQuestions(ids);
	}	
	
	@Transactional
	@Override
	public void saveQuestion(Integer questionCategoryId, Question prototype) {
		Question question = repository.getQuestion(prototype.getId());
		QuestionCategory questionCategory = questionCategoryId == null
				? null
				: repository.getQuestionCategory(questionCategoryId);
		question.setAnswer(prototype.getAnswer());		
		question.setDate(prototype.getDate());
		question.setEmail(prototype.getEmail());
		question.setName(prototype.getName());
		question.setPubl(prototype.isPubl());
		question.setQuestion(prototype.getQuestion());
		question.setQuestionCategory(questionCategory);
	}
	
	@Transactional
	@Override
	public void addQuestion(Integer questionCategoryId, Question question) {
		QuestionCategory questionCategory = questionCategoryId == null
				? null
				: repository.getQuestionCategory(questionCategoryId);
		question.setQuestionCategory(questionCategory);		
		repository.saveQuestion(question);
	}

	@Transactional
	@Dehibernate
	@Override
	public List<QuestionCategory> getAllCategories() {
		return categoryRepository.loadAllQuestionCategories();
	}
	
	@Transactional
	@Override
	public List<QuestionCategory> loadAllQuestionCategories() {
		return repository.loadAllQuestionCategories();
	}
	
	@Transactional
	@Override
	public List<Question> getCategoryQuestions(int categoryId, int page) {
		int offset = page * QUESTIONS_ON_PAGE;
		int limit = QUESTIONS_ON_PAGE;
		return repository.getCategoryQuestions(categoryId, offset, limit);
	}
	
	@Transactional
	@Override
	public int countCategoryQuestions(int categoryId) {
		return (int) repository.countCategoryQuestions(categoryId);
	}	
	
	@Transactional
	@Override
	public QuestionCategory getQuestionCategory(int id){
		return repository.getQuestionCategory(id);
	}
	
}
