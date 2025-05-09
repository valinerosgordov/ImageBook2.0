package ru.imagebook.client.calc.ctl;

import ru.minogin.core.client.flow.BaseMessage;

public class QuantitySelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 5356074803712206571L;

	public static final String QUANTITY = "quantity";

	public QuantitySelectedMessage(int quantity) {
		super(CalcMessages.QUANTITY_SELECTED);

		set(QUANTITY, quantity);
	}

	public int getQuantity() {
		return (Integer) get(QUANTITY);
	}
}
