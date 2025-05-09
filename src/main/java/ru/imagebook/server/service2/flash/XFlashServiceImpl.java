package ru.imagebook.server.service2.flash;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.imagebook.client.flash.service.Flashes;
import ru.imagebook.server.service.flash.FlashConfig;
import ru.imagebook.server.service.flash.FlashPath;
import ru.imagebook.server.service.flash.FlashSize;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.flash.Flash;
import ru.imagebook.shared.service.admin.flash.FlashExistsException;
import ru.imagebook.shared.service.admin.flash.TooManyFlashesException;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.gfx.GraphicsUtil;
import ru.minogin.gfx.GraphicsUtil.ImageType;

public class XFlashServiceImpl implements XFlashService {
	@Autowired
	private FlashConfig config;

	@PreAuthorize("hasRole('OPERATOR') or #order.user.id == principal.userId")
	@Override
	public void generateFlash(Order<?> order, int flashWidth) {
		try {
			if (flashWidth < Flashes.MIN_FLASH_WIDTH
					|| flashWidth > Flashes.MAX_FLASH_WIDTH)
				throw new IllegalArgumentException();

			if (!order.isPublishFlash())
				throw new RuntimeException("Flash is not published.");

			List<Flash> flashes = loadFlashes(order);
			if (flashes.size() >= Flashes.MAX_FLASHES)
				throw new TooManyFlashesException();

			if (order.getCode() == null)
				order.setCode(UUID.randomUUID().toString());

			FlashPath flashPath = new FlashPath(config);

			int orderId = order.getId();
			File sourceFolder = new File(flashPath.getFlashDir(orderId));

			String destFolder = flashPath.getPublishPath(order.getCode(), flashWidth);
			File destFile = new File(destFolder);
			if (destFile.exists())
				throw new FlashExistsException();
			destFile.mkdirs();

			for (File file : sourceFolder.listFiles()) {
				String sourceName = file.getName();
				if (!sourceName.contains("_l."))
					continue;

				BufferedImage source = ImageIO.read(file);
				int sourcePageWidth = source.getWidth();
				int sourcePageHeight = source.getHeight();
				int pageWidth = flashWidth / 2;
				int pageHeight = pageWidth * sourcePageHeight / sourcePageWidth;

				BufferedImage dest = GraphicsUtil.resize(source, ImageType.JPEG,
						pageWidth, pageHeight);
				String destName = sourceName.replace("_l.", ".");
				GraphicsUtil.saveJpeg(dest, destFolder + "/" + destName);
			}
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@PreAuthorize("hasRole('OPERATOR') or #order.user.id == principal.userId")
	@Override
	public List<Flash> loadFlashes(Order<?> order) {
		if (!order.isPublishFlash())
			throw new RuntimeException("Flash is not published.");

		List<Flash> flashes = new ArrayList<Flash>();

		Album album = (Album) order.getProduct();

		FlashPath flashPath = new FlashPath(config);
		String path = flashPath.getPublishPath(order.getCode());
		File folder = new File(path);
		if (folder.exists()) {
			Vendor vendor = order.getUser().getVendor();

			File[] files = folder.listFiles();
			Arrays.sort(files, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return f1.getName().compareTo(f2.getName());
				}
			});
			for (File file : files) {
				int width = new Integer(file.getName());
				int height = getHeight(album, width);

				FreeMarker freeMarker = new FreeMarker(getClass());
				freeMarker.set("orderCode", order.getCode());
				freeMarker.set("width", width);
				freeMarker.set("height", height);
				freeMarker.set("flashUrl", vendor.getFlashUrl());
				String code = freeMarker.process("flashCode.ftl", Locales.RU);

				Flash flash = new Flash(width, height, code);
				flashes.add(flash);
			}
		}

		return flashes;
	}

	@Override
	public int getHeight(Album album, int width) {
		FlashSize flashSize = new FlashSize(album);
		int pageWidth = flashSize.getPageWidthPx();
		int pageHeight = flashSize.getPageHeightPx();
		return width * pageHeight / (pageWidth * 2);
	}

	@PreAuthorize("hasRole('OPERATOR') or #order.user.id == principal.userId")
	@Override
	public void deleteFlash(Order<?> order, int width) {
		try {
			if (!order.isPublishFlash())
				throw new RuntimeException("Flash is not published.");

			FlashPath flashPath = new FlashPath(config);
			String path = flashPath.getPublishPath(order.getCode(), width);
			File folder = new File(path);
			FileUtils.deleteDirectory(folder);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
