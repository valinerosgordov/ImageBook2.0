package ru.minogin.util.shared.string;

import java.util.HashMap;
import java.util.Map;

public class Transliteration {
	public static String toIdentifier(String s) {
		if (s == null)
			return null;

		Map<Character, String> rules = new HashMap<Character, String>();
		rules.put('а', "a");
		rules.put('б', "b");
		rules.put('в', "v");
		rules.put('г', "g");
		rules.put('д', "d");
		rules.put('е', "e");
		rules.put('ё', "yo");
		rules.put('ж', "zh");
		rules.put('з', "z");
		rules.put('и', "i");
		rules.put('й', "y");
		rules.put('к', "k");
		rules.put('л', "l");
		rules.put('м', "m");
		rules.put('н', "n");
		rules.put('о', "o");
		rules.put('п', "p");
		rules.put('р', "r");
		rules.put('с', "s");
		rules.put('т', "t");
		rules.put('у', "u");
		rules.put('ф', "f");
		rules.put('х', "h");
		rules.put('ц', "ts");
		rules.put('ч', "ch");
		rules.put('ш', "sh");
		rules.put('щ', "sh");
		rules.put('ь', "");
		rules.put('ы', "y");
		rules.put('ъ', "");
		rules.put('э', "e");
		rules.put('ю', "u");
		rules.put('я', "ya");

		rules.put('А', "A");
		rules.put('Б', "B");
		rules.put('В', "V");
		rules.put('Г', "G");
		rules.put('Д', "D");
		rules.put('Е', "E");
		rules.put('Ё', "Yo");
		rules.put('Ж', "Zh");
		rules.put('З', "Z");
		rules.put('И', "I");
		rules.put('Й', "Y");
		rules.put('К', "K");
		rules.put('Л', "L");
		rules.put('М', "M");
		rules.put('Н', "N");
		rules.put('О', "O");
		rules.put('П', "P");
		rules.put('Р', "R");
		rules.put('С', "S");
		rules.put('Т', "T");
		rules.put('У', "U");
		rules.put('Ф', "F");
		rules.put('Х', "H");
		rules.put('Ц', "Ts");
		rules.put('Ч', "Ch");
		rules.put('Ш', "Sh");
		rules.put('Щ', "Sh");
		rules.put('Ь', "");
		rules.put('Ы', "Y");
		rules.put('Ъ', "");
		rules.put('Э', "E");
		rules.put('Ю', "U");
		rules.put('Я', "Ya");

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			String replacement = rules.get(c);
			if (replacement != null)
				sb.append(replacement);
			else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9'))
				sb.append(c);
			else
				sb.append("_");
		}
		return sb.toString();
	}
}
