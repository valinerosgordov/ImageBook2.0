package ru.minogin.util.shared.math;

public class MathUtil {
	public static int round(double x) {
		return (int) Math.round(x);
	}
	
	public static int roundMoney(double x) {
		return (int) Math.ceil(x);
	}
}
