package ru.minogin.core.client.lang.template;

import java.util.Collection;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.collections.XArrayList;
import ru.minogin.core.client.lang.*;
import ru.minogin.core.client.lang.parser.Parser;
import ru.minogin.core.client.lang.parser.Script;
import ru.minogin.core.client.lang.tokenizer.Tokenizer;

public class Compiler {
	private final CoreFactory factory;
	private Functions functions = new Functions();
	private final Renderer renderer;

	public Compiler(CoreFactory factory) {
		this(factory, new BaseRenderer());
	}

	public Compiler(CoreFactory factory, Renderer renderer) {
		this.factory = factory;
		this.renderer = renderer;
	}

	public void registerFunction(Collection<String> names, Function function) {
		functions.registerFunction(names, function);
	}

	public void registerFunction(String name, Function function) {
		functions.registerFunction(new XArrayList<String>(name), function);
	}

	public void registerLibrary(Library library) {
		functions.registerLibrary(library);
	}

	public String compile(String template) {
		return compile(template, new BaseBean());
	}

	public String compile(String template, Bean context) {
		String code = new Preprocessor().process(template);

		Tokenizer tokenizer = new Tokenizer(factory, code);
		Parser parser = new Parser(tokenizer);
		Script script = parser.parse();

		TemplateExecutor executor = new TemplateExecutor(functions, context, renderer);
		execute(script, executor);

		return executor.getValue();
	}

	protected void execute(Script script, TemplateExecutor executor) {
		script.execute(executor);
	}
}
