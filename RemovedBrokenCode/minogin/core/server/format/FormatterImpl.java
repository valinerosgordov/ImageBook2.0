package ru.minogin.core.server.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ru.minogin.core.client.format.Formatter;

public class FormatterImpl implements Formatter {
	@Override
	public String n2(int n) {
		NumberFormat format = new DecimalFormat("00");
		return format.format(n);
	}
}
