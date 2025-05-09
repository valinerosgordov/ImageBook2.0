package ru.imagebook.server.ctl;

import java.util.List;
import java.util.Map;

import ru.imagebook.client.calc.ctl.CalcMessages;
import ru.imagebook.client.calc.ctl.LoadDataMessage;
import ru.imagebook.client.calc.ctl.LoadDataResultMessage;
import ru.imagebook.server.service.CalcService;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.ProductService;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.server.service.beanstore.BeanStoreService;

public class CalcController extends Controller {
	private final CalcService service;
	private final ProductService productService;
	private final BeanStoreService beanStoreService;

	public CalcController(Dispatcher dispatcher, final CalcService service,
			final ProductService productService,
			final BeanStoreService beanStoreService) {
		super(dispatcher);

		this.service = service;
		this.productService = productService;
		this.beanStoreService = beanStoreService;
	}

	@Override
	public void registerHandlers() {
		addHandler(CalcMessages.LOAD_DATA, new MessageHandler<LoadDataMessage>() {
			@Override
			public void handle(LoadDataMessage message) {
				Map<Integer, List<Product>> products;
				Integer productId = message.getProductId();
				List<Color> colors = productService.loadColors();
				PricingData pricingData = beanStoreService.load(OrderService.PRICING_DATA);
				if (productId != null) {
					Product product = service.getProductOld(productId);
					send(new LoadDataResultMessage(product, colors, pricingData));
				}
				else {
					products = service.loadProductsMap();
					send(new LoadDataResultMessage(products, colors, pricingData));
				}
			}
		});
	}
}
