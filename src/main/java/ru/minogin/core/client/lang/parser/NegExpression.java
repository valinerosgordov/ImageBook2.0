package ru.minogin.core.client.lang.parser;

public class NegExpression extends Expression {
	private Expression op;
	
	public NegExpression(Expression op) {
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
