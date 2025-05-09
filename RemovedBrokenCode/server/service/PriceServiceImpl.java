package ru.imagebook.server.service;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.model.integration.pricelist.PriceInformation;
import ru.imagebook.server.model.integration.pricelist.PriceList;
import ru.imagebook.server.model.integration.pricelist.Product;
import ru.imagebook.server.repository.PriceRepository;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Paper;
import ru.imagebook.shared.model.ProductType;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.collections.XArrayList;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.locale.Locales;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class PriceServiceImpl implements PriceService {
	private final PriceRepository repository;
	private final OrderService orderService;

	public PriceServiceImpl(PriceRepository repository, OrderService orderService) {
		this.repository = repository;
		this.orderService = orderService;
	}

	@Override
	@Transactional
	public void showPrices(Writer writer, Integer productId) {
		try {
			Configuration config = new Configuration();
			config.setClassForTemplateLoading(getClass(), "");
			config.setObjectWrapper(new DefaultObjectWrapper());
			Locale locale = new Locale("ru", "RU");
			config.setEncoding(locale, "UTF-8");
			config.setDefaultEncoding("UTF-8");
			config.setOutputEncoding("UTF-8");

			User user = new User();
			user.setLevel(0);

			Map<String, Object> root = new HashMap<String, Object>();

			List<Album> albums;
			if (productId == null) {
				albums = repository.loadAlbums();
				root.put("showNavigation", true);
			}
			else {
				albums = new XArrayList<Album>(repository.getAlbum(productId));
				root.put("showNavigation", false);
			}

			List<Color> allColors = repository.loadColors();

			Map<Integer, List<Album>> albumsMap = new LinkedHashMap<Integer, List<Album>>();
			for (Album album : albums) {
				Integer type = album.getType();
				List<Album> typeAlbums = albumsMap.get(type);
				if (typeAlbums == null) {
					typeAlbums = new ArrayList<Album>();
					albumsMap.put(type, typeAlbums);
				}
				typeAlbums.add(album);
			}
			List<Bean> types = new ArrayList<Bean>();
			for (int type : albumsMap.keySet()) {
				Bean typeAlbums = new BaseBean();
				typeAlbums.set("name", ProductType.values.get(type));
				typeAlbums.set("albums", albumsMap.get(type));
				types.add(typeAlbums);
			}
			root.put("types", types);

			for (Album album : albums) {
				album.set("typeName", ProductType.values.get(album.getType()));
				album.set("paperName", Paper.values.get(album.getPaper()));

				int minPageCount = album.getMinPageCount();
				int maxPageCount = album.getMaxPageCount();
				List<Integer> colorRange = album.getColorRange();
				List<Integer> coverLamRange = album.getCoverLamRange();
				List<Integer> pageLamRange = album.getPageLamRange();

				List<Bean> pages1 = new ArrayList<Bean>();
				for (int pageCount = minPageCount; pageCount <= maxPageCount; pageCount += album
						.getMultiplicity()) {
					Bean page = new BaseBean();
					page.set("count", pageCount);
					pages1.add(page);
				}
				album.set("pages", pages1);

				int even = 0;
				List<Bean> colors = new ArrayList<Bean>();
				for (Color c : allColors) {
					if (!colorRange.contains(c.getNumber()))
						continue;

					Bean color = new BaseBean();
					color.set("color", c);

					if (c.getNumber() == 0)
						color.set("colorName", "Индивидуальная");
					else
						color.set("colorName", c.getName().get(Locales.RU));

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

							int[] qs = new int[] { 1, 2, 3, 6, 11, 21, 41, 61, 101, 301 };
							int[] levels = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
							List<Bean> quantities = new ArrayList<Bean>();

							for (int i = 0; i < qs.length - 1; i++) {
								Bean quantity = new BaseBean();
								int q = qs[i];
								quantity.set("minQuantity", q);
								quantity.set("maxQuantity", qs[i + 1] - 1);
								quantity.set("level", levels[i]);

								List<Bean> pages = new ArrayList<Bean>();
								for (int pageCount = minPageCount; pageCount <= maxPageCount; pageCount += album
										.getMultiplicity()) {
									Bean page = new BaseBean();

									Order<?> order = new AlbumOrderImpl(album);
									order.setUser(user);
									order.setColor(c);
									order.setCoverLamination(coverLamNumber);
									order.setPageLamination(pageLamNumber);
									order.setPageCount(pageCount);
									order.setQuantity(q);

									page.set("price", orderService.computeCost(order, q) / q);
									pages.add(page);
								}
								quantity.set("pages", pages);
								quantities.add(quantity);
							}
							pageLam.set("quantities", quantities);
							pageLam.set("even", even);
							even = (even == 0) ? 1 : 0;

							pageLams.add(pageLam);
						}
						coverLam.set("pageLams", pageLams);

						coverLams.add(coverLam);
					}
					color.set("coverLams", coverLams);
					colors.add(color);
				}
				album.set("colors", colors);

			}
			root.put("products", albums);

			Template template = config.getTemplate("price.ftl");

			template.process(root, writer);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@Override
	@Transactional
	public PriceList getPriceListForAlbums(List<Album> albums, String locale) {

		PriceList priceList = new PriceList();
		try {
			List<Color> allColors = repository.loadColors();

			User user = new User();
			user.setLevel(0);

			for (Album album : albums) {
				Product product = new Product();
				product.setBlockFormat(album.getBlockFormat());
				product.setPaperName(Paper.values.get(album.getPaper()).get(locale));
				product.setId(album.getId());
				product.setName(album.getName().get(locale));
				product.setGroupName(ProductType.values.get(album.getType())
						.get(locale));
				product.setGroupId(album.getType());

				int minPageCount = album.getMinPageCount();
				int maxPageCount = album.getMaxPageCount();
				List<Integer> colorRange = album.getColorRange();
				List<Integer> coverLamRange = album.getCoverLamRange();
				List<Integer> pageLamRange = album.getPageLamRange();

				List<Integer> pages1 = new ArrayList<Integer>();
				for (int pageCount = minPageCount; pageCount <= maxPageCount; pageCount += album
						.getMultiplicity()) {
					pages1.add(pageCount);
				}
				product.setPages(pages1);

				int even = 0;
				List<ru.imagebook.server.model.integration.pricelist.Color> colors = new ArrayList<ru.imagebook.server.model.integration.pricelist.Color>();

				for (Color c : allColors) {
					if (!colorRange.contains(c.getNumber()))
						continue;

					ru.imagebook.server.model.integration.pricelist.Color color = new ru.imagebook.server.model.integration.pricelist.Color();
					color.setNumber(c.getNumber());

					if (c.getNumber() == 0) {
						color.setName("Индивидуальная");
					}
					else {
						color.setName(c.getName().get(Locales.RU));
					}

					List<ru.imagebook.server.model.integration.pricelist.CoverLamination> coverLams = new ArrayList<ru.imagebook.server.model.integration.pricelist.CoverLamination>();

					for (int coverLamNumber : CoverLamination.values.keySet()) {
						if (!coverLamRange.contains(coverLamNumber))
							continue;

						ru.imagebook.server.model.integration.pricelist.CoverLamination coverLam = new ru.imagebook.server.model.integration.pricelist.CoverLamination();
						coverLam.setName(CoverLamination.values.get(coverLamNumber).get(
								locale));
						coverLam.setId(coverLamNumber);

						List<ru.imagebook.server.model.integration.pricelist.PageLamination> pageLams = new ArrayList<ru.imagebook.server.model.integration.pricelist.PageLamination>();

						for (int pageLamNumber : PageLamination.values.keySet()) {
							if (!pageLamRange.contains(pageLamNumber))
								continue;

							ru.imagebook.server.model.integration.pricelist.PageLamination pageLam = new ru.imagebook.server.model.integration.pricelist.PageLamination();
							pageLam.setName(PageLamination.values.get(pageLamNumber).get(
									locale));
							pageLam.setId(pageLamNumber);

							int[] qs = new int[] { 1, 2, 3, 6, 11, 21, 41, 61, 101, 301 };
							int[] levels = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
							List<ru.imagebook.server.model.integration.pricelist.Quantity> quantities = new ArrayList<ru.imagebook.server.model.integration.pricelist.Quantity>();

							for (int i = 0; i < qs.length - 1; i++) {
								ru.imagebook.server.model.integration.pricelist.Quantity quantity = new ru.imagebook.server.model.integration.pricelist.Quantity();
								int q = qs[i];
								quantity.setMinQuantity(q);
								quantity.setMaxQuantity(qs[i + 1] - 1);
								quantity.setLevel(levels[i]);

								List<PriceInformation> pages = new ArrayList<PriceInformation>();
								for (int pageCount = minPageCount; pageCount <= maxPageCount; pageCount += album
										.getMultiplicity()) {
									PriceInformation page = new PriceInformation();

									Order<?> order = new AlbumOrderImpl(album);
									order.setUser(user);
									order.setColor(c);
									order.setCoverLamination(coverLamNumber);
									order.setPageLamination(pageLamNumber);
									order.setPageCount(pageCount);
									order.setQuantity(q);

									page.setPageCount(pageCount);
									try {
										page.setPrice(orderService.computeCost(order, q) / q);
									}
									catch (NullPointerException e) {
										e.printStackTrace();
									}

									pages.add(page);
								}
								quantity.setPriceInformation(pages);
								quantities.add(quantity);
							}
							pageLam.setQuantities(quantities);
							// pageLam.set("even", even);
							even = (even == 0) ? 1 : 0;

							pageLams.add(pageLam);
						}
						coverLam.setPageLaminations(pageLams);

						coverLams.add(coverLam);
					}
					color.setCoverLaminations(coverLams);
					colors.add(color);
				}
				product.setColors(colors);
				priceList.addProduct(product);
			}
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}

		return priceList;
	}
}
