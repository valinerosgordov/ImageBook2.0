package ru.imagebook.client.admin.ctl.pricing;

import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SavePricingDataMessage extends BaseMessage {
	private static final long serialVersionUID = -869529352672970568L;

	public static final String DATA = "data";

	SavePricingDataMessage() {}

	public SavePricingDataMessage(PricingData data) {
		super(PricingMessages.SAVE_PRICING_DATA);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(DATA, data);
	}

	public PricingData getPricingData() {
		return get(DATA);
	}
}
