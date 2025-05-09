package ru.imagebook.server.service;

import static java.util.Collections.singletonList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.imagebook.shared.model.ProductType.getProductTypeName;
import ru.imagebook.client.admin.service.ProductImageRemoteService;
import ru.imagebook.client.common.service.Calc;
import ru.imagebook.client.common.service.CalcImpl;
import ru.imagebook.client.common.service.CostCalculator;
import ru.imagebook.client.common.service.CostCalculatorImpl;
import ru.imagebook.server.model.calc.CalcProduct;
import ru.imagebook.server.model.calc.CalcProductDetail;
import ru.imagebook.server.model.calc.CalcProductPrice;
import ru.imagebook.server.model.calc.CalcProductRequest;
import ru.imagebook.server.model.calc.CalcProductType;
import ru.imagebook.server.repository.CalcRepository;
import ru.imagebook.server.repository.ProductImageRepository;
import ru.imagebook.server.repository.ProductRepository;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Paper;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductImage;
import ru.imagebook.shared.model.ProductType;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.saasengine.server.service.beanstore.BeanStoreService;

public class CalcServiceImpl implements CalcService {
    private static final String DEFAULT_CALC_LOCALE = Locales.RU;
	private static final int MAX_BONUS_LEVEL = 8;

	private final CalcRepository calcRepository;
	private final BeanStoreService beanStoreService;
	private final ProductRepository productRepository;
	// TODO create separate service ProductImageService with common methods
	private final ProductImageRemoteService productImageRemoteService;

	public CalcServiceImpl(CalcRepository calcRepository, BeanStoreService beanStoreService,
						   ProductRepository productRepository, ProductImageRemoteService productImageRemoteService) {
		this.calcRepository = calcRepository;
		this.beanStoreService = beanStoreService;
		this.productRepository = productRepository;
		this.productImageRemoteService = productImageRemoteService;
	}

	@Deprecated
	@Override
	public Map<Integer, List<Product>> loadProductsMap() {
		List<Product> products = calcRepository.loadProductsOld();

		Map<Integer, List<Product>> productsMap = new HashMap<Integer, List<Product>>();
		for (Product product : products) {
			Integer type = product.getType();
			List<Product> typeProducts = productsMap.get(type);
			if (typeProducts == null) {
				typeProducts = new ArrayList<Product>();
				productsMap.put(type, typeProducts);
			}
			typeProducts.add(product);
		}

		return productsMap;
	}

	@Override
    public List<CalcProductType> getProductTypes() {
	    List<CalcProductType> productTypes = new ArrayList<>();
	    for (Integer type : calcRepository.loadProductTypes()) {
			String imageUrl = productImageRemoteService.getProductTypePhotoPath(type);
	        productTypes.add(new CalcProductType(type, getProductTypeName(type, DEFAULT_CALC_LOCALE), imageUrl));
        }
        return productTypes;
    }

	@Override
	public List<CalcProduct> getProductsByType(Integer type) {
	    List<CalcProduct> products = new ArrayList<>();
	    for (Product product : calcRepository.loadProductsByType(type)) {
	        products.add(new CalcProduct(product.getId(), product.getName().get(DEFAULT_CALC_LOCALE)));
        }
        return products;
	}

	@Override
    public List<Integer> getBonusLevels() {
	    List<Integer> bonusLevels = new ArrayList<>();
	    for (int i = 0; i <= MAX_BONUS_LEVEL; i++) {
			bonusLevels.add(i);
        }
        return bonusLevels;
    }

	@Override
	public Product getProductOld(Integer productId) {
		return calcRepository.getProduct(productId);
	}

    @Override
    public CalcProductDetail getProduct(Integer productId) {
        Product product = calcRepository.getProduct(productId);
        List<String> productImages = getProductImages(product);

        return new CalcProductDetail(
            product.getId(),
            product.getName().get(DEFAULT_CALC_LOCALE),
			getCoverLamValues(product.getCoverLamRange()),
            getPageLamValues(product.getPageLamRange()),
            product.getMinPageCount(),
            product.getMaxPageCount(),
            product.getMultiplicity(),
            product.getApproxProdTime(),
			Paper.values.get(product.getPaper()).get(DEFAULT_CALC_LOCALE),
            product.getBlockFormat(),
            product.getCalcComment(),
			productImages
        );
    }

    private List<String> getProductImages(Product product) {
		List<ProductImage> productImages = productImageRemoteService.loadPhotos(product.getId());
		List<String> images = new ArrayList<>();
		for (ProductImage productImage : productImages) {
			images.add(productImage.getPath());
		}
		return images;
	}

	private Map<Integer, String> getCoverLamValues(List<Integer> coverLamRange) {
		Map<Integer, String> coverLamMap = new HashMap<>();
		for (Integer coverLam : coverLamRange) {
			coverLamMap.put(coverLam, CoverLamination.values.get(coverLam).get(DEFAULT_CALC_LOCALE));
		}
		return coverLamMap;
	}

	private Map<Integer, String> getPageLamValues(List<Integer> pageLamRange) {
		Map<Integer, String> pageLamMap = new HashMap<>();
		for (Integer pageLam : pageLamRange) {
			pageLamMap.put(pageLam, PageLamination.values.get(pageLam).get(DEFAULT_CALC_LOCALE));
		}
		return pageLamMap;
	}

    @Override
	public CalcProductPrice computeProductPrice(CalcProductRequest calcRequest) {
		Album album = (Album) calcRepository.getProduct(calcRequest.getProductId());
		AlbumOrder order = new AlbumOrderImpl(album);
		order.setPageCount(calcRequest.getPages());

		// TODO cache
		List<Color> colors = productRepository.loadColors();
		Map<Integer, Color> colorsMap = new HashMap<Integer, Color>();
		for (Color color : colors) {
			colorsMap.put(color.getNumber(), color);
		}
		Integer colorNumber = album.getColorRange().get(0);
		Color color = colorsMap.get(colorNumber);

		order.setColor(color);
		order.setCoverLamination(calcRequest.getCoverLam());
		order.setPageLamination(calcRequest.getPageLam());
		order.setQuantity(calcRequest.getQuantity());

		// TODO cache
		PricingData pricingData = beanStoreService.load(OrderService.PRICING_DATA);

		Calc calc = new CalcImpl(order, pricingData);
		int price = calc.getImagebookPrice();
		int cost = price * calcRequest.getQuantity();
		boolean isSpecialOfferEnabled = album.isSpecialOfferEnabled(calcRequest.getQuantity());
		CostCalculator calculator = new CostCalculatorImpl(calc, calcRequest.getBonusLevel(), 0, 0,
			isSpecialOfferEnabled);

		int total = calculator.calculateCost(order);
		int discount = cost - total;
		int totalPrice = total / calcRequest.getQuantity();

		return new CalcProductPrice(price, cost, discount, total, totalPrice);
	}
}
