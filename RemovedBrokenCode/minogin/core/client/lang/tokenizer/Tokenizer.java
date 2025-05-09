package ru.minogin.core.client.lang.tokenizer;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.lang.BaseSyntax;
import ru.minogin.core.client.lang.LangError;
import ru.minogin.core.client.lang.tokenizer.Token.Type;

public class Tokenizer {
	public static final int UNEXPECTED_END_OF_STRING = 100;
	public static final int ILLEGAL_CHARACTER = 101;

	private CoreFactory factory;
	private char[] buffer;
	private int pos, previousPos, startPos;
	private int line = 1;
	private Syntax syntax = new BaseSyntax();

	Tokenizer() {}

	public Tokenizer(CoreFactory factory, String s) {
		this(factory, s.toCharArray());
	}

	public Tokenizer(CoreFactory factory, char[] buffer) {
		this.factory = factory;
		this.buffer = buffer;
	}

	public void setSyntax(Syntax syntax) {
		this.syntax = syntax;
	}

	public Token next() {
		previousPos = pos;

		if (pos >= buffer.length)
			return new Token(Type.END);

		while (isWhitespace(buffer[pos])) {
			if (isNewLine(buffer[pos]))
				line++;

			pos++;
			if (pos >= buffer.length)
				return new Token(Type.END);
		}

		char c = buffer[pos];
		startPos = pos;
		pos++;

		if (c == '"' || c == '\'')
			return parseString(c);
		else if (Character.isDigit(c))
			return parseNumber();
		else if (c == '+')
			return new Token(Type.PLUS);
		else if (c == '-')
			return new Token(Type.MINUS);
		else if (c == '*')
			return new Token(Type.MUL);
		else if (c == '=')
			return new Token(Type.EQUALS);
		else if (c == '<') {
			if (pos < buffer.length && buffer[pos] == '=') {
				pos++;
				return new Token(Type.LESS_EQUALS);
			}
			else if (pos < buffer.length && buffer[pos] == '>') {
				pos++;
				return new Token(Type.NOT_EQUALS);
			}
			else
				return new Token(Type.LESS);
		}
		else if (c == '>') {
			if (pos < buffer.length && buffer[pos] == '=') {
				pos++;
				return new Token(Type.GREATER_EQUALS);
			}
			else if (pos < buffer.length && buffer[pos] == '<') {
				pos++;
				return new Token(Type.NOT_EQUALS);
			}
			else
				return new Token(Type.GREATER);
		}
		else if (c == '(')
			return new Token(Type.OPEN_PARENTHESIS);
		else if (c == ')')
			return new Token(Type.CLOSE_PARENTHESIS);
		else if (c == '{')
			return new Token(Type.LEFT_BRACE);
		else if (c == '}')
			return new Token(Type.RIGHT_BRACE);
		else if (c == '[')
			return new Token(Type.LEFT_BRACKET);
		else if (c == ']')
			return new Token(Type.RIGHT_BRACKET);
		else if (c == ',')
			return new Token(Type.COMMA);
		else if (c == '.')
			return new Token(Type.DOT);
		else if (c == ';')
			return new Token(Type.SEMICOLON);
		else if (c == ':')
			return new Token(Type.COLON);
		else
			return parseName();
	}

	public void undo() {
		pos = previousPos;
	}

	private boolean isWhitespace(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	private boolean isNewLine(char c) {
		return c == '\n';
	}

	private Token parseString(char quote) {
		StringBuffer sb = new StringBuffer();
		char c;
		while (true) {
			if (pos >= buffer.length)
				throw new LangError(UNEXPECTED_END_OF_STRING);

			c = buffer[pos];
			pos++;

			if (c == '\\' && pos < buffer.length && buffer[pos] == quote) {
				sb.append(buffer[pos]);
				pos++;
			}
			else if (c != quote)
				sb.append(c);
			else
				return new Token(Type.STRING, sb.toString());
		}
	}

	private Token parseNumber() {
		char c;
		pos = startPos;

		StringBuffer sb = new StringBuffer();
		while (true) {
			if (pos >= buffer.length)
				return new Token(Type.NUMBER, sb.toString());

			c = buffer[pos];

			if (Character.isDigit(c) || c == '.') {
				sb.append(c);
				pos++;
			}
			else
				return new Token(Type.NUMBER, sb.toString());
		}
	}

	private Token parseName() {
		char c;
		pos = startPos;

		StringBuffer sb = new StringBuffer();
		while (true) {
			if (pos >= buffer.length)
				return createNameToken(sb.toString());

			c = buffer[pos];

			if ((sb.length() == 0 && isIdentifierStart(c)) || (sb.length() > 0 && isIdentifierPart(c))) {
				sb.append(c);
				pos++;
			}
			else {
				if (sb.length() == 0)
					throw new LangError(ILLEGAL_CHARACTER, c + "");
				return createNameToken(sb.toString());
			}
		}
	}

	private boolean isIdentifierStart(char c) {
		return isLetter(c) || c == '_' || c == '/';
	}

	private boolean isIdentifierPart(char c) {
		return isIdentifierStart(c) || Character.isDigit(c);
	}

	protected boolean isLetter(char c) {
		return factory.getChar().isLetter(c);
	}

	private Token createNameToken(String name) {
		Type type = syntax.getType(name);
		if (type != null)
			return new Token(type);
		else {
			if (name.length() > 0 && name.charAt(0) == '/') {
				pos -= name.length() - 1;
				return new Token(Type.DIV);
			}
			else
				return new Token(Type.NAME, name);
		}
	}
}
