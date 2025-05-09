package ru.saasengine.client.model.filter;

public class FilterCondition extends Item {
	private static final long serialVersionUID = -5617382687364137575L;

	public static final String TYPE_NAME = "filter.FilterCondition";

	private static final String PATH = "path";
	private static final String OPERATOR = "operator";
	private static final String VALUE = "value";

	FilterCondition() {}

	FilterCondition(FilterCondition prototype) {
		super(prototype);
	}

	public FilterCondition(Joint joint, Path path, Operator operator, Object value) {
		super(joint);

		set(PATH, path);
		set(OPERATOR, operator);
		set(VALUE, value);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public Path getPath() {
		return get(PATH);
	}

	public Operator getOperator() {
		return get(OPERATOR);
	}

	public Object getValue() {
		return get(VALUE);
	}

	public void setValue(Object value) {
		set(VALUE, value);
	}

	@Override
	public <T> T accept(ItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public FilterCondition copy() {
		return new FilterCondition(this);
	}
}
