package ru.minogin.core.client.lang.tokenizer;

public class Token {
	public enum Type {
		STRING,
		NUMBER,
		
		TRUE,
		FALSE,

		PLUS,
		MINUS,
		MUL,
		DIV,
		EQUALS,
		NOT_EQUALS,
		LESS,
		GREATER,
		LESS_EQUALS,
		GREATER_EQUALS,
		OPEN_PARENTHESIS,
		CLOSE_PARENTHESIS,
		LEFT_BRACE,
		RIGHT_BRACE,
		LEFT_BRACKET,
		RIGHT_BRACKET,
		COLON,
		DOT,
		COMMA,
		SEMICOLON,

		NOT,
		AND,
		OR,

		IF,
		ELSE,
		ENDIF,

		WHILE,
		ENDWHILE,

		FOR,
		ENDFOR,

		NAME,

		END;
	}

	private Type type;
	private String value;

	public Token(Type type) {
		this(type, null);
	}

	public Token(Type type, String value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Token))
			return false;

		Token token = (Token) obj;
		return token.type == type && (token.value != null ? token.value.equals(value) : value == null);
	}

	@Override
	public int hashCode() {
		return type.hashCode() ^ (value != null ? value.hashCode() : 0);
	}

	@Override
	public String toString() {
		return type + ":" + value;
	}
}
