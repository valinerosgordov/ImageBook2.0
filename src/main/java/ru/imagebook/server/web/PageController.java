package ru.imagebook.server.web;

import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.shared.model.site.Page;

public abstract class PageController extends MainController {
	protected PageModel createPageModel(Page page) {
		PageModel model = new PageModel();
		model.setTitle(page.getTitle());
		model.setKeywords(page.getKeywords());
		model.setDescription(page.getDescription());
		model.setH1(page.getH1());
		model.setWide(page.isWide());
		model.setFooter(page.getFooter());
		return model;
	}
}
