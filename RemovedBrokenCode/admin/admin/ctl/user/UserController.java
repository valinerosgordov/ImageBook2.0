package ru.imagebook.client.admin.ctl.user;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.service.UserRemoteServiceAsync;
import ru.imagebook.client.admin.view.user.UserPresenter;
import ru.imagebook.client.common.ctl.user.UserLoadedMessage;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.admin.ProductsResult;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.gxt.form.ObjectFieldCallback;
import ru.minogin.core.client.security.PasswordGenerator;
import ru.minogin.util.client.rpc.XAsyncCallback;

@Singleton
public class UserController extends Controller implements UserPresenter {
	private final UserView view;
	private final CoreFactory coreFactory;
	private final UserService userService;
	private final I18nService i18nService;
	private final UserRemoteServiceAsync userRemoteService;

	private String query;
	private List<Vendor> vendors;

	@Inject
	public UserController(Dispatcher dispatcher, UserView view, CoreFactory coreFactory,
						  UserService userService, I18nService i18nService, UserRemoteServiceAsync userRemoteService) {
		super(dispatcher);

		this.view = view;
		this.coreFactory = coreFactory;
		this.userService = userService;
		this.i18nService = i18nService;
		this.userRemoteService = userRemoteService;
		view.setPresenter(this);
	}

	@Override
	public void registerHandlers() {
		addHandler(ru.imagebook.client.common.ctl.user.UserMessages.USER_LOADED,
				new MessageHandler<UserLoadedMessage>() {
					@Override
					public void handle(UserLoadedMessage message) {
						User user = message.getUser();
						userService.setUser(user);

						send(ru.imagebook.client.common.ctl.user.UserMessages.USER_ACQUIRED);
					}
				});

		addHandler(UserMessages.SHOW_USERS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadDataMessage());
			}
		});

		addHandler(UserMessages.LOAD_DATA_RESULT, new MessageHandler<LoadDataResultMessage>() {
			@Override
			public void handle(LoadDataResultMessage message) {
				vendors = message.getVendors();
				query = null;
				view.showUsersSection();
			}
		});

		addHandler(UserMessages.SHOW_ADD_FORM, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				User user = new User();
				PasswordGenerator passwordGenerator = coreFactory.createPasswordGenerator();
				user.setPassword(passwordGenerator.generate());
				view.showAddForm(user, i18nService.getLocale(), vendors);
			}
		});

		addHandler(UserMessages.SHOW_EDIT_FORM, new MessageHandler<ShowEditFormMessage>() {
			@Override
			public void handle(ShowEditFormMessage message) {
				User user = message.getUser();
				view.showEditForm(user, i18nService.getLocale(), vendors);
			}
		});

		addHandler(UserMessages.ADD_USER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.reload();
				view.hideAddForm();
			}
		});

		addHandler(UserMessages.SEND_INVITATION_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.reload();
			}
		});

		addHandler(UserMessages.UPDATE_USER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.reload();
				view.hideEditForm();
			}
		});

		addHandler(UserMessages.DELETE_USERS_REQUEST, new MessageHandler<DeleteUsersRequestMessage>() {
			@Override
			public void handle(DeleteUsersRequestMessage message) {
				List<User> users = message.getUsers();
				if (!users.isEmpty())
					view.confirmDeleteUsers(users);
				else
					view.alertNoUsersToDelete();
			}
		});

		addHandler(UserMessages.DELETE_USERS, new MessageHandler<DeleteUsersMessage>() {
			@Override
			public void handle(DeleteUsersMessage message) {
				List<Integer> userIds = new ArrayList<Integer>();

				for (User user : message.getUsers()) {
					userIds.add(user.getId());
				}
				message.setUserIds(userIds);
			}
		});

		addHandler(UserMessages.DELETE_USERS_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.reload();
			}
		});

		addHandler(UserMessages.CANNOT_DELETE_OWN_USER, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertCannotDeleteOwnUser();
			}
		});

		addHandler(UserMessages.LOAD_USERS_RESULT, new MessageHandler<LoadUsersResultMessage>() {
			@Override
			public void handle(LoadUsersResultMessage message) {
				view.showUsers(message.getUsers(), message.getOffset(), (int) message.getTotal(),
						i18nService.getLocale());
			}
		});

		addHandler(UserMessages.SEND_INVITATION, new MessageHandler<SendInvitationMessage>() {
			@Override
			public void handle(SendInvitationMessage message) {
				List<Integer> userIds = message.getUserIds();
				if (userIds.isEmpty()) {
					message.setCancelled(true);
					view.alertNoUsersSelected();
				}
			}
		});

		addHandler(UserMessages.INVITATION_ALREADY_SENT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertInvitationAlreadySent();
			}
		});

		addHandler(UserMessages.LOAD_USERS, new MessageHandler<LoadUsersMessage>() {
			@Override
			public void handle(LoadUsersMessage message) {
				message.setQuery(query);
			}
		});

		addHandler(UserMessages.SEARCH, new MessageHandler<SearchUserMessage>() {
			@Override
			public void handle(SearchUserMessage message) {
				query = message.getQuery();
				view.reload();
			}
		});

		addHandler(UserMessages.USER_EXISTS_ERROR, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertUserExists();
			}
		});
	}

	@Override
	public void loadProducts(final int offset, final int limit, String query,
							 final ObjectFieldCallback<Product> productCallback) {
		userRemoteService.loadProducts(offset, limit, query, new XAsyncCallback<ProductsResult>() {
			@Override
			public void onSuccess(ProductsResult productsResult) {
				view.showProducts(productsResult.getProducts(), offset, productsResult.getTotal(), productCallback);
			}
		});
	}
}