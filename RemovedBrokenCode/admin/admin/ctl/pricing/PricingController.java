package ru.imagebook.client.admin.ctl.pricing;

import java.util.List;

import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PricingController extends Controller {
	private final PricingView view;
	private final I18nService i18nService;

	@Inject
	public PricingController(Dispatcher dispatcher, final PricingView view,
			final I18nService i18nService) {
		super(dispatcher);

		this.view = view;
		this.i18nService = i18nService;
	}

	@Override
	public void registerHandlers() {
		addHandler(PricingMessages.EDIT_PRICING_DATA, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				BaseMessage loadMessage = new BaseMessage(PricingMessages.LOAD_PRICING_DATA);
				loadMessage.addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
				send(loadMessage);
			}
		});

		addHandler(PricingMessages.PRICING_DATA_LOADED, new MessageHandler<PricingDataLoadedMessage>() {
			@Override
			public void handle(PricingDataLoadedMessage message) {
				PricingData pricingData = message.getPricingData();
				List<Color> colors = message.getColors();

				view.show(pricingData, colors, i18nService.getLocale());
			}
		});
	}
}
