package ru.minogin.ui.client.field;

import com.google.gwt.event.shared.GwtEvent;

public class SearchEvent extends GwtEvent<SearchHandler> {
	public static final Type<SearchHandler> TYPE = new Type<SearchHandler>();

	private final String query;

	public SearchEvent(String query) {
		this.query = query;
	}

	@Override
	public Type<SearchHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SearchHandler handler) {
		handler.onSearch(this);
	}

	public String getQuery() {
		return query;
	}
}
