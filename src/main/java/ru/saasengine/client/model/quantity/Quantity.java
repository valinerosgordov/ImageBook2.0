package ru.saasengine.client.model.quantity;

import java.math.BigDecimal;

import ru.minogin.core.client.bean.BasePersistentBean;

public class Quantity extends BasePersistentBean {
	private static final long serialVersionUID = -3963889325327418247L;

	public static final String TYPE_NAME = "quantity.Quantity";

	private static final String VALUE = "value";
	private static final String UNIT_ID = "unitId";

	Quantity() {}

	Quantity(Quantity prototype) {
		super(prototype);
	}

	public Quantity(BigDecimal value, int unitId) {
		if (value == null || unitId == 0)
			throw new NullPointerException();

		set(VALUE, value);
		set(UNIT_ID, unitId);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public BigDecimal getValue() {
		return get(VALUE);
	}

	public int getUnitId() {
		return (Integer) get(UNIT_ID);
	}

	public void setUnitId(int unitId) {
		set(UNIT_ID, unitId);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Quantity))
			return false;

		Quantity quantity = (Quantity) obj;
		return quantity.getValue().equals(getValue()) && quantity.getUnitId() == getUnitId();
	}

	@Override
	public int hashCode() {
		return getValue().hashCode() ^ getUnitId();
	}

	@Override
	public Quantity copy() {
		return new Quantity(this);
	}
}
