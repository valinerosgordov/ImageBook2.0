package ru.saasengine.client.service;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.collections.XArrayList;
import ru.minogin.core.client.i18n.lang.I18nContext;
import ru.minogin.core.client.i18n.lang.NounFormFunction;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.client.text.lang.CapitalizeFunction;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LangService {
	private final I18nService i18nService;
	private final Compiler compiler;

	@Inject
	public LangService(CoreFactory coreFactory, I18nService i18nService) {
		this.i18nService = i18nService;

		compiler = new Compiler(coreFactory);
		compiler.registerFunction(new XArrayList<String>("nounForm", "форма_сущ"),
				new NounFormFunction());
		compiler.registerFunction(new XArrayList<String>("capitalize", "с_прописной"),
				new CapitalizeFunction());
	}

	public Compiler getCompiler() {
		return compiler;
	}

	public String compile(String template, Bean context) {
		I18nContext.setLocale(context, i18nService.getLocale());
		return compiler.compile(template, context);
	}

	public String compile(String template, String var, Object value) {
		Bean context = new BaseBean();
		context.set(var, value);
		return compile(template, context);
	}
}
