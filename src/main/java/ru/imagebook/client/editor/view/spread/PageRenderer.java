package ru.imagebook.client.editor.view.spread;

import java.util.Set;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.editor.Component;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;

public class PageRenderer {
	public static final String PAGE_BORDER_COLOR = "grey";
	private static final String PAGE_FILL_COLOR = "white";
	public static final int BORDER_WIDTH = 1;
	private static final String MARGIN_FILL_COLOR = "black";
	private static final double MARGIN_OPACITY = 0.5;

	private DrawingArea canvas;
	private Order<?> order;
	private int pageNumber;
	private String sessionId;
	private float k;

	public PageRenderer(DrawingArea canvas, Order<?> order, int pageNumber, String sessionId, float k) {
		this.canvas = canvas;
		this.order = order;
		this.pageNumber = pageNumber;
		this.sessionId = sessionId;
		this.k = k;
	}

	public void render() {
		canvas.clear();

		Layout layout = order.getLayout();
		Page page = layout.getPages().get(pageNumber);

		int xMargin = (int) (page.getXMargin() * k);
		int left = xMargin;
		int yMargin = (int) (page.getYMargin() * k);
		int top = yMargin;
		float widthMm = page.getWidth() - 2 * page.getXMargin();
		int width = (int) (widthMm * k);
		float heightMm = page.getHeight() - 2 * page.getYMargin();
		int height = (int) (heightMm * k);
		Rectangle rectangle = new Rectangle(left, top, width, height);
		rectangle.setStrokeWidth(BORDER_WIDTH);
		rectangle.setStrokeColor(PAGE_BORDER_COLOR);
		rectangle.setFillColor(PAGE_FILL_COLOR);
		canvas.add(rectangle);

		if (page.isSpreadOrFlyLeafPage()) {
			Line line = new Line(left + width / 2, top, left + width / 2, top + height);
			line.setStrokeWidth(BORDER_WIDTH);
			line.setStrokeColor(PAGE_BORDER_COLOR);
			canvas.add(line);
		}

		ComponentRenderer renderer = new ComponentRenderer(sessionId, left, top, k, page, canvas,
				width, height);
		Set<Component> components = page.getComponents();
		for (Component component : components) {
			component.accept(renderer);
		}

		showEdge(left, top, width, height, xMargin, yMargin);
	}

	private void showEdge(int left, int top, int width, int height, int xMargin, int yMargin) {
		Rectangle rectangle = new Rectangle(left - xMargin, top - yMargin, width + xMargin * 2 + 1,
				yMargin);
		rectangle.setStrokeWidth(0);
		rectangle.setFillColor(MARGIN_FILL_COLOR);
		rectangle.setFillOpacity(MARGIN_OPACITY);
		canvas.add(rectangle);

		rectangle = new Rectangle(left - xMargin, top + height, width + xMargin * 2 + 1, yMargin + 1);
		rectangle.setStrokeWidth(0);
		rectangle.setFillColor(MARGIN_FILL_COLOR);
		rectangle.setFillOpacity(MARGIN_OPACITY);
		canvas.add(rectangle);

		rectangle = new Rectangle(left - xMargin, top, xMargin, height);
		rectangle.setStrokeWidth(0);
		rectangle.setFillColor(MARGIN_FILL_COLOR);
		rectangle.setFillOpacity(MARGIN_OPACITY);
		canvas.add(rectangle);

		rectangle = new Rectangle(left + width, top, xMargin + 1, height);
		rectangle.setStrokeWidth(0);
		rectangle.setFillColor(MARGIN_FILL_COLOR);
		rectangle.setFillOpacity(MARGIN_OPACITY);
		canvas.add(rectangle);
	}
}
