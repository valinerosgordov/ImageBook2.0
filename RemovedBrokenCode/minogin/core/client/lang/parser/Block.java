package ru.minogin.core.client.lang.parser;

import java.util.ArrayList;
import java.util.List;


public class Block {
	private List<Statement> statements = new ArrayList<Statement>();
	
	public List<Statement> getStatements() {
		return statements;
	}
	
	public void execute(Executor executor) {
		executor.execute(this);
	}
}
