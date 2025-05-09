package ru.imagebook.client.editor.ctl.file;

import ru.imagebook.client.editor.ctl.order.OrderMessages;
import ru.imagebook.client.editor.ctl.order.ShowOrderMessage;
import ru.imagebook.client.editor.service.EditorService;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.editor.ImageLayoutType;
import ru.minogin.core.client.file.FileBean;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.service.auth.AuthService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FileController extends Controller {
	private final FileView view;
	private final AuthService authService;

	private final EditorService service;

	@Inject
	public FileController(Dispatcher dispatcher, FileView view, AuthService authService,
			EditorService service) {
		super(dispatcher);

		this.view = view;
		this.authService = authService;
		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(FileMessages.LAYOUT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.layout();
			}
		});

		addHandler(FileMessages.SHOW_FOLDERS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadFoldersMessage());
			}
		});

		addHandler(FileMessages.LOAD_FOLDERS_RESULT, new MessageHandler<LoadFoldersResultMessage>() {
			@Override
			public void handle(LoadFoldersResultMessage message) {
				FileBean rootFolder = message.getRoot();
				FileBean baseFolder = rootFolder.getChildren().get(0);
				service.setFolder(baseFolder);
				view.showFolders(rootFolder);
				view.selectFolder(baseFolder);
				// send(new LoadImagesMessage(folder.getPath()));
			}
		});

		addHandler(FileMessages.FOLDER_SELECTED, new MessageHandler<FolderSelectedMessage>() {
			@Override
			public void handle(FolderSelectedMessage message) {
				FileBean folder = message.getFolder();
				service.setFolder(folder);
				send(new LoadImagesMessage(folder.getPath()));
			}
		});

		addHandler(FileMessages.IMAGE_SELECTED, new MessageHandler<ImageSelectedMessage>() {
			@Override
			public void handle(ImageSelectedMessage message) {
				service.setImagePath(message.getPath());
			}
		});

		addHandler(FileMessages.LOAD_IMAGES_RESULT, new MessageHandler<LoadImagesResultMessage>() {
			@Override
			public void handle(LoadImagesResultMessage message) {
				view.showImages(message.getNames(), authService.getSessionId(), service.getFolder());
				send(FileMessages.SHOW_IMAGE_MENU);
			}
		});

		addHandler(FileMessages.SHOW_IMAGE_MENU, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showImageMenu(service.getOrder(), service.getPageNumber());
			}
		});

		addHandler(FileMessages.UPLOAD, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showUploadForm(service.getFolder(), authService.getSessionId());
			}
		});

		addHandler(FileMessages.UPLOAD_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideUploadProgress();
				view.showUnpackingProgress();
			}
		});

		addHandler(FileMessages.UPLOAD_FAILED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideUploadProgress();
				view.alertUploadFailed();
			}
		});

		addHandler(FileMessages.UNPACKING_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideUnpackingProgress();
				view.showResizingProgress();
			}
		});

        addHandler(FileMessages.NON_ZIP_ARCHIVE_FORMAT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.hideUnpackingProgress();
                view.alertNonZipArchiveFormat();
            }
        });

        addHandler(FileMessages.UNSUPPORTED_IMAGE_TYPE, new MessageHandler<UnsupportedImageTypeMessage>() {
            @Override
            public void handle(UnsupportedImageTypeMessage message) {
                view.hideResizingProgress();
                view.alertUnsupportedImageType(message.getFilename());
            }
        });

		addHandler(FileMessages.UNPACKING_FAILED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideUnpackingProgress();
				view.alertUnpackingFailed();
			}
		});

		addHandler(FileMessages.RESIZING_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadImagesMessage(service.getFolder().getPath()));
				send(new LoadFoldersMessage());
				view.hideResizingProgress();
				view.hideUploadForm();
			}
		});

		addHandler(FileMessages.RESIZING_PROGRESS, new MessageHandler<ResizingProgressMessage>() {
			@Override
			public void handle(ResizingProgressMessage message) {
				double resized = message.getResized();
				double total = message.getTotal();
				double value = resized / total;
				view.updateResizeProgress(value);
			}
		});

		addHandler(FileMessages.RESIZING_FAILED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideResizingProgress();
				view.alertResizingFailed();
			}
		});

		addHandler(FileMessages.CREATE_FOLDER_REQUEST, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showCreateFolderForm();
			}
		});

		addHandler(FileMessages.CREATE_FOLDER, new MessageHandler<CreateFolderMessage>() {
			@Override
			public void handle(CreateFolderMessage message) {
				message.setPath(service.getFolder().getPath());
			}
		});

		addHandler(FileMessages.CREATE_FOLDER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadFoldersMessage());
				view.hideCreateFolderForm();
			}
		});

		addHandler(FileMessages.EDIT_FOLDER_REQUEST, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showEditFolderForm(service.getFolder().getName());
			}
		});

		addHandler(FileMessages.EDIT_FOLDER, new MessageHandler<EditFolderMessage>() {
			@Override
			public void handle(EditFolderMessage message) {
				message.setPath(service.getFolder().getPath());
			}
		});

		addHandler(FileMessages.EDIT_FOLDER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadFoldersMessage());
				view.hideEditFolderForm();
			}
		});

		addHandler(FileMessages.DELETE_FOLDER, new MessageHandler<DeleteFolderMessage>() {
			@Override
			public void handle(DeleteFolderMessage message) {
				message.setPath(service.getFolder().getPath());
			}
		});

		addHandler(FileMessages.DELETE_FOLDER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadFoldersMessage());
			}
		});

		addHandler(FileMessages.ADD_IMAGE, new MessageHandler<AddImageMessage>() {
			@Override
			public void handle(AddImageMessage message) {
				String path =message.getType() < ImageLayoutType.FOLDER_NORMAL ? service.getImagePath() : service.getFolder().getPath();
				message.setPath(path);
			}
		});

		/*addHandler(FileMessages.SHOW_NOTIFICATION, new MessageHandler<ShowNotificationMessage>() {
			@Override
			public void handle(ShowNotificationMessage message) {
				System.out.print(message.getImageLayoutType());
			}
		});*/

		addHandler(FileMessages.DELETE_IMAGE, new MessageHandler<DeleteImageMessage>() {
			@Override
			public void handle(DeleteImageMessage message) {
				String path = service.getImagePath();
				message.setPath(path);
			}
		});

		addHandler(FileMessages.DELETE_IMAGE_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadImagesMessage(service.getFolder().getPath()));
			}
		});

		addHandler(FileMessages.REQIRE_SHOW_NOTIFICATION, new MessageHandler<RequireShowNotificationMessage>() {
			@Override
			public void handle(RequireShowNotificationMessage message) {
				view.showNotificationMessage(message.getType(), message.getImageLayoutType(), message.isShowMessage());
			}
		});

		addHandler(FileMessages.UPLOAD_PROGRESS, new MessageHandler<UploadProgressMessage>() {
			@Override
			public void handle(UploadProgressMessage message) {
				double uploaded = message.getUploaded();
				double total = message.getTotal();
				double value = uploaded / total;
				view.updateUploadProgress(value);
			}
		});

		addHandler(FileMessages.WRONG_FILE_TYPE, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideUploadProgress();
				view.alertWrongFileType();
			}
		});

		addHandler(FileMessages.DISPOSE, new MessageHandler<DisposeMessage>() {
			@Override
			public void handle(DisposeMessage message) {
				FileBean folder = service.getFolder();
				message.setPath(folder.getPath());
			}
		});

		addHandler(FileMessages.DISPOSE_RESULT, new MessageHandler<DisposeResultMessage>() {
			@Override
			public void handle(DisposeResultMessage message) {
				AlbumOrder order = (AlbumOrder) message.getOrder();
				service.setOrder(order);

				send(new ShowOrderMessage(order));

				send(OrderMessages.ORDER_SHOWN);
			}
		});

		addHandler(OrderMessages.ORDER_SHOWN, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.enableDisposeButton();
			}
		});

		addHandler(OrderMessages.CLOSE_ORDER, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.disableDisposeButton();
				view.disableDisposeFolderImagesMenu();
			}
		});
	}
}
