package ru.minogin.core.client.lang.parser;

import java.util.Map;

public class ObjectExpression extends Expression {
	private Map<Expression, Expression> values;

	public ObjectExpression(Map<Expression, Expression> values) {
		this.values = values;
	}
	
	public Map<Expression, Expression> getValues() {
		return values;
	}

	@Override
	public Object eval(Executor executor) {
		return executor.eval(this);
	}
}
