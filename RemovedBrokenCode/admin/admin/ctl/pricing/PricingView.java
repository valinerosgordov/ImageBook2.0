package ru.imagebook.client.admin.ctl.pricing;

import java.util.List;

import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.flow.Message;

public interface PricingView {
	void show(PricingData data, List<Color> colors, String locale);

	void confirmLeaving(Message message);
}
