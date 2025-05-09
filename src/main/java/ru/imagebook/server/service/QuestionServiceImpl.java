package ru.imagebook.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.service.QuestionService;
import ru.imagebook.client.faq.service.QuestionsService;
import ru.imagebook.server.repository.QuestionCategoryRepository;
import ru.imagebook.server.repository.QuestionRepository;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.gxt.grid.LoadResult;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.hibernate.Dehibernate;

@PreAuthorize("hasRole('SITE_ADMIN')")
public class QuestionServiceImpl implements QuestionService, QuestionsService {
	@Autowired
	private QuestionRepository repository;

	@Autowired
	private QuestionCategoryRepository categoryRepository;

	@Autowired
	private MessageSource messages;

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private VendorService vendorService;

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
	public void deleteQuestions(List<Integer> ids) {
		if (ids.isEmpty())
			return;
		repository.deleteQuestions(ids);
	}

	@Transactional
	@Override
	public void saveQuestion(Integer questionCategoryId, Question prototype) {
		Question question = repository.getQuestion(prototype.getId());

		question.setQuestionCategory(prototype.getQuestionCategory());
		question.setAnswer(prototype.getAnswer());
		question.setDate(prototype.getDate());
		question.setEmail(prototype.getEmail());
		question.setName(prototype.getName());
		question.setPubl(prototype.isPubl());
		question.setQuestion(prototype.getQuestion());
	}

	@Transactional
	@Override
	public void addQuestion(Integer questionCategoryId, Question question) {
		QuestionCategory questionCategory = questionCategoryId == null ? null : repository
				.getQuestionCategory(questionCategoryId);
		question.setQuestionCategory(questionCategory);
		repository.saveQuestion(question);
	}

	@Transactional
	@Dehibernate
	@Override
	public List<QuestionCategory> getAllCategories() {
		return categoryRepository.loadAllQuestionCategories();
	}

	@Override
	@Transactional
	@PreAuthorize("permitAll")
	public void askQuestion(Question question, Integer questionCategoryId) {
		QuestionCategory questionCategory = questionCategoryId == null ? null : repository
				.getQuestionCategory(questionCategoryId);
		question.setQuestionCategory(questionCategory);
		repository.saveQuestion(question);

		FreeMarker freeMarker;
		String html;
		Vendor vendor = vendorService.getVendorByCurrentSite();
		String[] args = { vendor.getName() };
		String subject = messages.getMessage("askQuestionSubject", args, new Locale(Locales.RU));
		String adminSubject = messages.getMessage("adminAskQuestionSubject", args, new Locale(Locales.RU));
		//
		// Sending information to registered user.
		if (question.getEmail() != null && !question.getEmail().equals("")) {
			User user = new User();
			user.setVendor(vendor);
			List<Email> userEmails = new ArrayList<Email>();
			userEmails.add(new Email(question.getEmail(), true));
			user.setEmails(userEmails);

			freeMarker = new FreeMarker(getClass());
			html = freeMarker.process("askThanks.ftl", Locales.RU);

			notifyService.notifyUser(user, subject, html);
		}

		notifyService.notifyVendorAdmin(vendor, adminSubject, question.getQuestion());
	}

}
