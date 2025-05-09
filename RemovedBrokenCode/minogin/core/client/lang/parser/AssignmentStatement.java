package ru.minogin.core.client.lang.parser;

public class AssignmentStatement extends Statement {
	private final Expression var;
	private final Expression value;

	public AssignmentStatement(Expression var, Expression value) {
		this.var = var;
		this.value = value;
	}

	public Expression getVar() {
		return var;
	}

	public Expression getValue() {
		return value;
	}

	@Override
	public void execute(Executor executor) {
		executor.execute(this);
	}
}
