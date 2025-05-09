package ru.imagebook.server.service2.web;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.OrderRepository;
import ru.imagebook.server.repository.SiteRepository;
import ru.imagebook.shared.service.app.IncorrectCodeError;
import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.*;
import ru.imagebook.shared.model.site.Banner;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.imagebook.shared.model.site.Document;
import ru.imagebook.shared.model.site.Phrase;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.TopSection;

public class WebServiceImpl implements WebService {
	private final SiteRepository repository;
	private final OrderRepository orderRepository;
	private final VendorService vendorService;
	private final String prefix;

	@Autowired
	public WebServiceImpl(SiteRepository repository, ServerConfig serverConfig, OrderRepository orderRepository, VendorService vendorService) {
		this.repository = repository;
		prefix = serverConfig.getWebPrefix();
		this.orderRepository = orderRepository;
		this.vendorService = vendorService;
	}

	@Transactional
	@Override
	public Section getSection(String key) {
		return repository.getSection(key);
	}

	@Transactional
	@Override
	public List<TopSection> loadTopSections() {
		List<TopSection> topSections = repository.loadTopSections();
		for (TopSection topSection : topSections) {
			String url = topSection.getUrl() != null ? topSection.getUrl() : prefix + "/"
					+ topSection.getKey();
			topSection.set("url_", url);
		}
		return topSections;
	}

	@Transactional
	@Override
	public List<Banner> loadPageBanners() {
		List<Banner> banners = repository.loadPageBanners();
		Collections.reverse(banners);
		return banners;
	}

	@Transactional
	@Override
	public Section getRootSection() {
		return repository.getRootSection();
	}

	@Transactional
	@Override
	public List<Section> loadSections1() {
		List<Section> sections = repository.loadSections(getRootSection());
		for (Section s : sections) {
			String url = s.getUrl() != null ? s.getUrl() : prefix + "/" + s.getKey();
			s.set("url_", url);
		}
		return sections;
	}

	@Transactional
	@Override
	public Document getDocument(String key) {
		return repository.getDocument(key);
	}

	@Transactional
	@Override
	public TopSection getTopSection(String key) {
		return repository.getTopSection(key);
	}

	@Transactional
	@Override
	public List<Phrase> loadPhrases() {
		return repository.loadPhrases();
	}

	@Transactional
	@Override
	public List<Section> loadSections(Section parent) {
		return repository.loadSections(parent);
	}

	@Transactional
	@Override
	public Section getSection(int id) {
		return repository.getSection(id);
	}

	@Transactional
	@Override
	public List<DirSection1> loadDirSections1() {
		return repository.loadDirSections1();
	}

	@Transactional
	@Override
	public List<Album> loadAlbums() {
		return repository.loadAlbums();
	}

	@Transactional
	@Override
	public List<Color> loadColors() {
		return repository.loadColors();
	}

	@Transactional
	@Override
	public DirSection1 findDirSection1(String key) {
		return repository.findDirSection1(key);
	}

	@Transactional
	@Override
	public List<DirSection2> loadDirSections2(DirSection1 section1) {
		return repository.loadDirSections2(section1);
	}
	
	@Transactional
	@Override
	public List<DirSection2> loadDirSections2FullInformation(DirSection1 section1) {
		return repository.loadDirSections2FullInformation(section1);
	}	

	@Transactional
	@Override
	public DirSection2 findDirSection2(String key) {
		return repository.findDirSection2(key);
	}

	@Transactional
	@Override
	public Album getAlbum(int id) {
		return repository.getAlbum(id);
	}

	@Transactional
	@Override
	public Document findDocument(String key) {
		return repository.findDocument(key);
	}

	@Transactional
	@Override
	public BonusAction getBonusAction(final String code) {
		Vendor vendor = vendorService.getVendorByCurrentSite();

		final BonusCode bonusCode;
		if (vendor.isNoBonusCheck()) {
			bonusCode = orderRepository.getFirstCodeFromLastAction(vendor);
		}
		else {
			bonusCode = orderRepository.findCode(code, vendor);
		}
		if (bonusCode == null) throw new IncorrectCodeError();
		return bonusCode.getAction();
	}
}
