package ru.imagebook.server.service2.flash;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository2.FlashRepository;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Vendor;

public class XmlFlashServiceImpl implements XmlFlashService {
	@Autowired
	private XFlashService xService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	protected FlashRepository repository;

	@Transactional
	@Override
	public FlashXml createFlashXml(String orderCode, int width) {
		AlbumOrder order = (AlbumOrder) repository.getOrder(orderCode);
		Album album = (Album) order.getProduct();
		Vendor vendor = order.getUser().getVendor();

		FlashXml xml = new FlashXml();
		xml.setWidth(width / 2);
		xml.setHeight(xService.getHeight(album, width));
		xml.setHardCover(album.isHardcover() ? "true" : "false");
		xml.setTitle("&nbsp;");
		xml.setOrderCode(orderCode);
		xml.setFlashUrl(vendor.getFlashUrl());
		xml.setFlashWidth(width);
		List<FlashPage> pages = xml.getPages();
		if (album.isSeparateCover()) {
			pages.add(new FlashPage("f1"));
			pages.add(new FlashPage("f2"));
		}
		for (int i = 1; i <= order.getPageCount(); i++) {
			pages.add(new FlashPage(i + ""));
		}
		if (album.isSeparateCover()) {
			pages.add(new FlashPage("b1"));
			pages.add(new FlashPage("b2"));
		}
		// String name = session.get("name");
		// String author = session.get("author");
		// String title = name + "\n";
		// if (!author.isEmpty())
		// title += "Автор: " + author;
		// freeMarker.set("title", title);
		// freeMarker.process("webFlashXml.ftl", Locales.RU, writer);
		//
		return xml;
	}
}
