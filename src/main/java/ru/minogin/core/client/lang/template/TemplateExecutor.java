package ru.minogin.core.client.lang.template;

import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.lang.Functions;
import ru.minogin.core.client.lang.ScriptExecutor;
import ru.minogin.core.client.lang.parser.Script;

public class TemplateExecutor extends ScriptExecutor {
	private final Renderer renderer;
	private String result;

	public TemplateExecutor(Functions functions, Bean context, Renderer renderer) {
		super(functions, context);

		this.renderer = renderer;
	}

	@Override
	public void execute(Script script) {
		super.execute(script);

		StringBuffer buffer = new StringBuffer();
		for (Object value : getResults()) {
			buffer.append(renderer.render(value));
		}

		result = buffer.toString();
	}

	public String getValue() {
		return result;
	}
}
