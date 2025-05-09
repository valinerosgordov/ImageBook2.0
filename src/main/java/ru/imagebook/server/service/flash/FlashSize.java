package ru.imagebook.server.service.flash;

import ru.imagebook.shared.model.Album;

public class FlashSize {
	private static final float MM_TO_INCH = 0.0393700787f;
	private static final float MM_TO_PX = MM_TO_INCH * 300f;
	private static final int SMALL_WEB_WIDTH = 220;
	private static final int WEB_WIDTH = 300;

	private int pageWidthPx;
	private int pageHeightPx;
	private int normalWidthPx = 450;
	private int normalHeightPx;
	private int smallHeightPx = 70;
	private int largeWidthPx = 1200;
	private int smallWidthPx;
	private int largeHeightPx;
	private int flashWidthPx;
	private int flashHeightPx;

	public FlashSize(Album album) {
		float margin = 5;

		float width = album.getWidth();
		float pageWidthMm;
		if (album.isSeparateCover())
			pageWidthMm = width - margin - ((float) album.getInnerCrop() * 0.1f);
		else
			pageWidthMm = width - 2 * margin;
		pageWidthPx = (int) (pageWidthMm * MM_TO_PX);

		float height = album.getHeight();
		float pageHeightMm = height - 2 * margin;
		pageHeightPx = (int) (pageHeightMm * MM_TO_PX);

		normalHeightPx = pageHeightPx * normalWidthPx / pageWidthPx;

		smallWidthPx = pageWidthPx * smallHeightPx / pageHeightPx;

		largeHeightPx = pageHeightPx * largeWidthPx / pageWidthPx;

		flashWidthPx = normalWidthPx * 2 + 80;
		flashHeightPx = normalHeightPx + 100;
	}

	public int getPageWidthPx() {
		return pageWidthPx;
	}

	public int getPageHeightPx() {
		return pageHeightPx;
	}

	public int getNormalWidthPx() {
		return normalWidthPx;
	}

	public int getNormalHeightPx() {
		return normalHeightPx;
	}

	public int getSmallWidthPx() {
		return smallWidthPx;
	}

	public int getSmallHeightPx() {
		return smallHeightPx;
	}

	public int getLargeWidthPx() {
		return largeWidthPx;
	}

	public int getLargeHeightPx() {
		return largeHeightPx;
	}

	@Deprecated
	public int getWebWidth(boolean small) {
		if (small)
			return SMALL_WEB_WIDTH;
		else
			return WEB_WIDTH;
	}

	public int getWebWidth() {
		return SMALL_WEB_WIDTH;
	}

	@Deprecated
	public int getWebHeight(boolean small) {
		return pageHeightPx * getWebWidth(small) / pageWidthPx;
	}

	public int getWebHeight() {
		return pageHeightPx * getWebWidth() / pageWidthPx;
	}

	public int getFlashWidthPx() {
		return flashWidthPx;
	}

	public int getFlashHeightPx() {
		return flashHeightPx;
	}
}
