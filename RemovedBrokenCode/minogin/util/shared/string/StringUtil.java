package ru.minogin.util.shared.string;

public class StringUtil {
	public static String trimRight(String s) {
		return s.replaceAll("\\s+$", "");
	}

	public static String formatNumericUnit(int n, String unit1, String unit2, String unit5) {
		return formatNumericUnit(n, new String[] { unit1, unit2, unit5 });
	}

	public static String formatNumericUnit(int n, String[] units) {
		if (n % 10 == 0 || n % 10 > 4 || (n % 100 > 10 && n % 100 < 20))
			return units[2];
		else if (n % 10 == 1)
			return units[0];
		else
			return units[1];
	}
}
