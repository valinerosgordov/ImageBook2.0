package ru.minogin.core.server.gxt;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.fckeditor.FCKeditor;
import ru.minogin.core.client.gxt.form.HtmlFieldService;
import ru.minogin.core.server.spring.SpringUtil;

public class HtmlFieldServiceImpl implements HtmlFieldService {
	private static final long serialVersionUID = 2028353550506263383L;

	@Override
	public String getEditor(String value) {
		String uuid = UUID.randomUUID().toString();
		String id = "html_field_" + uuid.replace("-", "");

		HttpServletRequest request = SpringUtil.getRequest();
		FCKeditor editor = new FCKeditor(request, id);
		if (value == null)
			value = "";
		editor.setValue(value);
		return editor.createHtml();
	}
}
