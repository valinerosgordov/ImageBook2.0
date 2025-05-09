package ru.imagebook.server.ctl;

import java.util.List;

import ru.imagebook.client.admin.ctl.pricing.PricingDataLoadedMessage;
import ru.imagebook.client.admin.ctl.pricing.PricingMessages;
import ru.imagebook.client.admin.ctl.pricing.SavePricingDataMessage;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.ProductService;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.server.service.beanstore.BeanStoreService;

public class PricingController extends Controller {
	private final BeanStoreService beanStoreService;
	private final ProductService productService;

	public PricingController(Dispatcher dispatcher, final BeanStoreService beanStoreService,
			final ProductService productService) {
		super(dispatcher);

		this.beanStoreService = beanStoreService;
		this.productService = productService;
	}

	@Override
	public void registerHandlers() {
		addHandler(PricingMessages.LOAD_PRICING_DATA, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				PricingData pricingData = beanStoreService.load(OrderService.PRICING_DATA);
				if (pricingData == null)
					pricingData = new PricingData();

				List<Color> colors = productService.loadColors();

				send(new PricingDataLoadedMessage(pricingData, colors));
			}
		});

		addHandler(PricingMessages.SAVE_PRICING_DATA, new MessageHandler<SavePricingDataMessage>() {
			@Override
			public void handle(SavePricingDataMessage message) {
				PricingData pricingData = message.getPricingData();

				for (String name : pricingData.getPropertyNames()) {
					Double value = pricingData.get(name);
					if (value == null)
						throw new RuntimeException("Some pricing data is null.");
				}

				beanStoreService.save(OrderService.PRICING_DATA, pricingData);

				Message reply = new BaseMessage(PricingMessages.PRICING_DATA_SAVED);
				reply.addAspects(RemotingAspect.CLIENT);
				sendReply(reply, message);
			}
		});
	}
}
