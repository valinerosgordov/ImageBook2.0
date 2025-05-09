package ru.imagebook.client.app.view;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.i18n.lang.ImplodeFunction;
import ru.minogin.core.client.i18n.lang.PrefixFunction;
import ru.minogin.core.client.lang.template.Compiler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CompilerFactory {
	private Compiler compiler;

	@Inject
	public CompilerFactory(CoreFactory coreFactory) {
		compiler = coreFactory.createCompiler();
		compiler.registerFunction("implode", new ImplodeFunction());
		compiler.registerFunction("prefix", new PrefixFunction());
	}

	public Compiler getCompiler() {
		return compiler;
	}
}
