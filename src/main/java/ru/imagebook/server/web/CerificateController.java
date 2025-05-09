package ru.imagebook.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.imagebook.server.model.web.Breadcrumb;
import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.shared.service.app.IncorrectCodeError;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.site.SiteConfig;
import ru.imagebook.server.service2.web.WebService;
import ru.imagebook.shared.model.*;
import java.util.*;

@Controller
public class CerificateController extends MainController {

	@Autowired
	protected UserService userService;
	@Autowired
	protected AuthService authService;

	private final WebService service;
	private final OrderService orderService;
	private final SiteConfig config;
	private final String url;

	private static final String ROOT_URL = "checkCertificate";
	private static final String VIEW = "certificate";
	private static final String ROOT_URL_WITH_SLASH = "/" + ROOT_URL;
	private static final String PAGE_HEADER = "Проверка сертификата по коду";

	@Autowired
	public CerificateController(WebService service, ServerConfig serverConfig,
								OrderService orderService, SiteConfig config) {
		this.service = service;
		this.orderService = orderService;
		this.config = config;

		url = serverConfig.getWebPrefix() +  ROOT_URL_WITH_SLASH;
	}

	@Override
	protected void layout(Model model, PageModel page) {
		super.layout(model, page);

		model.addAttribute("checkCertificateUrl", url);

		List<Breadcrumb> breadcrumbs = page.getBreadcrumbs();
		breadcrumbs.add(new Breadcrumb("Главная", "/"));
		breadcrumbs.add(new Breadcrumb(PAGE_HEADER, url));
	}

	@RequestMapping(value = ROOT_URL_WITH_SLASH, method = RequestMethod.GET)
	public String initForm(final Model model) {
		PageModel page = new PageModel();
		page.setH1(PAGE_HEADER);
		layout(model, page);
		return VIEW;
	}

	@RequestMapping(value = ROOT_URL_WITH_SLASH+"/check", method = RequestMethod.POST)
	public String check(final @RequestParam(value="bonusCode", required = true) String bonusCode, final Model model) {
		PageModel page = new PageModel();
		page.setH1(PAGE_HEADER);
		layout(model, page);
		if (bonusCode == null || "".equals(bonusCode.trim())) {
			model.addAttribute("isEmptyCodeError", true);
			return VIEW;
		} else {
			model.asMap().remove("isEmptyCodeError");
		}
		BonusAction action;

		try {
			action = service.getBonusAction(bonusCode.trim());
		} catch (IncorrectCodeError e) {
			action = null;
		}
		if (action != null) {
			model.addAttribute("action", action);
		}
		model.addAttribute("isBonusCodeCheckSuccessfull", action != null);
		model.addAttribute("bonusCode", bonusCode);

		return VIEW;
	}
}
