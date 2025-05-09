package ru.imagebook.client.admin.ctl.pricing;

import java.util.List;

import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class PricingDataLoadedMessage extends BaseMessage {
	private static final long serialVersionUID = -727348896844868363L;

	public static final String DATA = "data";
	public static final String COLORS = "colors";

	PricingDataLoadedMessage() {}

	public PricingDataLoadedMessage(PricingData data, List<Color> colors) {
		super(PricingMessages.PRICING_DATA_LOADED);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(DATA, data);
		set(COLORS, colors);
	}

	public PricingData getPricingData() {
		return get(DATA);
	}

	public List<Color> getColors() {
		return get(COLORS);
	}
}
