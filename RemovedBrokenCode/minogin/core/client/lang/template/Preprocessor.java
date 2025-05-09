package ru.minogin.core.client.lang.template;

import java.util.ArrayList;
import java.util.List;

import ru.minogin.core.client.lang.template.Part.Type;
import ru.minogin.core.client.text.StringUtil;

public class Preprocessor {
	private List<Part> parts = new ArrayList<Part>();

	public String process(String template) {
		char[] buffer = template.toCharArray();

		int pos = 0, depth = 0;

		Part current = new Part(Type.TEXT);
		parts.add(current);
		while (true) {
			if (pos >= buffer.length)
				break;

			char c = buffer[pos];
			pos++;

			boolean special = false;
			if (current.getType() == Type.TEXT) {
				if (c == Syntax.BEGIN_SCRIPT_1) {
					if (pos < buffer.length && buffer[pos] == Syntax.BEGIN_SCRIPT_2) {
						special = true;
						pos++;
						current = new Part(Type.CODE);
						parts.add(current);
					}
				}
			}
			else if (current.getType() == Type.CODE) {
				if (c == Syntax.BEGIN_SCRIPT_2)
					depth++;
				else if (c == Syntax.END_SCRIPT) {
					if (depth == 0) {
						special = true;
						current = new Part(Type.TEXT);
						parts.add(current);
					}
					else
						depth--;
				}
			}

			if (!special)
				current.getText().append(c);
		}

		StringBuffer out = new StringBuffer();
		for (Part part : parts) {
			if (part.getText().length() > 0) {
				if (part.getType() == Type.TEXT)
					out.append("\"" + StringUtil.escapeQuotes(part.getText().toString()) + "\" ");
				else if (part.getType() == Type.CODE)
					out.append(part.getText() + " ");
			}
		}

		return out.toString().trim();
	}
}
