package ru.minogin.core.client.lang.parser;

public class Script {
	private Block block;

	public Script(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}
	
	public void execute(Executor executor) {
		executor.execute(this);
	}
	
	@Override
	public String toString() {
		return block.toString();
	}
}
