package ru.minogin.core.client.lang.parser;

public class NameExpression extends Expression {
	private String name;

	public NameExpression(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Object eval(Executor executor) {
		return executor.eval(this);
	}
}
