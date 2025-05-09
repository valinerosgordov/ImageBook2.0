package ru.minogin.core.client.util;

public class MathUtil {
	public static int round10(double d) {
		d = d / 10.0;
		int i = (int) Math.round(d);
		return i * 10;
	}
}
