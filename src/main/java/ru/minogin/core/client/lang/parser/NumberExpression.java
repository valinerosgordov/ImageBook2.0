package ru.minogin.core.client.lang.parser;


public class NumberExpression extends Expression {
	private String value;

	public NumberExpression(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public Object eval(Executor executor) {
		return executor.eval(this);
	}
}
