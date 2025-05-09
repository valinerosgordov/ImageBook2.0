package ru.imagebook.server.web;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.imagebook.server.model.web.Breadcrumb;
import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.server.service2.web.WebService;
import ru.imagebook.shared.model.site.Page;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.TopSection;
import ru.minogin.core.client.collections.CollectionUtil;
import ru.minogin.core.server.spring.PageNotFoundException;
import ru.minogin.twitter.server.config.site.TwitterConfig;
import ru.minogin.twitter.server.service.site.TwitterService;
import ru.minogin.twitter.shared.model.site.Twit;

@Controller
public class SectionController extends PageController {

	private static final int IMAGEBOOK_ONLINE_PAGE_ID = 15;

	private final WebService service;
	@Autowired(required = false)
	protected TwitterService twitterService;
	@Autowired(required = false)
	protected TwitterConfig twitterConfig;

	@Autowired
	public SectionController(WebService service) {
		this.service = service;
	}

	@RequestMapping(value = { "/index", "/" }, method = RequestMethod.GET)
	public String getIndex(Model model) {
		Section section = service.getSection("index");

		String content = section.getContent();
		// вставляем новости из twitter
		if ((twitterService != null) && (twitterConfig != null) && (content != null)) {
            boolean addNewsFromTwitter = content.contains("{twitter}");
			if (addNewsFromTwitter) {
				List<Twit> news = twitterService.getNews();

				StringBuilder sb = new StringBuilder();
				MessageFormat formatter = new MessageFormat(twitterConfig.getTemplate());

				for (Twit oneNews : news) {

					String dateString;
					if (oneNews.getLinkSite() != null) {
						dateString = "<a href=\"" + oneNews.getLinkSite() + "\">"
								+ getStringDate(oneNews.getCreatedDate()) + "</a>";
					}
					else {
						dateString = getStringDate(oneNews.getCreatedDate());
					}

					Object[] args = new Object[] {
							dateString,
							oneNews.getText()
					};
					sb.append(formatter.format(args));
				}
				section.setContent(content.replace("{twitter}", sb));
			}
		}

		PageModel page = createPageModel(section);
		layout(model, page);

		model.addAttribute("content", compile(section.getContent()));	// TODO [gen]

		model.addAttribute("index", true);

		return "page";
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/school_2014" }, method = RequestMethod.GET)
	public String getSchool2014(Model model) {
		Section section = service.getSection("school_2014");

		PageModel page = createPageModel(section);
		layout(model, page);

		// remove top section by id
		Map<String, Object> map = model.asMap();
		List<TopSection> topSections = (List<TopSection>) map.get("topSections");

		Iterator<TopSection> it = topSections.iterator();
		while (it.hasNext()) {
			TopSection topSection = it.next();
			if (topSection.getId() == IMAGEBOOK_ONLINE_PAGE_ID) {
				it.remove();
				break;
			}
		}

		model.addAttribute("content", compile(section.getContent()));   // TODO [gen]
		model.addAttribute("logoImageId", "logo-img-school");

		return "page";
	}

	@RequestMapping(value = "/{key}", method = RequestMethod.GET)
	public String getSection(@PathVariable String key, Model model) {
		Page page = service.getSection(key);
		if (page == null)
			page = service.getTopSection(key);
		if (page == null)
			page = service.getDocument(key);
		if (page == null)
			throw new PageNotFoundException();

		PageModel pageModel = createPageModel(page);
		layout(model, pageModel);

		model.addAttribute("content", compile(page.getContent()));

		List<Breadcrumb> breadcrumbs = pageModel.getBreadcrumbs();
		breadcrumbs.add(new Breadcrumb("Главная", "/"));

		if (page instanceof Section) {
			Section section = (Section) page;
			int level = section.getLevel();
			model.addAttribute("level", level);
			if (level == 1) {
				List<Section> sections2 = service.loadSections(section);
				model.addAttribute("sections2", sections2);

				breadcrumbs.add(new Breadcrumb(page.getName(), page.getKey()));
			}
			else if (level == 2) {
				Section parent = service.getSection(section.getParent().getId());

				List<Section> sections2 = service.loadSections(parent);
				section = CollectionUtil.find(sections2, section);
				section.set("selected", true);
				model.addAttribute("sections2", sections2);

				List<Section> sections3 = service.loadSections(section);
				if (!sections3.isEmpty())
					section.set("sections3", sections3);

				breadcrumbs.add(new Breadcrumb(parent.getName(), parent.getKey()));
				breadcrumbs.add(new Breadcrumb(section.getName(), section.getKey()));
			}
			else if (level == 3) {
				Section parent = service.getSection(section.getParent().getId());
				Section superParent = service.getSection(parent.getParent().getId());

				List<Section> sections2 = service.loadSections(superParent);
				parent = CollectionUtil.find(sections2, parent);
				model.addAttribute("sections2", sections2);

				List<Section> sections3 = service.loadSections(parent);
				section = CollectionUtil.find(sections3, section);
				for (Section iSection : sections3) {
					if (iSection.equals(section))
						iSection.set("selected", true);
				}
				parent.set("sections3", sections3);

				breadcrumbs.add(new Breadcrumb(superParent.getName(), superParent.getKey()));
				breadcrumbs.add(new Breadcrumb(parent.getName(), parent.getKey()));
				breadcrumbs.add(new Breadcrumb(section.getName(), section.getKey()));
			}
		}
		else {
			breadcrumbs.add(new Breadcrumb(page.getName(), page.getKey()));
		}

		return "page";
	}

	private static String getStringDate(Date date) {
		String[] ms = new String[] { "января", "февраля", "марта", "апреля", "мая",
				"июня", "июля", "августа", "сентября", "октября", "ноября", "декабря" };

		String p = "{0} {1} {2} г.";

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return MessageFormat.format(p, String.valueOf(c.get(Calendar.DAY_OF_MONTH)), ms[c.get(Calendar.MONTH)],
            String.valueOf(c.get(Calendar.YEAR)));
	}
}
