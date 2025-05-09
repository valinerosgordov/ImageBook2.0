package ru.minogin.core.client.lang.parser;

import java.util.List;

public class FunctionCallExpression extends Expression {
	private String function;
	private List<Expression> args;

	public FunctionCallExpression(String function, List<Expression> args) {
		this.function = function;
		this.args = args;
	}

	public String getFunction() {
		return function;
	}

	public List<Expression> getArgs() {
		return args;
	}

	@Override
	public Object eval(Executor executor) {
		return executor.eval(this);
	}
}
