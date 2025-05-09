package ru.imagebook.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.service.QuestionCategoryService;
import ru.imagebook.server.repository.QuestionCategoryRepository;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.gxt.grid.LoadResult;
import ru.minogin.core.server.hibernate.Dehibernate;

@PreAuthorize("hasRole('SITE_ADMIN')")
public class QuestionCategoryServiceImpl implements QuestionCategoryService {
	@Autowired
	private QuestionCategoryRepository questionCategoryRepository;
	
	
	@Transactional
	@Dehibernate
	@Override
	public LoadResult<QuestionCategory> loadQuestionCategories(int offset, int limit) {
		List<QuestionCategory> questionCategories = questionCategoryRepository.loadQuestionCategories(offset, limit);
		long total = questionCategoryRepository.countQuestionCategories();
		return new LoadResult<QuestionCategory>(questionCategories, offset, total);
	}
	
	@Transactional
	@Override	
	public void deleteQuestionCategories(List<Integer> ids){
		if (ids.isEmpty())
			return;		
		questionCategoryRepository.deleteQuestionCategories(ids);
	}
	
	@Transactional
	@Override
	public void saveQuestionCategory(QuestionCategory prototype) {
		QuestionCategory questionCategory = questionCategoryRepository.getQuestionCategory(prototype.getId());
		questionCategory.setName(prototype.getName());
		questionCategory.setNumber(prototype.getNumber());
	//TODO	questionCategoryRepository.flush();
	}
	
	@Transactional
	@Override
	public void addQuestionCategory(QuestionCategory questionCategory) {
		questionCategoryRepository.saveQuestionCategory(questionCategory);
	}

	@Override
	public Long countQuestionCategories() {
		// TODO Auto-generated method stub
		return null;
	}	
}
