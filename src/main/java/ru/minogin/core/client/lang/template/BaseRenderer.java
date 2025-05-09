package ru.minogin.core.client.lang.template;

import java.math.BigDecimal;

public class BaseRenderer implements Renderer {
	@Override
	public String render(Object value) {
		if (value == null)
			return "";
		else if (value instanceof String)
			return (String) value;
		else if (value instanceof Integer)
			return value.toString();
		else if (value instanceof Double)
			return value.toString();
		else if (value instanceof Boolean)
			return value.toString();
		else if (value instanceof BigDecimal)
			return value.toString();
		else
			return "";
	}
}
