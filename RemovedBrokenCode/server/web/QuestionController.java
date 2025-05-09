package ru.imagebook.server.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.imagebook.server.model.QuestionCategoryView;
import ru.imagebook.server.model.QuestionView;
import ru.imagebook.server.model.web.Breadcrumb;
import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.server.model.web.PagerItem;
import ru.imagebook.server.service.QuestionService;
import ru.imagebook.server.service.QuestionSiteService;
import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.site.SiteConfig;
import ru.imagebook.server.service2.web.WebService;
import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.gwt.ClientParametersWriter;

@Controller
public class QuestionController extends MainController {
	@Autowired
	private MessageSource messages;	
	@Autowired
	private QuestionSiteService service;
	
	private final WebService webService;
	private final SiteConfig config;
	private final String url;

	@Autowired
	public QuestionController(WebService service, ServerConfig serverConfig,
			SiteConfig config) {
		this.webService = service;
		this.config = config;

		String dir = "faq";
		url = serverConfig.getWebPrefix() + "/" + dir;
	}
	
	@RequestMapping(value = "/faq", method = RequestMethod.GET)
	public String getAllQuestionCategories(Model model) {
		
		PageModel pageModel = new PageModel();
		pageModel.setH1(messages.getMessage("allQuestionCategories", null,  new Locale(Locales.RU)));
		layout(model, pageModel);

		List<QuestionCategory> questionCategories = service.loadAllQuestionCategories();
		
		List<QuestionCategoryView> views = createQuestionCategoriesViews(questionCategories);
		model.addAttribute("questionCategories", views);

		if(!questionCategories.isEmpty()) {
			model.addAttribute("toAsk", Boolean.TRUE);
		}
		else {
			model.addAttribute("toAsk", Boolean.FALSE);
		}
		
		ClientParametersWriter writer = new ClientParametersWriter();
		writer.setParam("question", true);
		writer.setParam("questionCategoryId", "null");
		writer.write(model);
		
		List<Breadcrumb> breadcrumb = pageModel.getBreadcrumbs();
		breadcrumb.add(new Breadcrumb(messages.getMessage("mainPage", null, new Locale(Locales.RU)), "/"));
		breadcrumb.add(new Breadcrumb(messages.getMessage("allQuestionCategories", null, new Locale(Locales.RU))));
		model.addAttribute("breadcrumb", breadcrumb);
		return "questionCategories";
	}
	
	@RequestMapping(value = { "/faq/{categoryId}" }, method = RequestMethod.GET)
	public String getQuestionsFirstPage(@PathVariable int categoryId, Model model) {
		return getQuestions(1, categoryId, model);
	}

	@RequestMapping(value = { "/faq/{categoryId}/page/{page}" }, method = RequestMethod.GET)
	public String getQuestions(@PathVariable int page, @PathVariable int categoryId, Model model) {
		page--;

		QuestionCategory questionCategory = service.getQuestionCategory(categoryId);
		
		PageModel pageModel = new PageModel();
		pageModel.setH1(questionCategory.getName());
		layout(model, pageModel);

		List<Question> questions = service.getCategoryQuestions(categoryId, page);
		int total = service.countCategoryQuestions(categoryId);
 
		if(total > 0)
			model.addAttribute("toAsk", Boolean.TRUE);
		
		List<QuestionView> views = createQuestionViews(questions);
		model.addAttribute("questions", views);

		String categoryName = categoryId + "";
		int pages = (total - 1) / QuestionService.QUESTIONS_ON_PAGE + 1;
		List<PagerItem> pagerItems = new ArrayList<PagerItem>();
		for (int i = 0; i < pages; i++) {
			String pageName = (i + 1) + "";
			PagerItem item = new PagerItem();
			item.setName(pageName);
			if (i != page) {
				String url = getPrefix() + "/faq/" + categoryName;
				if (i > 0)
					url += "/page/" + pageName;
				item.setUrl(url);
			}
			pagerItems.add(item);
			if (i > 10)
				break;
		}
		model.addAttribute("pagerItems", pagerItems);

		ClientParametersWriter writer = new ClientParametersWriter();
		writer.setParam("question", true);
		writer.setParam("questionCategoryId", categoryId);
		writer.write(model);
		
		List<Breadcrumb> breadcrumb = pageModel.getBreadcrumbs();
		breadcrumb.add(new Breadcrumb(messages.getMessage("mainPage", null, new Locale(Locales.RU)), "/"));
		breadcrumb.add(new Breadcrumb(messages.getMessage("allQuestionCategories", null, new Locale(Locales.RU)), url));
		breadcrumb.add(new Breadcrumb(questionCategory.getName(), url));
		model.addAttribute("breadcrumb", breadcrumb);
		
		return "questions";
	}	
	
	protected List<QuestionCategoryView> createQuestionCategoriesViews(List<QuestionCategory> questionCategories) {
		List<QuestionCategoryView> views = new ArrayList<QuestionCategoryView>();
		for (QuestionCategory category : questionCategories) {
			QuestionCategoryView view = new QuestionCategoryView();
			view.setId(category.getId());
			view.setName(category.getName());
			view.setUrl(getPrefix() + "/faq/" + category.getId());
			view.setNumberOfAnswers(service.countCategoryQuestions(category.getId()));
			views.add(view);
		}
		return views;
	}
	
	protected List<QuestionView> createQuestionViews(List<Question> questions) {
		List<QuestionView> views = new ArrayList<QuestionView>();
		for (Question question : questions) {
			QuestionView view = new QuestionView();
			view.setDate(question.getDate());
			view.setAnswer(question.getAnswer());
			view.setQuestion(question.getQuestion());
			view.setName(question.getName());
			views.add(view);
		}
		return views;
	}
	
}
