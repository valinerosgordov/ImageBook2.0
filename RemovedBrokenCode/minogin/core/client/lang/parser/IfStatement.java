package ru.minogin.core.client.lang.parser;

public class IfStatement extends Statement {
	private Expression condition;
	private Block thenBlock;
	private Block elseBlock;

	public IfStatement(Expression condition, Block thenBlock, Block elseBlock) {
		this.condition = condition;
		this.thenBlock = thenBlock;
		this.elseBlock = elseBlock;
	}

	public Expression getCondition() {
		return condition;
	}

	public Block getThenBlock() {
		return thenBlock;
	}

	public Block getElseBlock() {
		return elseBlock;
	}

	@Override
	public void execute(Executor executor) {
		executor.execute(this);
	}
}
