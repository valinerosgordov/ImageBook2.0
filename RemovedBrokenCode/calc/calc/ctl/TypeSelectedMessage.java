package ru.imagebook.client.calc.ctl;

import ru.minogin.core.client.flow.BaseMessage;

public class TypeSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -799659971468754000L;

	public static final String PRODUCT_TYPE = "productType";

	public TypeSelectedMessage(int productType) {
		super(CalcMessages.TYPE_SELECTED);

		set(PRODUCT_TYPE, productType);
	}

	public int getProductType() {
		return (Integer) get(PRODUCT_TYPE);
	}
}
