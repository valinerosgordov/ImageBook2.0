package ru.minogin.ui.client.list;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;

import java.util.Collection;

public class DynamicList<T> extends ActiveList<T> {
	private static final int WINDOW_SCROLL_DELTA_PX = 200;

	private final DynamicLoader<T> loader;
	private final int limit;

	private int offset;
	private boolean loading;

	public DynamicList(ActiveListRenderer<T> renderer, final DynamicLoader<T> loader, final int limit) {
		super(renderer);

		this.loader = loader;
		this.limit = limit;

		this.offset = 0;
		Window.addWindowScrollHandler(new ScrollHandler() {
			@Override
			public void onWindowScroll(ScrollEvent event) {
				int scrollTop = event.getScrollTop();
				int scrollBottom = scrollTop + Window.getClientHeight();
				int scrollHeight = Document.get().getScrollHeight();
				if (scrollHeight - scrollBottom < WINDOW_SCROLL_DELTA_PX) {
					if (loading)
						return;
					loading = true;

					offset += limit;
					loader.load(offset, limit, new DynamicLoadCallback<T>() {
						@Override
						public void onNextValuesLoaded(Collection<T> values) {
							addValues(values);
							loading = false;
						}
					});
				}
			}
		});
	}

	public void showValues() {
		if (loading)
			return;
		loading = true;

		offset = 0;
		loader.load(offset, limit, new DynamicLoadCallback<T>() {
			@Override
			public void onNextValuesLoaded(Collection<T> values) {
				showValues(values);
				loading = false;
			}
		});
	}
}
