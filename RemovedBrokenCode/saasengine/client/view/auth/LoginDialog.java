package ru.saasengine.client.view.auth;

import ru.minogin.core.client.gxt.form.XTextField;
import ru.saasengine.client.model.auth.Credentials;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public abstract class LoginDialog extends Dialog {
	private int userNameMinLength = 5;
	private int userNameMaxLength = 100;
	private int passwordMinLength = 6;
	private int passwordMaxLength = 100;

	private final Credentials credentials;
	private XTextField userNameField;
	private XTextField passwordField;
	private Button loginButton;

	public LoginDialog(final Credentials credentials, AuthConstants constants, AuthBundle bundle) {
		this.credentials = credentials;

		setId(AuthWidgets.LOGIN_DIALOG);

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(120);
		layout.setDefaultWidth(155);
		setLayout(layout);

		setIcon(AbstractImagePrototype.create(bundle.login16()));
		setHeading(constants.invitation());
		setModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(330);
		setResizable(false);
		setClosable(false);

		KeyListener keyListener = new KeyListener() {
			@Override
			public void componentKeyPress(ComponentEvent event) {
				if (event.getKeyCode() == 13 && loginButton.isEnabled())
					login();
			}

			public void componentKeyUp(ComponentEvent event) {
				validate();
			}
		};

		userNameField = new XTextField();
		userNameField.setId(AuthWidgets.LOGIN_DIALOG__USER_NAME_FIELD);
		userNameField.setFieldLabel(constants.userName());
		userNameField.addKeyListener(keyListener);
		userNameField.setMinLength(userNameMinLength);
		userNameField.setMaxLength(userNameMaxLength);
		userNameField.setValue(credentials.getUserName());
		userNameField.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				credentials.setUserName(userNameField.getValue());
			}
		});
		add(userNameField);

		setFocusWidget(userNameField);

		passwordField = new XTextField();
		passwordField.setId(AuthWidgets.LOGIN_DIALOG__PASSWORD_FIELD);
		passwordField.setPassword(true);
		passwordField.setFieldLabel(constants.password());
		passwordField.addKeyListener(keyListener);
		passwordField.setMinLength(passwordMinLength);
		passwordField.setMaxLength(passwordMaxLength);
		passwordField.setValue(credentials.getPassword());
		passwordField.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				credentials.setPassword(passwordField.getValue());
			}
		});
		add(passwordField);

		loginButton = new Button(constants.enter());
		loginButton.setId(AuthWidgets.LOGIN_DIALOG__LOGIN_BUTTON);
		loginButton.setId("dcd9bd76-a0f3-4b67-b9a4-6f17f27bf1b5");
		loginButton.disable();
		loginButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				login();
			}
		});

		addButton(loginButton);
	}

	@Override
	protected void createButtons() {}

	private void validate() {
		boolean isValid = !userNameField.isEmpty() && !passwordField.isEmpty()
				&& userNameField.getValue().length() >= userNameMinLength
				&& userNameField.getValue().length() <= userNameMaxLength
				&& passwordField.getValue().length() >= passwordMinLength
				&& passwordField.getValue().length() <= passwordMaxLength;
		loginButton.setEnabled(isValid);
	}

	public void setUserNameMinLength(int userNameMinLength) {
		this.userNameMinLength = userNameMinLength;
	}

	public void setUserNameMaxLength(int userNameMaxLength) {
		this.userNameMaxLength = userNameMaxLength;
	}

	public void setPasswordMinLength(int passwordMinLength) {
		this.passwordMinLength = passwordMinLength;
	}

	public void setPasswordMaxLength(int passwordMaxLength) {
		this.passwordMaxLength = passwordMaxLength;
	}

	public void reset() {
		userNameField.reset();
		passwordField.reset();
		validate();
		userNameField.focus();
	}

	private void login() {
		credentials.setUserName(userNameField.getValue());
		credentials.setPassword(passwordField.getValue());

		onLogin();
	}

	protected abstract void onLogin();
}
