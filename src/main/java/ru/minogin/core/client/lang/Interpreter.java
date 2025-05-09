package ru.minogin.core.client.lang;

import java.util.Collection;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.lang.parser.Parser;
import ru.minogin.core.client.lang.parser.Script;
import ru.minogin.core.client.lang.tokenizer.Tokenizer;

public class Interpreter {
	private final CoreFactory factory;

	private Functions functions = new Functions();

	public Interpreter(CoreFactory factory) {
		this.factory = factory;
	}

	public void registerFunction(Collection<String> names, Function function) {
		functions.registerFunction(names, function);
	}

	public void registerLibrary(Library library) {
		functions.registerLibrary(library);
	}

	public Object execute(String script, Bean context) {
		Tokenizer tokenizer = new Tokenizer(factory, script);
		Parser parser = new Parser(tokenizer);
		Script parsedScript = parser.parse();

		ScriptExecutor executor = new ScriptExecutor(functions, context);
		execute(parsedScript, executor);

		int size = executor.getResults().size();
		return size > 0 ? executor.getResults().get(size - 1) : null;
	}

	protected void execute(Script script, ScriptExecutor executor) {
		script.execute(executor);
	}
}
