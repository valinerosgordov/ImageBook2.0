package ru.imagebook.server.service;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.exception.Exceptions;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class PHPriceServiceImpl implements PHPriceService {
	private final OrderService orderService;
	private final ProductService productService;

	public PHPriceServiceImpl(OrderService orderService, ProductService productService) {
		this.orderService = orderService;
		this.productService = productService;
	}

	@Override
	@Transactional
	public void showPrices(Writer writer) {
		try {
			Configuration config = new Configuration();
			config.setClassForTemplateLoading(getClass(), "");
			config.setObjectWrapper(new DefaultObjectWrapper());
			Locale locale = new Locale("ru", "RU");
			config.setEncoding(locale, "UTF-8");
			config.setDefaultEncoding("UTF-8");
			config.setOutputEncoding("UTF-8");

			Map<String, Object> root = new HashMap<String, Object>();

			List<Album> products = productService.loadAlbums();
			List<Color> allColors = productService.loadColors();

			for (Product product : products) {
				Album album = (Album) product;

				product.set("typeName", ProductType.values.get(product.getType()));

				int minPageCount = product.getMinPageCount();
				int maxPageCount = product.getMaxPageCount();
				List<Integer> colorRange = product.getColorRange();
				List<Integer> coverLamRange = product.getCoverLamRange();
				List<Integer> pageLamRange = product.getPageLamRange();

				List<Bean> pages1 = new ArrayList<Bean>();
				for (int pageCount = minPageCount; pageCount <= maxPageCount; pageCount += product
						.getMultiplicity()) {
					Bean page = new BaseBean();
					page.set("count", pageCount);
					pages1.add(page);
				}
				product.set("pages", pages1);

				List<Bean> colors = new ArrayList<Bean>();
				for (Color c : allColors) {
					if (!colorRange.contains(c.getNumber()))
						continue;

					Bean color = new BaseBean();
					color.set("color", c);

					List<Bean> coverLams = new ArrayList<Bean>();
					for (int coverLamNumber : CoverLamination.values.keySet()) {
						if (!coverLamRange.contains(coverLamNumber))
							continue;

						Bean coverLam = new BaseBean();
						coverLam.set("name", CoverLamination.values.get(coverLamNumber));

						List<Bean> pageLams = new ArrayList<Bean>();
						for (int pageLamNumber : PageLamination.values.keySet()) {
							if (!pageLamRange.contains(pageLamNumber))
								continue;

							Bean pageLam = new BaseBean();
							pageLam.set("name", PageLamination.values.get(pageLamNumber));

							List<Bean> pages = new ArrayList<Bean>();
							for (int pageCount = minPageCount; pageCount <= maxPageCount; pageCount += product
									.getMultiplicity()) {
								Bean page = new BaseBean();

								Order<?> order = new AlbumOrderImpl(album);
								order.setColor(c);
								order.setCoverLamination(coverLamNumber);
								order.setPageLamination(pageLamNumber);
								order.setPageCount(pageCount);

								page.set("price", orderService.computePhPrice(order));
								pages.add(page);
							}
							pageLam.set("pages", pages);

							pageLams.add(pageLam);
						}
						coverLam.set("pageLams", pageLams);

						coverLams.add(coverLam);
					}
					color.set("coverLams", coverLams);
					colors.add(color);
				}
				product.set("colors", colors);
			}
			root.put("products", products);

			Template template = config.getTemplate("phPrice.ftl");

			template.process(root, writer);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
