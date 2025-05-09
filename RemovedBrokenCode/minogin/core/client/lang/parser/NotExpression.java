package ru.minogin.core.client.lang.parser;

public class NotExpression extends Expression {
	private Expression op;
	
	public NotExpression(Expression op) {
		this.op = op;
	}
	
	public Expression getOp() {
		return op;
	}
	
	@Override
	public Object eval(Executor executor) {
		return executor.eval(this);
	}
}
