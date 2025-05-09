package ru.imagebook.server.model.web;

import java.util.ArrayList;
import java.util.List;

public class PageModel {
	private String title;
	private String keywords;
	private String description;
	private String h1;
	private boolean wide = false;
	private List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();
	private String footer;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getH1() {
		return h1;
	}

	public void setH1(String h1) {
		this.h1 = h1;
	}

	public boolean isWide() {
		return wide;
	}

	public void setWide(boolean wide) {
		this.wide = wide;
	}

	public List<Breadcrumb> getBreadcrumbs() {
		return breadcrumbs;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}
}
