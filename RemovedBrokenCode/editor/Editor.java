package ru.imagebook.client.editor;

import ru.imagebook.client.common.ctl.auth.AuthController;
import ru.imagebook.client.common.ctl.register.RegisterController;
import ru.imagebook.client.editor.ctl.EditorController;
import ru.imagebook.client.editor.ctl.EditorMessages;
import ru.imagebook.client.editor.ctl.file.FileController;
import ru.imagebook.client.editor.ctl.order.OrderController;
import ru.imagebook.client.editor.ctl.pages.PagesController;
import ru.imagebook.client.editor.ctl.spread.SpreadController;
import ru.imagebook.client.editor.ctl.user.EditorUserController;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.remoting.RemotingPostController;
import ru.saasengine.client.ctl.auth.AuthPreController;
import ru.saasengine.client.ctl.browser.BrowserController;
import ru.saasengine.client.ctl.failure.FailureController;

import com.google.inject.Inject;

public class Editor {
	private final Dispatcher dispatcher;
	private AuthController authController;
	private BrowserController browserController;
	private EditorController editorController;
	private FailureController failureController;
	private AuthPreController authPreController;
	private RemotingPostController remotingPostController;
	private FileController fileController;
	private SpreadController spreadController;
	private OrderController orderController;
	private PagesController pagesController;
	private EditorUserController userController;
	private RegisterController registerController;

	@Inject
	public Editor(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Inject
	public void setAuthController(AuthController authController) {
		this.authController = authController;
	}

	@Inject
	public void setBrowserController(BrowserController browserController) {
		this.browserController = browserController;
	}

	@Inject
	public void setEditorController(EditorController editorController) {
		this.editorController = editorController;
	}

	@Inject
	public void setFailureController(FailureController failureController) {
		this.failureController = failureController;
	}

	@Inject
	public void setAuthPreController(AuthPreController authPreController) {
		this.authPreController = authPreController;
	}

	@Inject
	public void setRemotingPostController(
			RemotingPostController remotingPostController) {
		this.remotingPostController = remotingPostController;
	}

	@Inject
	public void setFileController(FileController fileController) {
		this.fileController = fileController;
	}

	@Inject
	public void setSpreadController(SpreadController spreadController) {
		this.spreadController = spreadController;
	}

	@Inject
	public void setOrderController(OrderController orderController) {
		this.orderController = orderController;
	}

	@Inject
	public void setPagesController(PagesController pagesController) {
		this.pagesController = pagesController;
	}
	
	@Inject
	public void setUserController(EditorUserController userController) {
		this.userController = userController;
	}
	
	@Inject
	public void setRegisterController(RegisterController registerController) {
		this.registerController = registerController;
	}

	public void start() {
		authPreController.registerHandlers();

		editorController.registerHandlers();

		authController.registerHandlers();
		browserController.registerHandlers();
		failureController.registerHandlers();
		fileController.registerHandlers();
		spreadController.registerHandlers();
		orderController.registerHandlers();
		pagesController.registerHandlers();
		userController.registerHandlers();
		registerController.registerHandlers();

		remotingPostController.registerHandlers();

		dispatcher.send(new BaseMessage(EditorMessages.START));

		// dispatcher.send(new LoginMessage(new Credentials("andrey", "passwd")));
	}
}
