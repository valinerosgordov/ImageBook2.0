package ru.minogin.core.client.lang.parser;

public class WhileStatement extends Statement {
	private Expression condition;
	private Block body;

	public WhileStatement(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}

	public Expression getCondition() {
		return condition;
	}

	public Block getBody() {
		return body;
	}

	@Override
	public void execute(Executor executor) {
		executor.execute(this);
	}
}
