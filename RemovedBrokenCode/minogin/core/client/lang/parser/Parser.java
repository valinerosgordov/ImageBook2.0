package ru.minogin.core.client.lang.parser;

import java.util.*;

import ru.minogin.core.client.lang.LangError;
import ru.minogin.core.client.lang.tokenizer.Token;
import ru.minogin.core.client.lang.tokenizer.Tokenizer;
import ru.minogin.core.client.lang.tokenizer.Token.Type;

public class Parser {
	private static final int CLOSE_PARENTHESIS_EXPECTED = 200;
	private static final int SYNTAX_ERROR = 201;
//	private static final int OBJECT_COLON_EXPECTED = 202;

	private final Tokenizer tokenizer;

	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	public Script parse() {
		return new Script(parseBlock(Type.END));
	}

	private Block parseBlock(Type... stopTypes) {
		Block block = new Block();

		while (true) {
			Token token = tokenizer.next();
			while (token.getType() == Type.SEMICOLON)
				token = tokenizer.next();
			tokenizer.undo();

			boolean stop = false;
			for (Type stopType : stopTypes) {
				if (token.getType() == stopType)
					stop = true;
			}
			if (stop)
				break;

			block.getStatements().add(parseStatement());
		}

		return block;
	}

	private Statement parseStatement() {
		Token token = tokenizer.next();
		if (token.getType() == Type.IF)
			return parseIf();
		else if (token.getType() == Type.WHILE)
			return parseWhile();
		tokenizer.undo();

		Expression expression = parseExpression();

		token = tokenizer.next();
		if (token.getType() == Type.COLON) {
			return new AssignmentStatement(expression, parseExpression());
		}
		tokenizer.undo();

		return new ExpressionStatement(expression);
	}

	private IfStatement parseIf() {
		Expression condition = parseExpression();
		Block thenBlock = parseBlock(Type.ELSE, Type.ENDIF);
		Block elseBlock;
		Token token = tokenizer.next();
		if (token.getType() == Type.ELSE) {
			elseBlock = parseBlock(Type.ENDIF);
			tokenizer.next();
		}
		else
			elseBlock = new Block();
		return new IfStatement(condition, thenBlock, elseBlock);
	}

	private WhileStatement parseWhile() {
		Expression condition = parseExpression();
		Block body = parseBlock(Type.ENDWHILE);
		tokenizer.next();
		return new WhileStatement(condition, body);
	}

	private Expression parseExpression() {
		Expression expression = parseLogic();

		return expression;
	}

	private Expression parseLogic() {
		Expression expression = parseRelation();

		while (true) {
			Token token = tokenizer.next();
			if (token.getType() == Type.AND)
				expression = new AndExpression(expression, parseRelation());
			else if (token.getType() == Type.OR)
				expression = new OrExpression(expression, parseRelation());
			else
				break;
		}

		tokenizer.undo();

		return expression;
	}

	private Expression parseRelation() {
		Expression expression = parseSum();

		while (true) {
			Token token = tokenizer.next();
			if (token.getType() == Type.EQUALS)
				expression = new EqualsExpression(expression, parseSum());
			else if (token.getType() == Type.LESS)
				expression = new LessExpression(expression, parseSum());
			else if (token.getType() == Type.LESS)
				expression = new EqualsExpression(expression, parseSum());
			else if (token.getType() == Type.LESS)
				expression = new EqualsExpression(expression, parseSum());
			else if (token.getType() == Type.LESS)
				expression = new EqualsExpression(expression, parseSum());
			else if (token.getType() == Type.LESS)
				expression = new EqualsExpression(expression, parseSum());
			else
				break;
		}

		tokenizer.undo();

		return expression;
	}

	private Expression parseSum() {
		Expression expression = parseMul();

		while (true) {
			Token token = tokenizer.next();
			if (token.getType() == Type.PLUS)
				expression = new SumExpression(expression, parseMul());
			else if (token.getType() == Type.MINUS)
				expression = new DiffExpression(expression, parseMul());
			else
				break;
		}

		tokenizer.undo();

		return expression;
	}

	private Expression parseMul() {
		Expression expression = parsePrimary();

		while (true) {
			Token token = tokenizer.next();
			if (token.getType() == Type.MUL)
				expression = new MulExpression(expression, parsePrimary());
			else if (token.getType() == Type.DIV)
				expression = new DivExpression(expression, parsePrimary());
			else
				break;
		}

		tokenizer.undo();

		return expression;
	}

	private Expression parsePrimary() {
		Expression expression;

		Token token = tokenizer.next();
		if (token.getType() == Type.MINUS)
			expression = new NegExpression(parseDot());
		else if (token.getType() == Type.NOT)
			expression = new NotExpression(parseDot());
		else {
			tokenizer.undo();
			expression = parseDot();
		}

		return expression;
	}

	private Expression parseDot() {
		Expression expression = parseBase();

		while (true) {
			Token token = tokenizer.next();
			if (token.getType() == Type.DOT)
				expression = new DotExpression(expression, parseBase());
			else
				break;
		}

		tokenizer.undo();

		return expression;
	}

	private Expression parseBase() {
		Expression expression;

		Token token = tokenizer.next();
		if (token.getType() == Type.STRING)
			expression = new StringExpression(token.getValue());
		else if (token.getType() == Type.NUMBER)
			expression = new NumberExpression(token.getValue());
		else if (token.getType() == Type.TRUE)
			expression = new TrueExpression();
		else if (token.getType() == Type.FALSE)
			expression = new FalseExpression();
		else if (token.getType() == Type.NAME) {
			Token token2 = tokenizer.next();
			if (token2.getType() == Type.OPEN_PARENTHESIS)
				expression = parseFunctionCall(token.getValue());
			else {
				tokenizer.undo();
				expression = new NameExpression(token.getValue());
			}
		}
		else if (token.getType() == Type.OPEN_PARENTHESIS) {
			expression = parseExpression();
			token = tokenizer.next();
			if (token.getType() != Type.CLOSE_PARENTHESIS)
				throw new LangError(CLOSE_PARENTHESIS_EXPECTED);
		}
		else if (token.getType() == Type.LEFT_BRACE)
			expression = parseObject();
		else
			throw new LangError(SYNTAX_ERROR);

		return expression;
	}

	private Expression parseFunctionCall(String function) {
		List<Expression> args = new ArrayList<Expression>();
		while (true) {
			Token token = tokenizer.next();

			if (token.getType() == Type.CLOSE_PARENTHESIS)
				break;
			tokenizer.undo();

			args.add(parseExpression());

			token = tokenizer.next();
			if (token.getType() == Type.CLOSE_PARENTHESIS)
				break;
			if (token.getType() != Type.COMMA && token.getType() != Type.SEMICOLON)
				tokenizer.undo();
		}
		return new FunctionCallExpression(function, args);
	}

	private Expression parseObject() {
		Map<Expression, Expression> values = new LinkedHashMap<Expression, Expression>();
		int i = 0;
		while (true) {
			Token token = tokenizer.next();

			if (token.getType() == Type.RIGHT_BRACE)
				break;
			tokenizer.undo();

			Expression var = parseExpression();
			token = tokenizer.next();
			if (token.getType() == Type.COLON) {
//				throw new LangError(OBJECT_COLON_EXPECTED);
				values.put(var, parseExpression());
				token = tokenizer.next();
			}
			else {
				values.put(new StringExpression(i + ""), var);
			}
			
			if (token.getType() == Type.RIGHT_BRACE)
				break;
			if (token.getType() != Type.COMMA && token.getType() != Type.SEMICOLON)
				tokenizer.undo();
			
			i++;
		}
		return new ObjectExpression(values);
	}
}
