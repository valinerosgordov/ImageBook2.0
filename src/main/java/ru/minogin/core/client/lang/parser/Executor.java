package ru.minogin.core.client.lang.parser;

public abstract class Executor {
	public abstract void execute(Script script);

	public abstract void execute(Block block);

	public abstract void execute(ExpressionStatement expressionStatement);

	public abstract void execute(AssignmentStatement statement);

	public abstract void execute(IfStatement statement);

	public abstract void execute(WhileStatement statement);

	public abstract Object eval(NumberExpression expression);

	public abstract Object eval(StringExpression expression);
	
	public abstract Object eval(TrueExpression expression);
	
	public abstract Object eval(FalseExpression expression);

	public abstract Object eval(SumExpression expression);

	public abstract Object eval(DiffExpression expression);

	public abstract Object eval(DotExpression expression);

	public abstract Object eval(MulExpression expression);

	public abstract Object eval(DivExpression expression);

	public abstract Object eval(FunctionCallExpression expression);

	public abstract Object eval(NameExpression expression);

	public abstract Object eval(AndExpression expression);

	public abstract Object eval(OrExpression expression);

	public abstract Object eval(EqualsExpression expression);

	public abstract Object eval(NotEqualsExpression expression);

	public abstract Object eval(LessExpression expression);

	public abstract Object eval(GreaterExpression expression);

	public abstract Object eval(LessEqualsExpression expression);

	public abstract Object eval(GreaterEqualsExpression expression);

	public abstract Object eval(NegExpression expression);

	public abstract Object eval(NotExpression expression);

	public abstract Object eval(ObjectExpression expression);
}
