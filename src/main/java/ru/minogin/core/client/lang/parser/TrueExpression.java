package ru.minogin.core.client.lang.parser;

public class TrueExpression extends Expression {
	@Override
	public Object eval(Executor executor) {
		return executor.eval(this);
	}
}
