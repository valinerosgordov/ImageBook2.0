package ru.imagebook.server.ctl.flash;

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import ru.imagebook.server.service.flash.FlashService;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;

public class FlashController extends Controller {
	@Autowired
	private FlashService service;

	@Autowired
	public FlashController(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void registerHandlers() {
		addHandler(FlashMessages.SHOW_FLASH_XML, new MessageHandler<ShowFlashXmlMessage>() {
			@Override
			public void handle(ShowFlashXmlMessage message) {
				String sessionId = message.getSessionId();
				Writer writer = message.getWriter();
				service.showFlashXml(sessionId, writer);
			}
		});

		addHandler(FlashMessages.SHOW_WEB_FLASH_XML, new MessageHandler<ShowWebFlashXmlMessage>() {
			@Override
			public void handle(ShowWebFlashXmlMessage message) {
				String sessionId = message.getSessionId();
				Writer writer = message.getWriter();
				service.showWebFlashXml(sessionId, writer);
			}
		});

		addHandler(FlashMessages.SHOW_FLASH_IMAGE, new MessageHandler<ShowFlashImageMessage>() {
			@Override
			public void handle(ShowFlashImageMessage message) {
				String sessionId = message.getSessionId();
				int type = message.getPageType();
				int size = message.getSize();
				int page = message.getPage();
				HttpServletResponse response = message.getResponse();
				service.showFlashImage(sessionId, type, size, page, response);
			}
		});

		addHandler(FlashMessages.SHOW_WEB_FLASH_IMAGE, new MessageHandler<ShowWebFlashImageMessage>() {
			@Override
			public void handle(ShowWebFlashImageMessage message) {
				String sessionId = message.getSessionId();
				int type = message.getPageType();
				int size = message.getSize();
				int page = message.getPage();
				service.showWebFlashImage(sessionId, type, size, page, message.getResponse());
			}
		});
	}
}
