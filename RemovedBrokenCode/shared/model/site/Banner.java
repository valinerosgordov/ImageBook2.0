package ru.imagebook.shared.model.site;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Banner extends BaseEntityBean {
	private static final long serialVersionUID = -3247040562272152983L;

	public static final String NAME = "name";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String URL = "url";
	public static final String TARGET_BLANK = "targetBlank";

	public Banner() {
		setTargetBlank(false);
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public String getTitle() {
		return get(TITLE);
	}

	public void setTitle(String title) {
		set(TITLE, title);
	}

	public String getContent() {
		return get(CONTENT);
	}

	public void setContent(String content) {
		set(CONTENT, content);
	}

	public String getUrl() {
		return get(URL);
	}

	public void setUrl(String url) {
		set(URL, url);
	}

	public boolean isTargetBlank() {
		return (Boolean) get(TARGET_BLANK);
	}

	public void setTargetBlank(boolean targetBlank) {
		set(TARGET_BLANK, targetBlank);
	}
}
