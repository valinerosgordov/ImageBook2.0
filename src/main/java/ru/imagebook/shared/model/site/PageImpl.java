package ru.imagebook.shared.model.site;

import ru.minogin.core.client.bean.BaseEntityBean;

public class PageImpl extends BaseEntityBean implements Page {
	private static final long serialVersionUID = -3494698178820528517L;

	private String footer;

	public PageImpl() {
		setWide(false);
		setTargetBlank(false);
	}

	@Override
	public String getKey() {
		return get(KEY);
	}

	@Override
	public void setKey(String key) {
		set(KEY, key);
	}

	@Override
	public String getName() {
		return get(NAME);
	}

	@Override
	public void setName(String name) {
		set(NAME, name);
	}

	@Override
	public String getH1() {
		return get(H1);
	}

	@Override
	public void setH1(String h1) {
		set(H1, h1);
	}

	@Override
	public String getTitle() {
		return get(TITLE);
	}

	@Override
	public void setTitle(String title) {
		set(TITLE, title);
	}

	@Override
	public String getKeywords() {
		return get(KEYWORDS);
	}

	@Override
	public void setKeywords(String keywords) {
		set(KEYWORDS, keywords);
	}

	@Override
	public String getDescription() {
		return get(DESCRIPTION);
	}

	@Override
	public void setDescription(String description) {
		set(DESCRIPTION, description);
	}

	@Override
	public String getContent() {
		return get(CONTENT);
	}

	@Override
	public void setContent(String content) {
		set(CONTENT, content);
	}

	@Override
	public boolean isWide() {
		return (Boolean) get(WIDE);
	}

	@Override
	public void setWide(boolean wide) {
		set(WIDE, wide);
	}

	@Override
	public String getUrl() {
		return get(URL);
	}

	@Override
	public void setUrl(String url) {
		set(URL, url);
	}

	@Override
	public boolean isTargetBlank() {
		return (Boolean) get(TARGET_BLANK);
	}

	@Override
	public void setTargetBlank(boolean targetBlank) {
		set(TARGET_BLANK, targetBlank);
	}

	@Override
	public String getFooter() {
		return footer;
	}

	@Override
	public void setFooter(String footer) {
		this.footer = footer;
	}

	@Override
	public Tag getTag() {
		return get(TAG);
	}

	@Override
	public void setTag(Tag tag) {
		set(TAG, tag);
	}
}
