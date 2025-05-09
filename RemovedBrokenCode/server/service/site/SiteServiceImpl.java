package ru.imagebook.server.service.site;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;

import ru.imagebook.client.common.service.CalcImpl;
import ru.imagebook.server.repository.SiteRepository;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.flash.FlashService;
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
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.DocumentImpl;
import ru.imagebook.shared.model.site.Folder;
import ru.imagebook.shared.model.site.Page;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.SectionImpl;
import ru.imagebook.shared.model.site.TopSection;
import ru.imagebook.shared.model.site.TopSectionImpl;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.gwt.ClientParametersWriter;

public class SiteServiceImpl implements SiteService {
	private static final String CONTENT = "content_";

	private final SiteRepository repository;
	private final SiteConfig siteConfig;
	private final FlashService flashService;
	private final OrderService orderService;
	private final ServerConfig serverConfig;
	private final CoreFactory coreFactory;

	public SiteServiceImpl(SiteRepository repository, SiteConfig siteConfig, FlashService flashService,
			OrderService orderService, ServerConfig serverConfig, CoreFactory coreFactory) {
		this.repository = repository;
		this.siteConfig = siteConfig;
		this.flashService = flashService;
		this.orderService = orderService;
		this.serverConfig = serverConfig;
		this.coreFactory = coreFactory;
	}

	@Override
	public Section loadSections() {
		Section root = null;
		Map<Section, List<Section>> map = new HashMap<Section, List<Section>>();
		List<Section> sections = repository.loadSections();
		for (Section section : sections) {
			Section parent = section.getParent();
			if (parent != null) {
				List<Section> parentSections = map.get(parent);
				if (parentSections == null) {
					parentSections = new ArrayList<Section>();
					map.put(parent, parentSections);
				}
				parentSections.add(section);
			}
			else
				root = section;
		}

		if (root == null) {
			root = new SectionImpl();
			repository.save(root);
		}

		addChildren(root, map);

		return root;
	}

	private void addChildren(Section section, Map<Section, List<Section>> map) {
		List<Section> children = map.get(section);
		if (children != null) {
			section.getChildren().addAll(children);
			for (Section child : children) {
				addChildren(child, map);
			}
		}
	}

	@Override
	public void saveSection(Section prototype) {
		Section section = repository.getSection(prototype.getId());
		section.setNumber(prototype.getNumber());
		fetch(section, prototype);
		section.setHidden(prototype.isHidden());
		try {
			repository.flush();
		}
		catch (DataIntegrityViolationException e) {
			throw new KeyExistsError();
		}
	}

	private void fetch(Page page, Page prototype) {
		page.setKey(prototype.getKey());
		page.setName(prototype.getName());
		page.setTitle(prototype.getTitle());
		page.setKeywords(prototype.getKeywords());
		page.setDescription(prototype.getDescription());
		page.setH1(prototype.getH1());
		page.setWide(prototype.isWide());
		page.setUrl(prototype.getUrl());
		page.setTargetBlank(prototype.isTargetBlank());
		page.setFooter(prototype.getFooter());
		page.setTag(prototype.getTag());
		String content = prototype.getContent();
		content = removeExtraBr(content);
		page.setContent(content);
	}

	private String removeExtraBr(String content) {
		return content.replace("<br></p>", "</p>");
	}

	@Override
	public void addSection(int parentId) {
		Section parent = repository.getSection(parentId);
		SortedSet<Section> children = parent.getChildren();
		int number;
		if (!children.isEmpty())
			number = children.last().getNumber() + 100;
		else
			number = 100;

		Section section = new SectionImpl();
		section.setLevel(parent.getLevel() + 1);
		section.setNumber(number);
		section.setParent(parent);
		section.setKey("");
		section.setName("");
		section.setTitle("");
		section.setH1("");
		try {
			repository.save(section);
		}
		catch (DataIntegrityViolationException e) {
			throw new KeyExistsError();
		}
	}

	@Override
	public void deleteSections(List<Integer> ids) {
		for (int id : ids) {
			deleteSection(id);
		}
	}

	private void deleteSection(int id) {
		Section section = repository.getSection(id);
		if (section.isRoot())
			throw new RootSectionDeleteError();

		try {
			repository.deleteSection(section);
		}
		catch (DataIntegrityViolationException e) {
			throw new ChildExistsError();
		}
	}

	@Override
	public void showPage(String uri, Writer writer, HttpServletResponse response) {
		FreeMarker freeMarker = new FreeMarker(getClass());

		String prefix = serverConfig.getWebPrefix();
		freeMarker.set("prefix", prefix);
		uri = uri.trim();
		uri = uri.replace(siteConfig.getInnerPrefix(), "");

		String key;
		String[] parts = uri.split("/");
		if (parts.length == 0 || parts.length == 1)
			key = "index";
		else
			key = parts[parts.length - 1];

		if (key.equals("newyear")) {
			try {
				response.sendRedirect("http://imagebook.ru/new_year");
			}
			catch (Exception e) {
				Exceptions.rethrow(e);
			}
			return;
		}

		freeMarker.set("index", false);

		List<TopSection> topSections = repository.loadTopSections();
		for (TopSection topSection : topSections) {
			String url = topSection.getUrl() != null ? topSection.getUrl() : prefix + "/"
					+ topSection.getKey();
			topSection.set("url_", url);
		}
		freeMarker.set("topSections", topSections);

		List<Banner> banners = repository.loadPageBanners();
		Collections.reverse(banners);
		freeMarker.set("banners", banners);

		Section root = repository.getRootSection();
		List<Section> sections1 = repository.loadSections(root);
		for (Section s : sections1) {
			String url = s.getUrl() != null ? s.getUrl() : prefix + "/" + s.getKey();
			s.set("url_", url);
		}
		freeMarker.set("sections1", sections1);

		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Главная", "/"));
		freeMarker.set("links", links);

		if (parts.length > 1 && parts[1].equals("books")) {
			showDir(freeMarker, parts, writer, links);
			return;
		}

		Page page = repository.getSection(key);
		if (page == null)
			page = repository.getTopSection(key);
		if (page == null)
			page = repository.getDocument(key);
		if (page == null)
			throw new PageNotFoundError();

		page.set(CONTENT, compile(page.getContent()));

		freeMarker.set("page", page);

		if (page instanceof Section) {
			Section section = (Section) page;
			int level = section.getLevel();
			freeMarker.set("level", level);
			if (level == 0)
				freeMarker.set("index", true);
			else {
				if (level == 1) {
					List<Section> sections2 = repository.loadSections(section);
					freeMarker.set("sections2", sections2);

					links.add(new Link(section.getName(), section.getKey()));
				}
				else if (level == 2) {
					Section parent = section.getParent();
					List<Section> sections2 = repository.loadSections(parent);
					for (Section iSection : sections2) {
						if (iSection.equals(section))
							iSection.set("selected", true);
					}
					freeMarker.set("sections2", sections2);

					List<Section> sections3 = repository.loadSections(section);
					if (!sections3.isEmpty())
						section.set("sections3", sections3);

					links.add(new Link(parent.getName(), parent.getKey()));
					links.add(new Link(section.getName(), section.getKey()));
				}
				else if (level == 3) {
					Section parent = section.getParent();
					Section superParent = parent.getParent();
					List<Section> sections2 = repository.loadSections(superParent);
					freeMarker.set("sections2", sections2);

					List<Section> sections3 = repository.loadSections(parent);
					for (Section iSection : sections3) {
						if (iSection.equals(section))
							iSection.set("selected", true);
					}
					parent.set("sections3", sections3);

					links.add(new Link(superParent.getName(), superParent.getKey()));
					links.add(new Link(parent.getName(), parent.getKey()));
					links.add(new Link(section.getName(), section.getKey()));
				}
			}
		}
		else if (page instanceof Document) {
			Document document = (Document) page;
			links.add(new Link(document.getName(), document.getKey()));
		}
		else if (page instanceof TopSection) {
			TopSection topSection = (TopSection) page;
			links.add(new Link(topSection.getName(), topSection.getKey()));
		}

		freeMarker.process("site.ftl", Locales.RU, writer);
	}

	private String compile(String content) {
		Compiler compiler = coreFactory.createCompiler();
		compiler.registerFunction("flash", new FlashFunction(flashService));
		content = compiler.compile(content);

		// TODO [opt]
		List<Phrase> phrases = repository.loadPhrases();
		for (Phrase phrase : phrases) {
			String key = "!" + phrase.getKey();
			String value = phrase.getValue();
			value = value.replace("<p>", "");
			value = value.replace("</p>", "");
			content = content.replace(key, value);
		}

		return content;
	}

	private void showDir(FreeMarker freeMarker, String[] parts, Writer writer, List<Link> links) {
		String locale = Locales.RU;
		PricingData pricingData = orderService.getPricingData();
		List<Color> colorList = repository.loadColors();
		Map<Integer, Color> colors = new LinkedHashMap<Integer, Color>();
		for (Color color : colorList) {
			colors.put(color.getNumber(), color);
		}

		FreeMarker catalogFreeMarker = new FreeMarker(getClass());
		catalogFreeMarker.set("prefix", serverConfig.getWebPrefix());
		String dir = "books";
		String url = serverConfig.getWebPrefix() + "/" + dir;
		freeMarker.set("catalogUrl", url);
		catalogFreeMarker.set("catalogUrl", url);

		Page page = new DocumentImpl();

		String name = "Фотокниги";
		links.add(new Link(name, url));

		String h1 = null;
		String content = null;
		if (parts.length == 2) {
			List<DirSection1> sections = repository.loadDirSections1();
			catalogFreeMarker.set("sections", sections);

			h1 = name;
			content = catalogFreeMarker.process("dir.ftl", Locales.RU);
		}
		else if (parts.length == 3) {
			if (parts[2].equals("all")) {
				List<Album> albums = repository.loadAlbums();
				for (Album album : albums) {
					setAlbumText(album, locale, colors, pricingData);
				}
				catalogFreeMarker.set("albums", albums);
				catalogFreeMarker.set("itemImageUrl", siteConfig.getFilesUrl() + "/dir/image/item");

				name = "Все фотокниги";
				h1 = name;
				content = catalogFreeMarker.process("albums.ftl", Locales.RU);

				links.add(new Link(name));
			}
			else {
				String key = parts[2];
				DirSection1 section1 = repository.findDirSection1(key);
				section1.set("selected", true);
				catalogFreeMarker.set("section1", section1);
				name = section1.getName();
				h1 = name;

				List<DirSection2> sections2 = repository.loadDirSections2(section1);
				section1.set("sections2", sections2);
				catalogFreeMarker.set("sections2", sections2);
				catalogFreeMarker.set("sectionImageUrl", siteConfig.getFilesUrl() + "/dir/image/section");
				content = catalogFreeMarker.process("dir2.ftl", Locales.RU);

				links.add(new Link(name));

				freeMarker.set("dir", true);
				List<DirSection1> sections = repository.loadDirSections1();
				freeMarker.set("dirSections", sections);
			}
		}
		else if (parts.length == 4) {
			if (parts[2].equals("book")) {
				int id = new Integer(parts[3]);
				Album album = repository.getAlbum(id);

				setAlbumText(album, locale, colors, pricingData);

				catalogFreeMarker.set("album", album);

				catalogFreeMarker.set("itemImageUrl", siteConfig.getFilesUrl() + "/dir/image/item");

				File folder = new File(siteConfig.getFilesPath() + "/dir/image/item/" + id);
				String[] files = folder.list();
				if (files != null)
					Arrays.sort(files);
				catalogFreeMarker.set("images", files);

				Document document = repository.findDocument("dir_flash_" + id);
				if (document != null)
					catalogFreeMarker.set("flash", document.getContent());

				name = album.getName().get(locale);
				h1 = name;
				content = catalogFreeMarker.process("album.ftl", Locales.RU);

				links.add(new Link(name));

				freeMarker.set("calc", true);
				ClientParametersWriter passer = new ClientParametersWriter();
				passer.setParam("productId", id);
				passer.write(freeMarker);
			}
			else {
				String key = parts[3];
				DirSection2 section2 = repository.findDirSection2(key);
				section2.set("selected", true);
				catalogFreeMarker.set("section2", section2);
				name = section2.getName();
				h1 = name;

				DirSection1 section1 = section2.getSection();
				section1.set("sections2", section1.getSections());

				for (Album album : section2.getAlbums()) {
					setAlbumText(album, locale, colors, pricingData);
				}

				catalogFreeMarker.set("itemImageUrl", siteConfig.getFilesUrl() + "/dir/image/item");

				content = catalogFreeMarker.process("dir3.ftl", Locales.RU);

				links.add(new Link(section1.getName(), url + "/" + section1.getKey()));
				links.add(new Link(name));

				freeMarker.set("dir", true);
				List<DirSection1> sections = repository.loadDirSections1();
				freeMarker.set("dirSections", sections);
			}
		}

		page.setH1(h1);
		page.set(CONTENT, content);
		freeMarker.set("page", page);
		freeMarker.process("site.ftl", Locales.RU, writer);
	}

	private void setAlbumText(Album album, String locale, Map<Integer, Color> colors,
			PricingData pricingData) {
		album.set("coverText", ProductType.values.get(album.getType()).get(locale));
		album.set("paperText", Paper.values.get(album.getPaper()).get(locale));
		album.set("bindingText", Binding.values.get(album.getBinding()).get(locale));

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

	@Override
	public List<TopSection> loadTopSections() {
		return repository.loadTopSections();
	}

	@Override
	public void addTopSection() {
		TopSection section = new TopSectionImpl();
		repository.save(section);
	}

	@Override
	public void saveTopSection(TopSection prototype) {
		TopSection topSection = repository.getTopSection(prototype.getId());
		topSection.setNumber(prototype.getNumber());
		fetch(topSection, prototype);
		try {
			repository.flush();
		}
		catch (DataIntegrityViolationException e) {
			throw new KeyExistsError();
		}
	}

	@Override
	public void deleteTopSections(List<Integer> ids) {
		repository.deleteTopSections(ids);
	}

	@Override
	public void addPhrase() {
		Phrase phrase = new Phrase();
		repository.savePhrase(phrase);
	}

	@Override
	public List<Phrase> loadPhrases() {
		return repository.loadPhrases();
	}

	@Override
	public void savePhrase(Phrase prototype) {
		Phrase phrase = repository.getPhrase(prototype.getId());
		phrase.setKey(prototype.getKey());
		phrase.setName(prototype.getName());
		String value = prototype.getValue();
		value = removeExtraBr(value);
		phrase.setValue(value);
	}

	@Override
	public void deletePhrases(List<Integer> ids) {
		repository.deletePhrases(ids);
	}

	@Override
	public List<Folder> loadFolders() {
		return repository.loadFolders();
	}

	@Override
	public void addDocument(int folderId) {
		Folder folder = repository.getFolder(folderId);

		Document document = new DocumentImpl();
		document.setFolder(folder);
		repository.saveDocument(document);
	}

	@Override
	public void saveDocument(Document prototype) {
		Document document = repository.getDocument(prototype.getId());
		fetch(document, prototype);
		try {
			repository.flush();
		}
		catch (DataIntegrityViolationException e) {
			throw new KeyExistsError();
		}
	}

	@Override
	public void deleteDocuments(List<Integer> ids) {
		if (ids.isEmpty())
			return;

		repository.deleteDocuments(ids);
	}

	@Override
	public List<Banner> loadBanners() {
		return repository.loadBanners();
	}

	@Override
	public void addBanner() {
		Banner banner = new Banner();
		repository.saveBanner(banner);
	}

	@Override
	public void saveBanner(Banner prototype) {
		Banner banner = repository.getBanner(prototype.getId());
		banner.setName(prototype.getName());
		banner.setTitle(prototype.getTitle());
		banner.setUrl(prototype.getUrl());
		banner.setTargetBlank(prototype.isTargetBlank());
		banner.setContent(prototype.getContent());
	}

	@Override
	public void deleteBanners(List<Integer> ids) {
		repository.deleteBanners(ids);
	}

	@Override
	public void addFolder() {
		Folder folder = new Folder();
		repository.saveFolder(folder);
	}

	@Override
	public void saveFolder(Folder prototype) {
		Folder folder = repository.getFolder(prototype.getId());
		folder.setName(prototype.getName());
		repository.flush();
	}

	@Override
	public void deleteFolders(List<Integer> ids) {
		if (ids.isEmpty())
			return;

		repository.deleteFolders(ids);
	}

	@Override
	public List<DirSection1> loadDirSections() {
		return repository.loadDirSections();
	}

	@Override
	public void addDirSection1() {
		DirSection1 section1 = new DirSection1();
		repository.saveDirSection1(section1);
	}

	@Override
	public void saveDirSection1(DirSection1 prototype) {
		DirSection1 section = repository.getDirSection1(prototype.getId());
		section.setIndex(prototype.getIndex());
		section.setKey(prototype.getKey());
		section.setName(prototype.getName());
		try {
			repository.flush();
		}
		catch (DataIntegrityViolationException e) {
			throw new KeyExistsError();
		}
	}

	@Override
	public void addDirSection2(int section1Id) {
		DirSection1 section1 = repository.getDirSection1(section1Id);

		DirSection2 section2 = new DirSection2();
		section2.setSection(section1);
		repository.saveSection2(section2);
	}

	@Override
	public void saveDirSection2(DirSection2 prototype) {
		DirSection2 section = repository.getDirSection2(prototype.getId());
		section.setIndex(prototype.getIndex());
		section.setKey(prototype.getKey());
		section.setPreview(prototype.getPreview());
		section.setName(prototype.getName());
		section.setAlbums(prototype.getAlbums());
		try {
			repository.flush();
		}
		catch (DataIntegrityViolationException e) {
			throw new KeyExistsError();
		}
	}

	@Override
	public List<Album> loadAlbums() {
		return repository.loadAlbums();
	}

	@Override
	public void deleteDirSections(int level, List<Integer> ids) {
		if (ids.isEmpty())
			return;

		if (level == 1)
			repository.deleteDirSections1(ids);
		else if (level == 2)
			repository.deleteDirSections2(ids);
	}

	@Override
	public String getNonLocalizedUrl(String path) {
		return siteConfig.getUrl() + path;
	}
}
