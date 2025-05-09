package ru.imagebook.server.service2.flash;

import java.util.ArrayList;
import java.util.List;

public class FlashXml {
	private int orderId;
	private int width;
	private int height;
	private int pageCount;
	private String hardCover;
	private String title;
	private String orderCode;
	private String flashUrl;
	private int flashWidth;
	private List<FlashPage> pages = new ArrayList<FlashPage>();

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getHardCover() {
		return hardCover;
	}

	public void setHardCover(String hardCover) {
		this.hardCover = hardCover;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<FlashPage> getPages() {
		return pages;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getFlashUrl() {
		return flashUrl;
	}

	public void setFlashUrl(String flashUrl) {
		this.flashUrl = flashUrl;
	}

	public int getFlashWidth() {
		return flashWidth;
	}

	public void setFlashWidth(int flashWidth) {
		this.flashWidth = flashWidth;
	}
}
