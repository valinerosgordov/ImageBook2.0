package ru.imagebook.server.service;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.config.FlyleafConfig;
import ru.imagebook.server.config.VellumConfig;
import ru.imagebook.server.repository.FlyleafRepository;
import ru.imagebook.server.repository.OrderRepository;
import ru.imagebook.server.repository.VellumRepository;
import ru.imagebook.server.service.flash.FlashConfig;
import ru.imagebook.server.service.flash.FlashPath;
import ru.imagebook.server.service.flash.FlashService;
import ru.imagebook.server.service.flash.PageSize;
import ru.imagebook.server.service.pdf.PdfService;
import ru.imagebook.shared.model.Flyleaf;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.Vellum;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.gfx.GraphicsUtil;

public class OrderService2Impl implements OrderService2 {
	private static final Logger LOG = Logger.getLogger(OrderService2Impl.class);

	@Autowired
	private FlashService flashService;
	@Autowired
	private OrderRepository repository;
	@Autowired
	private PdfService pdfService;
	@Autowired
	private UserService userService;
	@Autowired
	private FlyleafRepository flyleafRepository;
	@Autowired
	private FlyleafConfig flyleafConfig;
	@Autowired
	private VellumRepository vellumRepository;
	@Autowired
	private VellumConfig vellumConfig;

	private FlashPath flashPath;

	@Autowired
	public OrderService2Impl(FlashConfig flashConfig) {
		this.flashPath = new FlashPath(flashConfig);
	}

	@Override
	public void requestRegenerateOrder(int orderId) {
		Order<?> order = repository.getOrder(orderId);
		int state = order.getState();
		if (state == OrderState.FLASH_GENERATED)
			order.setState(OrderState.FLASH_REGENERATION);
		else if (state == OrderState.PRINTING)
			order.setState(OrderState.PDF_REGENERATION);
		else
			// TODO client
			throw new RuntimeException("Can't regenerate. Wrong order state.");
	}

	@Override
	public void regenerateOrder(int orderId) {
		Order<?> order = repository.getOrder(orderId);
		int state = order.getState();
		if (state == OrderState.FLASH_REGENERATION) {
			if (flashService.generate(order))
				order.setState(OrderState.FLASH_GENERATED);
			else {
				order.setState(OrderState.FLASH_REGENERATION_ERROR);
			}
		}
		else if (state == OrderState.PDF_REGENERATION) {
			if (flashService.generate(order)) {
				try {
					pdfService.generatePdfManually(order);
				}
				catch (Exception e) {
					order.setState(OrderState.PDF_ERROR);
				}
			}
			else
				order.setState(OrderState.PDF_ERROR);
		}
	}

	@Deprecated
	@Override
	public void publishWebFlash(int orderId, boolean small) {
		Order<?> order = repository.getOrder(orderId);
		if (order.getState() < OrderState.FLASH_GENERATED) {
			throw new RuntimeException("Wrong order state. Flash must be generated.");
		}

		flashService.generateWebImages(order, small);

		order.setWebFlash(true);
	}

	@Override
	public void publishWebFlash(int orderId) {
		Order<?> order = repository.getOrder(orderId);
		if (order.getState() < OrderState.FLASH_GENERATED) {
			throw new RuntimeException("Wrong order state. Flash must be generated.");
		}

		flashService.generateWebImages(order);

		order.setWebFlash(true);
	}

    @Override
    public void showPreview(int orderId, int pageType, HttpServletResponse response) {
        String path = flashPath.getImagePath(orderId, pageType, PageSize.SMALL, 1);
        try {
            GraphicsUtil.sendImage(path, response);
        } catch (Exception e) {
            LOG.error(String.format("Cannot load order [%s] preview [%s]", orderId, path));
            Exceptions.rethrow(e);
        }
    }

    @Transactional
	@Override
	public void showFlyleafAppImage(Integer flyleafId, HttpServletResponse response) {
		Flyleaf flyleaf = flyleafRepository.findById(flyleafId).get();
		String path = flyleafConfig.appImagePath(flyleaf.getAppImageFilename());
		GraphicsUtil.sendImage(path, response);
	}

	@Transactional
	@Override
	public void showVellumAppImage(Integer vellumId, HttpServletResponse response) {
		Vellum vellum = vellumRepository.findById(vellumId).get();
		String path = vellumConfig.appImagePath(vellum.getAppImageFilename());
		GraphicsUtil.sendImage(path, response);
	}
}