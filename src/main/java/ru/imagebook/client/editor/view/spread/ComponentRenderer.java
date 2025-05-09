package ru.imagebook.client.editor.view.spread;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.impl.util.SVGUtil;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import ru.imagebook.shared.model.editor.Barcode;
import ru.imagebook.shared.model.editor.ComponentVisitor;
import ru.imagebook.shared.model.editor.Image;
import ru.imagebook.shared.model.editor.ImageLayoutType;
import ru.imagebook.shared.model.editor.Page;
import ru.imagebook.shared.model.editor.Position;
import ru.imagebook.shared.model.editor.SafeArea;
import ru.minogin.core.client.gwt.crypto.UUID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;

public class ComponentRenderer implements ComponentVisitor {
	private static final String SAFE_AREA_COLOR = "blue";

	private final String sessionId;
	private final int left;
	private final int top;
	private final float k;
	private final Page page;
	private final DrawingArea canvas;
	private final int widthPx;
	private final int heightPx;

	public ComponentRenderer(String sessionId, int left, int top, float k,
			Page page, DrawingArea canvas, int widthPx, int heightPx) {
		this.sessionId = sessionId;
		this.left = left;
		this.top = top;
		this.k = k;
		this.page = page;
		this.canvas = canvas;
		this.widthPx = widthPx;
		this.heightPx = heightPx;
	}

	@Override
	public void visit(Image image) {
		String url = GWT.getHostPageBaseURL() + "component?a=" + sessionId + "&b="
				+ image.getId();
		int x = left + (int) (image.getLeft() * k);
		int y = top + (int) (image.getTop() * k);
		int w = (int) (image.getWidth() * k);
		int h = (int) (image.getHeight() * k);
		org.vaadin.gwtgraphics.client.Image xImage = new org.vaadin.gwtgraphics.client.Image(
				x, y, w, h, url);
		canvas.add(xImage);

		if (image.getLayoutType() == ImageLayoutType.BACKGROUND_LEFT
				|| image.getLayoutType() == ImageLayoutType.BACKGROUND_RIGHT) {
			Element clipPath = SVGUtil.createSVGElementNS("clipPath");
			String id = new UUID().toString();
			clipPath.setId(id);
			Element clipRect = SVGUtil.createSVGElementNS("rect");
			clipRect.setAttribute("y", y + "px");
			clipRect.setAttribute("height", h + "px");
			if (image.getLayoutType() == ImageLayoutType.BACKGROUND_LEFT) {
				int w2 = (int) ((image.getWidth() - page.getXMargin()) * k);
				clipRect.setAttribute("x", x + "px");
				clipRect.setAttribute("width", w2 + "px");
			}
			else if (image.getLayoutType() == ImageLayoutType.BACKGROUND_RIGHT) {
				int x2 = left + (int) ((image.getLeft() + page.getYMargin()) * k);
				clipRect.setAttribute("x", x2 + "px");
				clipRect.setAttribute("width", w + "px");
			}

			clipPath.appendChild(clipRect);
			canvas.getElement().getFirstChildElement().appendChild(clipPath);
			xImage.getElement().setAttribute("clip-path", "url(#" + id + ")");
		}

		Rectangle cropRectangle = new Rectangle(left, top, widthPx, heightPx);
		cropRectangle.setStrokeWidth(PageRenderer.BORDER_WIDTH);
		cropRectangle.setStrokeColor(PageRenderer.PAGE_BORDER_COLOR);
		cropRectangle.setFillOpacity(0.0);
		canvas.add(cropRectangle);
	}

	@Override
	public void visit(Barcode barcode) {
		String url = GWT.getHostPageBaseURL() + "component?a=" + sessionId + "&b="
				+ barcode.getId();
		int x = left + (int) (barcode.getLeft() * k);
		int y = top + (int) (barcode.getTop() * k);
		int w = (int) (barcode.getWidth() * k);
		int h = (int) (barcode.getHeight() * k);
		org.vaadin.gwtgraphics.client.Image xImage = new org.vaadin.gwtgraphics.client.Image(
				x, y, w, h, url);
		canvas.add(xImage);
	}

	@Override
	public void visit(Position position) {
		int x = left + (int) (position.getLeft() * k);
		int y = top + (int) (position.getTop() * k);
		int w = (int) (position.getWidth() * k);
		int h = (int) (position.getHeight() * k);
		Rectangle rectangle = new Rectangle(x, y, w, h);
		canvas.add(rectangle);
	}

	@Override
	public void visit(SafeArea safeArea) {
		int x = left + (int) (safeArea.getLeft() * k);
		int y = top + (int) (safeArea.getTop() * k);
		int w = (int) (safeArea.getWidth() * k);
		int h = (int) (safeArea.getHeight() * k);
		Rectangle rectangle = new Rectangle(x, y, w, h);
		rectangle.setStrokeWidth(1);
		rectangle.setStrokeColor(SAFE_AREA_COLOR);
		rectangle.setFillOpacity(0);
		canvas.add(rectangle);
	}
}
