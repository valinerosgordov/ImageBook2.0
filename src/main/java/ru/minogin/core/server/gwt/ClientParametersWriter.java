package ru.minogin.core.server.gwt;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.ui.Model;

import ru.minogin.core.server.freemarker.FreeMarker;

public class ClientParametersWriter {
	private Map<String, Object> params = new LinkedHashMap<String, Object>();

	public void setParam(String name, Object value) {
		params.put(name, value);
	}

	public String getHtml() {
		String html = "<script language=\"javascript\">\n";
		html += "\tvar clientParameters = {\n";
		for (String name : params.keySet()) {
			html += "\t\t" + name + ": ";
			Object value = params.get(name);
			char t;
			if (value instanceof String)
				t = 's';
			else if (value instanceof Integer)
				t = 'i';
			else if (value instanceof Boolean)
				t = 'b';
			else
				throw new RuntimeException("Unsupported type: " + value);
			html += "\"" + t + ":" + value + "\",\n";
		}
		html += "\t};\n";
		html += "</script>";
		return html;
	}

	public void write(FreeMarker freeMarker) {
		freeMarker.set("clientParameters", getHtml());
	}

	public void write(Model model) {
		model.addAttribute("clientParameters", getHtml());
	}
}
