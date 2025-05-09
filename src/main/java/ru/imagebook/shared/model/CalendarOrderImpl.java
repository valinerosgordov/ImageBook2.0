package ru.imagebook.shared.model;

public class CalendarOrderImpl extends OrderImpl<Calendar> implements CalendarOrder {
	private static final long serialVersionUID = -826825260437658449L;

	CalendarOrderImpl() {}

	public CalendarOrderImpl(Calendar calendar) {
		super(calendar);
	}
}
