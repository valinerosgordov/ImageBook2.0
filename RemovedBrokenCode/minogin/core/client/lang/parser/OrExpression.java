package ru.minogin.core.client.lang.parser;

public class OrExpression extends Expression {
	private Expression op1;
	private Expression op2;
	
	public OrExpression(Expression op1, Expression op2) {
		this.op1 = op1;
		this.op2 = op2;
	}
	
	public Expression getOp1() {
		return op1;
	}
	
	public Expression getOp2() {
		return op2;
	}
	
	@Override
	public Object eval(Executor executor) {
		return executor.eval(this);
	}
}
