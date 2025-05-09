package ru.imagebook.server.service;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

import ru.imagebook.client.common.service.Finishing;
import ru.imagebook.server.repository.FinishingRepository;
import ru.imagebook.server.service.flash.FlashConfig;
import ru.imagebook.server.service.flash.FlashPath;
import ru.imagebook.server.service.flash.PageSize;
import ru.imagebook.server.service.flash.PageType;
import ru.imagebook.server.service.pdf.PdfConfig;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.Product;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.locale.Locales;

public class FinishingServiceImpl implements FinishingService {
	private final FinishingRepository repository;
	private final PdfConfig config;
	private final FlashPath flashPath;

	public FinishingServiceImpl(FinishingRepository repository, FlashConfig flashConfig,
			PdfConfig config) {
		this.repository = repository;
		this.config = config;
		this.flashPath = new FlashPath(flashConfig);
	}

	@Override
	public List<Order<?>> loadOrders() {
		List<Order<?>> orders = repository.loadOrders();
		for (Order<?> order : orders) {
			String url = null;
			Date printDate = order.getPrintDate();
			if (printDate != null) {
				String dateText = DateFormat.getDateInstance().format(printDate);
				Product product = order.getProduct();
				url = "ftp://" + config.getUser() + ":" + config.getPassword() + "@" + config.getHost()
						+ "/" + dateText + "/" + product.getName().get(Locales.RU);
			}
			order.set(Finishing.PDF_URL, url);
		}
		return orders;
	}

	@Override
	public void finishOrder(int orderId) {
		Order<?> order = repository.findOrder(orderId);
		if (order == null)
			throw new AccessDeniedError();
		order.setState(OrderState.PRINTED);
	}

	@Override
	public void scan(int orderId) {
		Order<?> order = repository.findPrintingOrder(orderId);
		if (order != null)
			order.setState(OrderState.FINISHING);
	}

	@Override
	public void showPreview(int orderId, OutputStream outputStream) {
		try {
			Order<?> order = repository.findOrder(orderId);
			Album album = (Album) order.getProduct();
			String path;
			if (album.isSeparateCover())
				path = flashPath.getImagePath(order.getId(), PageType.FRONT, PageSize.NORMAL, 1);
			else
				path = flashPath.getImagePath(order.getId(), PageType.NORMAL, PageSize.NORMAL, 1);
			FileInputStream in = new FileInputStream(path);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			IOUtils.copy(in, bos);
			bos.flush();
			in.close();
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
