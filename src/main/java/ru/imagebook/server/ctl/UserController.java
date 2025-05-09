package ru.imagebook.server.ctl;

import java.io.Writer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import ru.imagebook.client.admin.ctl.user.AddUserMessage;
import ru.imagebook.client.admin.ctl.user.DeleteUsersMessage;
import ru.imagebook.client.admin.ctl.user.LoadDataMessage;
import ru.imagebook.client.admin.ctl.user.LoadDataResultMessage;
import ru.imagebook.client.admin.ctl.user.LoadUsersMessage;
import ru.imagebook.client.admin.ctl.user.LoadUsersResultMessage;
import ru.imagebook.client.admin.ctl.user.NoVendorNoUsernameUpdateUserMessage;
import ru.imagebook.client.admin.ctl.user.SendInvitationMessage;
import ru.imagebook.client.admin.ctl.user.UpdateUserMessage;
import ru.imagebook.client.admin.ctl.user.UserMessages;
import ru.imagebook.client.common.ctl.user.RecoverPasswordMessage;
import ru.imagebook.client.common.ctl.user.UserLoadedMessage;
import ru.imagebook.server.repository.AuthRepository;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Module;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.flow.remoting.MessageError;
import ru.saasengine.client.ctl.auth.AuthMessages;
import ru.saasengine.client.ctl.auth.LoggedInMessage;

public class UserController extends Controller {
	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private UserService service;
	@Autowired
	private VendorService vendorService;

	@Autowired
	public UserController(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void registerHandlers() {
		addHandler(UserMessages.SEND_INVITATION, new MessageHandler<SendInvitationMessage>() {
			@Override
			public void handle(SendInvitationMessage message) {
				List<Integer> userIds = message.getUserIds();
				service.sendInvitation(userIds);

				Message reply = new BaseMessage(UserMessages.SEND_INVITATION_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(AuthMessages.LOGGED_IN, new MessageHandler<LoggedInMessage>() {
			@Override
			public void handle(LoggedInMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				User user = service.getUser(userId);

				send(new UserLoadedMessage(user));
			}
		});

		addHandler(UserMessages.LOAD_USERS, new MessageHandler<LoadUsersMessage>() {
			@Override
			public void handle(LoadUsersMessage message) {
				int offset = message.getOffset();
				String query = message.getQuery();
				List<User> users = service.loadUsers(offset, message.getLimit(), query);
				long count = service.countUsers(query);
				send(new LoadUsersResultMessage(users, offset, count));
			}
		});

		addHandler(UserMessages.ADD_USER, new MessageHandler<AddUserMessage>() {
			@Override
			public void handle(AddUserMessage message) {
				User user = message.getUser();
				try {
					service.addUser(user);
				}
				catch (DataIntegrityViolationException e) {
					throw new MessageError(UserMessages.USER_EXISTS_ERROR);
				}

				service.updateAccount(user);

				Message reply = new BaseMessage(UserMessages.ADD_USER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(UserMessages.UPDATE_USER, new MessageHandler<UpdateUserMessage>() {
			@Override
			public void handle(UpdateUserMessage message) {
				User user = message.getUser();
				service.updateUser(user);

				service.updateAccount(user);

				Message reply = new BaseMessage(UserMessages.UPDATE_USER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(UserMessages.NO_VENDOR_NO_USERNAME_UPDATE_USER, new MessageHandler<NoVendorNoUsernameUpdateUserMessage>() {
			@Override
			public void handle(NoVendorNoUsernameUpdateUserMessage message) {
				User newUser = message.getUser();
				
				User oldUser = service.getUser(newUser.getId());
				if (!oldUser.getUserName().equals(newUser.getUserName()) || !oldUser.getVendor().equals(newUser.getVendor())) {
					throw new AccessDeniedError();
				}
				
				service.updateUser(newUser);

				service.updateAccount(newUser);

				Message reply = new BaseMessage(UserMessages.UPDATE_USER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});
		
		addHandler(UserMessages.DELETE_USERS, new MessageHandler<DeleteUsersMessage>() {
			@Override
			public void handle(DeleteUsersMessage message) {
				List<Integer> userIds = message.getUserIds();

				int userId = RemoteSessionMessage.getUserId(message);
				User user = service.getUser(userId);
				if (userIds.contains(user.getId()))
					throw new MessageError(UserMessages.CANNOT_DELETE_OWN_USER);

				authRepository.deleteAccounts(userIds);

				service.deleteUsers(userIds);

				Message reply = new BaseMessage(UserMessages.DELETE_USERS_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ru.imagebook.client.common.ctl.user.UserMessages.RECOVER_PASSWORD,
				new MessageHandler<RecoverPasswordMessage>() {
					@Override
					public void handle(RecoverPasswordMessage message) {
						String email = message.getEmail();
						Module module = Module.valueOf(message.getModule());
						service.recoverPassword(email, module);

						Message reply = new BaseMessage(
								ru.imagebook.client.common.ctl.user.UserMessages.RECOVER_PASSWORD_RESULT);
						reply.addAspects(RemotingAspect.CLIENT);
						send(reply);
					}
				});

		addHandler(ru.imagebook.server.ctl.user.UserMessages.RECOVER_PASSWORD,
				new MessageHandler<ru.imagebook.server.ctl.user.RecoverPasswordMessage>() {
					@Override
					public void handle(ru.imagebook.server.ctl.user.RecoverPasswordMessage message) {
						int userId = message.getUserId();
						String code = message.getCode();
						Writer writer = message.getWriter();
						String module = message.getModule();
						service.recoverPassword(userId, code, writer, module);
					}
				});

		addHandler(UserMessages.LOAD_DATA, new MessageHandler<LoadDataMessage>() {
			@Override
			public void handle(LoadDataMessage message) {
				List<Vendor> loadVendors = vendorService.loadVendors();
				send(new LoadDataResultMessage(loadVendors));
			}
		});
	}
}
