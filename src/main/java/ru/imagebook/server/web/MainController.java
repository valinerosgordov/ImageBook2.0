package ru.imagebook.server.web;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.flash.FlashService;
import ru.imagebook.server.service.site.FlashFunction;
import ru.imagebook.server.service2.web.WebService;
import ru.imagebook.shared.model.site.Phrase;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.lang.template.Compiler;

public abstract class MainController {
	private WebService service;
	private CoreFactory coreFactory;
	private FlashService flashService;
	private ServerConfig serverConfig;

	@Autowired
	public void setService(WebService service) {
		this.service = service;
	}

	@Autowired
	public void setCoreFactory(CoreFactory coreFactory) {
		this.coreFactory = coreFactory;
	}

	@Autowired
	public void setFlashService(FlashService flashService) {
		this.flashService = flashService;
	}

	@Autowired
	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	protected void layout(Model model, PageModel page) {
		String prefix = serverConfig.getWebPrefix();
		model.addAttribute("prefix", prefix);

		model.addAttribute("topSections", service.loadTopSections());
		model.addAttribute("banners", service.loadPageBanners());
		model.addAttribute("sections1", service.loadSections1());

		model.addAttribute("year", new DateTime().getYear());

		model.addAttribute("page", page);
	}

	protected String compile(String content) {
		Compiler compiler = coreFactory.createCompiler();
		compiler.registerFunction("flash", new FlashFunction(flashService));
		content = compiler.compile(content);

		// TODO [opt]
		List<Phrase> phrases = service.loadPhrases();
		for (Phrase phrase : phrases) {
			String key = "!" + phrase.getKey();
			String value = phrase.getValue();
			value = value.replace("<p>", "");
			value = value.replace("</p>", "");
			content = content.replace(key, value);
		}

		return content;
	}
	
	public String getPrefix() {
		return serverConfig.getWebPrefix();
	}
}
