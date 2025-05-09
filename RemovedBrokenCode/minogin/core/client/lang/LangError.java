package ru.minogin.core.client.lang;


public class LangError extends RuntimeException {
	private static final long serialVersionUID = 4876325024378806930L;

	private int code;
	private String value;

	@SuppressWarnings("unused")
	private LangError() {}

	public LangError(int code) {
		this(code, null);
	}

	public LangError(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "#" + code + (value != null ? ": " + value : "");
	}
}
