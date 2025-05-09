package ru.imagebook.client.editor.view.spread;

import java.util.List;

import org.vaadin.gwtgraphics.client.DrawingArea;

import ru.imagebook.client.editor.ctl.spread.SpreadView;
import ru.imagebook.client.editor.view.EditorWidgets;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteData;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SpreadViewImpl extends View implements SpreadView {
	private static final float WIDTH_PX = 700;
	private static final float OFFSET_PX = 30;

	private final Widgets widgets;

	@Inject
	public SpreadViewImpl(Dispatcher dispatcher, Widgets widgets) {
		super(dispatcher);

		this.widgets = widgets;
	}

	@Override
	public void showSpread(Order<?> order, int pageNumber, String sessionId) {
		final LayoutContainer spreadContainer = widgets.get(EditorWidgets.SPREAD_CONTAINER);
		spreadContainer.removeAll();

		Layout layout = order.getLayout();
		List<Page> pages = layout.getPages();

		float maxWidth = 0;
		for (Page page : pages) {
			if (page.getWidth() > maxWidth)
				maxWidth = page.getWidth();
		}
		float k = WIDTH_PX / maxWidth;

		Page page = pages.get(pageNumber);
		float pageWidth = page.getWidth();
		float pageHeight = page.getHeight();
		int width = (int) (pageWidth * k);
		int height = (int) (pageHeight * k);

		DrawingArea canvas = new DrawingArea(width, height);
		spreadContainer.add(canvas, new AbsoluteData((int) OFFSET_PX, (int) OFFSET_PX));

		spreadContainer.layout();

		PageRenderer renderer = new PageRenderer(canvas, order, pageNumber, sessionId, k);
		renderer.render();
	}

	@Override
	public void hideSpread() {
		final LayoutContainer spreadContainer = widgets.get(EditorWidgets.SPREAD_CONTAINER);
		spreadContainer.removeAll();
	}
}
