package ru.imagebook.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.faq.service.QuestionsService;
import ru.imagebook.server.repository.QuestionRepository;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;


public class SiteQuestionServiceImpl implements QuestionsService{
	@Autowired
	private QuestionRepository repository;
	@Autowired
	private NotifyService notifyService;
	
	@Transactional
	@Override
	public void askQuestion(Question question, Integer questionCategoryId) {
		//Save to DataBase
		QuestionCategory questionCategory = questionCategoryId == null
				? null
				: repository.getQuestionCategory(questionCategoryId);
		question.setQuestionCategory(questionCategory);		
		repository.saveQuestion(question);
		
		FreeMarker freeMarker;
		String html;
		String subject = "Онлайн-налоги: спасибо за вопрос";
		String adminSubject = "Онлайн-налоги: спасибо за вопрос";
		//
		// Sending information to registered user.
		if(question.getEmail() != null && !question.getEmail().equals("")){
			User user = new User();
			List<Email> userEmails = new ArrayList<Email>();
			userEmails.add(new Email(question.getEmail(), true));
			user.setEmails(userEmails);
	
			freeMarker = new FreeMarker(getClass());
			html = freeMarker.process("askThanks.ftl", Locales.RU);
	
			notifyService.notifyUser(user, subject, html);
		}		
		
		notifyService.notifyAdmin(adminSubject, question.getQuestion());
	}	
	
}
