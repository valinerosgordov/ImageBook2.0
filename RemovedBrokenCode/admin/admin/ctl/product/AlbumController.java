package ru.imagebook.client.admin.ctl.product;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.service.AlbumRemoteServiceAsync;
import ru.imagebook.client.admin.view.product.AlbumPresenter;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumImpl;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.admin.UsersResult;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.util.client.rpc.XAsyncCallback;

@Singleton
public class AlbumController extends Controller implements AlbumPresenter {
	private final AlbumView view;
	private final I18nService i18nService;
	private final AlbumRemoteServiceAsync albumRemoteService;
	private final ProductImageController productImageController;

	private Product product;

	@Inject
	public AlbumController(Dispatcher dispatcher, AlbumView view, I18nService i18nService,
						   AlbumRemoteServiceAsync albumRemoteService, ProductImageController productImageController) {
		super(dispatcher);
		this.view = view;
		view.setPresenter(this);
		this.i18nService = i18nService;
		this.albumRemoteService = albumRemoteService;
		this.productImageController = productImageController;
	}

	@Override
	public void loadUsers(final int offset, final int limit, String query) {
		albumRemoteService.loadUsers(offset, limit, query, new XAsyncCallback<UsersResult>() {
			@Override
			public void onSuccess(UsersResult usersResult) {
				view.showUsers(usersResult.getUsers(), offset, usersResult.getTotal(), i18nService.getLocale());
			}
		});
	}

	@Override
	public void photosButtonClicked() {
		product = view.getSelectedAlbum();
		if (product == null) {
			view.productSelectionEmpty();
		} else {
			String productName = product.getName().getNonEmptyValue(i18nService.getLocale());
			productImageController.setProductId(product.getId());
			productImageController.show(productName);
		}
	}

	@Override
	public void registerHandlers() {
		addHandler(ProductMessages.SHOW_ALBUMS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showAlbumsSection();

				send(new LoadAlbumsMessage());
			}
		});

		addHandler(ProductMessages.LOAD_ALBUMS_RESULT, new MessageHandler<LoadAlbumsResultMessage>() {
			@Override
			public void handle(LoadAlbumsResultMessage message) {
				view.showAlbums(message.getAlbums(), i18nService.getLocale());
			}
		});

		addHandler(ProductMessages.SHOW_ADD_ALBUM_FORM, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showAddForm(new AlbumImpl(), i18nService.getLocale(), i18nService.getLocales());

				send(new LoadColorsForAlbumMessage());
			}
		});

		addHandler(ProductMessages.LOAD_COLORS_FOR_ALBUM_RESULT,
				new MessageHandler<LoadColorsForAlbumResultMessage>() {
					@Override
					public void handle(LoadColorsForAlbumResultMessage message) {
						view.showColorRangeField(message.getColors(), i18nService.getLocale());
					}
				});

		addHandler(ProductMessages.ALBUM_ADDED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadAlbumsMessage());

				view.hideAddForm();
			}
		});

		addHandler(ProductMessages.DELETE_ALBUMS_REQUEST,
				new MessageHandler<DeleteAlbumsRequestMessage>() {
					@Override
					public void handle(DeleteAlbumsRequestMessage message) {
						List<Album> albums = message.getAlbums();
						if (albums != null)
							view.confirmDeleteAlbums(albums);
						else
							view.alertNoAlbumsToDelete();
					}
				});

		addHandler(ProductMessages.DELETE_ALBUMS, new MessageHandler<DeleteAlbumsMessage>() {
			@Override
			public void handle(DeleteAlbumsMessage message) {
				List<Album> albums = message.getAlbums();
				List<Integer> ids = new ArrayList<Integer>();
				for (Album album : albums) {
					ids.add(album.getId());
				}
				message.setAlbumIds(ids);
			}
		});

		addHandler(ProductMessages.ALBUMS_DELETED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadAlbumsMessage());
			}
		});

		addHandler(ProductMessages.ALBUM_USED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertAlbumUsed();
			}
		});

		addHandler(ProductMessages.SHOW_EDIT_ALBUM_FORM, new MessageHandler<ShowEditAlbumFormMessage>() {
			@Override
			public void handle(ShowEditAlbumFormMessage message) {
				view.showEditForm(message.getAlbum(), i18nService.getLocale(), i18nService.getLocales());
				send(new LoadColorsForAlbumMessage());
			}
		});

		addHandler(ProductMessages.ALBUM_UPDATED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadAlbumsMessage());
				view.hideEditForm();
			}
		});
	}
}
