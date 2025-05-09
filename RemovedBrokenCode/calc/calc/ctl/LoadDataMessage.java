package ru.imagebook.client.calc.ctl;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadDataMessage extends BaseMessage {
	private static final long serialVersionUID = 4030807531067502358L;

	public static final String PRODUCT_ID = "productId";

	LoadDataMessage() {}

	public LoadDataMessage(Integer productId) {
		super(CalcMessages.LOAD_DATA);

		addAspects(RemotingAspect.REMOTE);

		set(PRODUCT_ID, productId);
	}

	public Integer getProductId() {
		return get(PRODUCT_ID);
	}
}
