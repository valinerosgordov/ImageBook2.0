package ru.minogin.pageflip.server;

import ru.minogin.util.server.freemarker.FreeMarker;

public class PageFlip {
	public static final int N_INITIAL_PAGES = 3;

	private String id;
	private final int pageWidth;
	private final int pageHeight;
	private int nPages;
	private boolean hard;
	private String imageUrlPattern;

	public PageFlip(String id, int pageWidth, int pageHeight, int nPages, boolean hard,
			String imageUrlPattern) {
		this.id = id;
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		this.nPages = nPages;
		this.hard = hard;
		this.imageUrlPattern = imageUrlPattern;
	}

	public String render() {
		FreeMarker freeMarker = new FreeMarker(PageFlip.class);
		freeMarker.set("id", "pageflip_" + id);
		freeMarker.set("albumWidth", pageWidth * 2);
		freeMarker.set("albumHeight", pageHeight);
		freeMarker.set("nPages", nPages);
		freeMarker.set("nInitialPages", N_INITIAL_PAGES);
		freeMarker.set("hard", hard);
		freeMarker.set("imageUrlPattern", imageUrlPattern);
		return freeMarker.process("pageflip.ftl");
	}
}
