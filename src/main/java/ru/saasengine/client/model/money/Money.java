package ru.saasengine.client.model.money;

import java.math.BigDecimal;

import ru.minogin.core.client.bean.BasePersistentBean;

public class Money extends BasePersistentBean {
	private static final long serialVersionUID = -1939606223650886686L;

	public static final String TYPE_NAME = "money.Money";

	private static final String VALUE = "value";
	private static final String CURRENCY_ID = "currencyId";

	Money() {}
	
	Money(Money prototype) {
		super(prototype);
	}

	public Money(double value, int currencyId) {
		this(new BigDecimal(value), currencyId);
	}

	public Money(BigDecimal value, int currencyId) {
		if (value == null || currencyId == 0)
			throw new NullPointerException();

		setValue(value);
		setCurrencyId(currencyId);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public BigDecimal getValue() {
		return get(VALUE);
	}

	public Integer getCurrencyId() {
		return get(CURRENCY_ID);
	}

	public void setValue(BigDecimal value) {
		set(VALUE, value);
	}

	public void setCurrencyId(int currencyId) {
		set(CURRENCY_ID, currencyId);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Money))
			return false;

		Money money = (Money) obj;
		return money.getValue().equals(getValue()) && money.getCurrencyId() == getCurrencyId();
	}

	@Override
	public int hashCode() {
		return getValue().hashCode() ^ getCurrencyId();
	}

	@Override
	public Money copy() {
		return new Money(this);
	}
}
