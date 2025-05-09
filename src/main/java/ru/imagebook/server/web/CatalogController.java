package ru.imagebook.server.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.imagebook.client.common.service.CalcImpl;
import ru.imagebook.server.model.web.Breadcrumb;
import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.site.SiteConfig;
import ru.imagebook.server.service2.web.WebService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Binding;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Paper;
import ru.imagebook.shared.model.ProductType;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.minogin.core.client.collections.CollectionUtil;
import ru.minogin.core.client.collections.Matcher;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.gwt.ClientParametersWriter;

@Controller
public class CatalogController extends MainController {
	private final WebService service;
	private final OrderService orderService;
	private final SiteConfig config;
	private final String url;

	@Autowired
	public CatalogController(WebService service, ServerConfig serverConfig,
			OrderService orderService, SiteConfig config) {
		this.service = service;
		this.orderService = orderService;
		this.config = config;

		String dir = "books";
		url = serverConfig.getWebPrefix() + "/" + dir;
	}

	@Override
	protected void layout(Model model, PageModel page) {
		super.layout(model, page);

		model.addAttribute("catalogUrl", url);

		List<Breadcrumb> breadcrumbs = page.getBreadcrumbs();
		breadcrumbs.add(new Breadcrumb("Главная", "/"));
		breadcrumbs.add(new Breadcrumb("Фотокниги", url));
	}

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String getCatalog(Model model) {
		PageModel page = new PageModel();
		page.setH1("Фотокниги");
		layout(model, page);

		List<DirSection1> sections = service.loadDirSections1();
		model.addAttribute("sections", sections);

		return "catalog";
	}

	@RequestMapping(value = "/books/all", method = RequestMethod.GET)
	public String getAllBooks(Model model) {
		PageModel page = new PageModel();
		String title = "Все фотокниги";
		page.setH1(title);
		layout(model, page);
		List<Breadcrumb> breadcrumbs = page.getBreadcrumbs();
		breadcrumbs.add(new Breadcrumb(title, "/"));

		String locale = Locales.RU;
		PricingData pricingData = orderService.getPricingData();
		List<Color> colorList = service.loadColors();
		Map<Integer, Color> colors = new LinkedHashMap<Integer, Color>();
		for (Color color : colorList) {
			colors.put(color.getNumber(), color);
		}

		List<Album> albums = service.loadAlbums();
		for (Album album : albums) {
			setAlbumText(album, locale, colors, pricingData);
		}
		model.addAttribute("albums", albums);
		model.addAttribute("itemImageUrl", config.getFilesUrl()
				+ "/dir/image/item");

		return "albums";
	}

	@RequestMapping(value = "/books/{key}", method = RequestMethod.GET)
	public String getCatalog2(@PathVariable final String key, Model model) {
		List<DirSection1> sections = service.loadDirSections1();
		model.addAttribute("dirSections", sections);
		DirSection1 section1 = CollectionUtil.find(sections,
				new Matcher<DirSection1>() {
					@Override
					public boolean matches(DirSection1 section) {
						return section.getKey().equals(key);
					}
				});
		section1.set("selected", true);
		model.addAttribute("section1", section1);

		String title = section1.getName();

		PageModel page = new PageModel();
		page.setH1(title);
		layout(model, page);
		List<Breadcrumb> breadcrumbs = page.getBreadcrumbs();
		breadcrumbs.add(new Breadcrumb(title, "/"));

		List<DirSection2> sections2 = service.loadDirSections2(section1);
		section1.set("sections2", sections2);
		model.addAttribute("dirSections2", sections2);
		model.addAttribute("sectionImageUrl", config.getFilesUrl()
				+ "/dir/image/section");

		model.addAttribute("dir", true);

		return "catalog2";
	}

	@RequestMapping(value = "/books/{key2}/{key}", method = RequestMethod.GET)
	public String getCatalog3(@PathVariable final String key, Model model) {
		DirSection2 section2 = service.findDirSection2(key);
		model.addAttribute("section2", section2);
		String title = section2.getName();

		List<DirSection1> sections = service.loadDirSections1();
		model.addAttribute("dirSections", sections);

		DirSection1 section1 = section2.getSection();
		section1 = CollectionUtil.find(sections, section1);
		section1.set("sections2", section1.getSections());

		DirSection2 section2InList = CollectionUtil.find(
				section1.getSections(), section2);
		section2InList.set("selected", true);

		// TODO [gen]
		String locale = Locales.RU;
		PricingData pricingData = orderService.getPricingData();
		List<Color> colorList = service.loadColors();
		Map<Integer, Color> colors = new LinkedHashMap<Integer, Color>();
		for (Color color : colorList) {
			colors.put(color.getNumber(), color);
		}
		for (Album album : section2.getAlbums()) {
			setAlbumText(album, locale, colors, pricingData);
		}

		model.addAttribute("itemImageUrl", config.getFilesUrl()
				+ "/dir/image/item");

		PageModel page = new PageModel();
		page.setH1(title);
		layout(model, page);
		List<Breadcrumb> breadcrumbs = page.getBreadcrumbs();
		breadcrumbs.add(new Breadcrumb(section1.getName(), url + "/"
				+ section1.getKey()));
		breadcrumbs.add(new Breadcrumb(title));

		model.addAttribute("dir", true);

		return "catalog3";
	}

	@RequestMapping(value = "/books/book/{id}", method = RequestMethod.GET)
	public String getBook(@PathVariable final int id, Model model) {
		Album album = service.getAlbum(id);

		// TODO [gen]
		String locale = Locales.RU;
		PricingData pricingData = orderService.getPricingData();
		List<Color> colorList = service.loadColors();
		Map<Integer, Color> colors = new LinkedHashMap<Integer, Color>();
		for (Color color : colorList) {
			colors.put(color.getNumber(), color);
		}
		setAlbumText(album, locale, colors, pricingData);

		model.addAttribute("album", album);

		model.addAttribute("itemImageUrl", config.getFilesUrl()
				+ "/dir/image/item");

		File folder = new File(config.getFilesPath() + "/dir/image/item/" + id);
		String[] files = folder.list();
		if (files != null)
			Arrays.sort(files);
		model.addAttribute("images", files);

		Document document = service.findDocument("dir_flash_" + id);
		if (document != null)
			model.addAttribute("flash", compile(document.getContent()));

		String title = album.getName().get(locale);
		PageModel page = new PageModel();
		page.setH1(title);
		layout(model, page);
		List<Breadcrumb> breadcrumbs = page.getBreadcrumbs();
		breadcrumbs.add(new Breadcrumb(title));

		model.addAttribute("calc", true);
		ClientParametersWriter passer = new ClientParametersWriter();
		passer.setParam("productId", id);

		passer.write(model);

		return "album";
	}

	private void setAlbumText(Album album, String locale,
			Map<Integer, Color> colors, PricingData pricingData) {
		album.set("coverText",
				ProductType.values.get(album.getType()).get(locale));
		album.set("paperText", Paper.values.get(album.getPaper()).get(locale));
		album.set("bindingText",
				Binding.values.get(album.getBinding()).get(locale));

		List<String> coverLams = new ArrayList<String>();
		for (int coverLam : album.getCoverLamRange()) {
			coverLams.add(CoverLamination.values.get(coverLam).get(locale));
		}
		album.set("coverLaminationText", StringUtil.implode(" / ", coverLams));

		List<String> pageLams = new ArrayList<String>();
		for (int pageLam : album.getPageLamRange()) {
			pageLams.add(PageLamination.values.get(pageLam).get(locale));
		}
		album.set("pageLaminationText", StringUtil.implode(" / ", pageLams));

		AlbumOrder order = new AlbumOrderImpl(album);
		int color = album.getColorRange().get(0);
		order.setColor(colors.get(color));
		order.setCoverLamination(album.getCoverLamRange().get(0));
		order.setPageLamination(album.getPageLamRange().get(0));

		order.setPageCount(album.getMinPageCount());
		CalcImpl calc = new CalcImpl(order, pricingData);
		int minPrice;
		if (album.isTrialAlbum())
			minPrice = 0;
		else
			minPrice = calc.getImagebookPrice();
		album.set("minPrice", minPrice);

		order.setPageCount(album.getMaxPageCount());

		List<Integer> pageLamRange = album.getPageLamRange();
		order.setPageLamination(pageLamRange.get(pageLamRange.size() - 1));

		List<Integer> coverLamRange = album.getCoverLamRange();
		order.setCoverLamination(coverLamRange.get(coverLamRange.size() - 1));

		calc = new CalcImpl(order, pricingData);
		album.set("maxPrice", calc.getImagebookPrice());
	}
}
