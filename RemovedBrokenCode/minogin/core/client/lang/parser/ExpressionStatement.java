package ru.minogin.core.client.lang.parser;


public class ExpressionStatement extends Statement {
	private Expression expression;

	public ExpressionStatement(Expression expression) {
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}
	
	@Override
	public void execute(Executor executor) {
		executor.execute(this);
	}
}
