package ru.imagebook.server.ctl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import ru.imagebook.client.admin.ctl.product.*;
import ru.imagebook.server.service.ProductService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.flow.remoting.MessageError;

public class ProductController extends Controller {
	private final ProductService service;

	public ProductController(Dispatcher dispatcher, final ProductService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(ProductMessages.LOAD_ALBUMS, new MessageHandler<LoadAlbumsMessage>() {
			@Override
			public void handle(LoadAlbumsMessage message) {
				List<Album> albums = service.loadAlbums();

				send(new LoadAlbumsResultMessage(albums));
			}
		});

		addHandler(ProductMessages.LOAD_COLORS_FOR_ALBUM, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				List<Color> colors = service.loadColors();
				send(new LoadColorsForAlbumResultMessage(colors));
			}
		});

		addHandler(ProductMessages.ADD_ALBUM, new MessageHandler<AddAlbumMessage>() {
			@Override
			public void handle(AddAlbumMessage message) {
				service.addAlbum(message.getAlbum());

				Message reply = new BaseMessage(ProductMessages.ALBUM_ADDED);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ProductMessages.UPDATE_ALBUM, new MessageHandler<UpdateAlbumMessage>() {
			@Override
			public void handle(UpdateAlbumMessage message) {
				service.updateAlbum(message.getAlbum());

				Message reply = new BaseMessage(ProductMessages.ALBUM_UPDATED);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ProductMessages.DELETE_ALBUMS, new MessageHandler<DeleteAlbumsMessage>() {
			@Override
			public void handle(DeleteAlbumsMessage message) {
				try {
					service.deleteAlbums(message.getAlbumIds());
				}
				catch (DataIntegrityViolationException e) {
					throw new MessageError(ProductMessages.ALBUM_USED);
				}

				Message reply = new BaseMessage(ProductMessages.ALBUMS_DELETED);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ProductMessages.LOAD_COLORS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				List<Color> colors = service.loadColors();
				send(new LoadColorsResultMessage(colors));
			}
		});

		addHandler(ProductMessages.UPDATE_COLORS, new MessageHandler<UpdateColorsMessage>() {
			@Override
			public void handle(UpdateColorsMessage message) {
				List<Color> colors = message.getColors();
				service.updateColors(colors);

				Message reply = new BaseMessage(ProductMessages.COLORS_UPDATED);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ProductMessages.DELETE_COLORS, new MessageHandler<DeleteColorsMessage>() {
			@Override
			public void handle(DeleteColorsMessage message) {
				List<Color> colors = message.getColors();
				try {
					service.deleteColors(colors);
				}
				catch (DataIntegrityViolationException e) {
					throw new MessageError(ProductMessages.COLOR_USED_ERROR);
				}

				Message reply = new BaseMessage(ProductMessages.COLORS_DELETED);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});
	}
}
