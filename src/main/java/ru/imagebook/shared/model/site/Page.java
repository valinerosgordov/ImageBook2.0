package ru.imagebook.shared.model.site;

import ru.minogin.core.client.bean.EntityBean;

public interface Page extends EntityBean {
	String KEY = "key";
	String NAME = "name";
	String H1 = "h1";
	String TITLE = "title";
	String KEYWORDS = "keywords";
	String DESCRIPTION = "description";
	String CONTENT = "content";
	String WIDE = "wide";
	String URL = "url";
	String TARGET_BLANK = "targetBlank";
	String TAG = "tag";

	String getKey();

	void setKey(String key);

	String getName();

	void setName(String name);

	String getH1();

	void setH1(String h1);

	String getTitle();

	void setTitle(String title);

	String getKeywords();

	void setKeywords(String keywords);

	String getDescription();

	void setDescription(String description);

	String getContent();

	void setContent(String content);

	boolean isWide();

	void setWide(boolean wide);

	String getUrl();

	void setUrl(String url);

	boolean isTargetBlank();

	void setTargetBlank(boolean targetBlank);

	String getFooter();

	void setFooter(String footer);

	Tag getTag();

	void setTag(Tag tag);
}
