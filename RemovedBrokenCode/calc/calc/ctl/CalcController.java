package ru.imagebook.client.calc.ctl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.imagebook.client.common.service.Calc;
import ru.imagebook.client.common.service.CalcImpl;
import ru.imagebook.client.common.service.CostCalculator;
import ru.imagebook.client.common.service.CostCalculatorImpl;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.gwt.ClientParametersReader;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CalcController extends Controller {
	private Map<Integer, List<Product>> products;
	private Map<Integer, Color> colorsMap;
	private PricingData pricingData;
	private Integer type;
	private Product product;
	private Color color;
	private Integer coverLam;
	private Integer pageLam;
	private Integer pages;
	private Integer level = 0;
	private Integer quantity = 1;
	private Integer productId;
	private final CalcView view;

	@Inject
	public CalcController(Dispatcher dispatcher, final CalcView view) {
		super(dispatcher);

		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(CalcMessages.START, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				ClientParametersReader passer = new ClientParametersReader();
				productId = passer.getParam("productId");

				view.showCalc(productId);

				send(new LoadDataMessage(productId));
			}
		});

		addHandler(CalcMessages.LOAD_DATA_RESULT,
				new MessageHandler<LoadDataResultMessage>() {
					@Override
					public void handle(LoadDataResultMessage message) {
						products = message.getProducts();
						product = message.getProduct();

						List<Color> colors = message.getColors();
						colorsMap = new HashMap<Integer, Color>();
						for (Color color : colors) {
							colorsMap.put(color.getNumber(), color);
						}

						pricingData = message.getPricingData();

						if (productId == null) {
							List<Integer> types = new ArrayList<Integer>();
							for (int type : ProductType.values.keySet()) {
								if (products.get(type) != null)
									types.add(type);
							}
							view.showForm(types, productId);
						}
						else {
							view.showForm(null, productId);
							send(new ProductSelectedMessage(product));
						}
					}
				});

		addHandler(CalcMessages.TYPE_SELECTED,
				new MessageHandler<TypeSelectedMessage>() {
					@Override
					public void handle(TypeSelectedMessage message) {
						type = message.getProductType();
						List<Product> typeProducts = products.get(type);
						view.showProductField(typeProducts);
					}
				});

		addHandler(CalcMessages.PRODUCT_SELECTED,
				new MessageHandler<ProductSelectedMessage>() {
					@Override
					public void handle(ProductSelectedMessage message) {
						product = message.getProduct();
						pages = product.getMinPageCount();
						Integer colorNumber = product.getColorRange().get(0);
						color = colorsMap.get(colorNumber);
						coverLam = product.getCoverLamRange().get(0);
						pageLam = product.getPageLamRange().get(0);

						view.showOtherFields(product, colorsMap, color, coverLam, pageLam,
								quantity);

						send(CalcMessages.CALCULATE_PRICE);
					}
				});

		addHandler(CalcMessages.PAGES_SELECTED,
				new MessageHandler<PagesSelectedMessage>() {
					@Override
					public void handle(PagesSelectedMessage message) {
						pages = message.getPages();

						send(CalcMessages.CALCULATE_PRICE);
					}
				});

		addHandler(CalcMessages.LEVEL_SELECTED,
				new MessageHandler<LevelSelectedMessage>() {
					@Override
					public void handle(LevelSelectedMessage message) {
						level = message.getLevel();

						send(CalcMessages.CALCULATE_PRICE);
					}
				});

		addHandler(CalcMessages.COLOR_SELECTED,
				new MessageHandler<ColorSelectedMessage>() {
					@Override
					public void handle(ColorSelectedMessage message) {
						color = message.getColor();

						send(CalcMessages.CALCULATE_PRICE);
					}
				});

		addHandler(CalcMessages.COVER_LAM_SELECTED,
				new MessageHandler<CoverLamSelectedMessage>() {
					@Override
					public void handle(CoverLamSelectedMessage message) {
						coverLam = message.getCoverLam();

						send(CalcMessages.CALCULATE_PRICE);
					}
				});

		addHandler(CalcMessages.PAGE_LAM_SELECTED,
				new MessageHandler<PageLamSelectedMessage>() {
					@Override
					public void handle(PageLamSelectedMessage message) {
						pageLam = message.getPageLam();

						send(CalcMessages.CALCULATE_PRICE);
					}
				});

		addHandler(CalcMessages.QUANTITY_SELECTED,
				new MessageHandler<QuantitySelectedMessage>() {
					@Override
					public void handle(QuantitySelectedMessage message) {
						quantity = message.getQuantity();

						send(CalcMessages.CALCULATE_PRICE);
					}
				});

		addHandler(CalcMessages.CALCULATE_PRICE, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (pages != null && color != null && coverLam != null
						&& pageLam != null && quantity != null) {
					if (product instanceof Album) {
						Album album = (Album) product;
						AlbumOrder order = new AlbumOrderImpl(album);
						order.setPageCount(pages);
						order.setColor(color);
						order.setCoverLamination(coverLam);
						order.setPageLamination(pageLam);
						order.setQuantity(quantity);
						Calc calc = new CalcImpl(order, pricingData);
						int price = calc.getImagebookPrice();
						int cost = price * quantity;
						CostCalculator calculator = new CostCalculatorImpl(calc, level, 0, 0,
						    album.isSpecialOfferEnabled(quantity));
						int total = calculator.calculateCost(order);
						int discount = cost - total;

						view.showPrice(product, price, quantity, cost, discount, total,
								productId);
					}
					else
						throw new RuntimeException("Unsupported product");
				}
				else
					view.hidePrice();
			}
		});
	}
}