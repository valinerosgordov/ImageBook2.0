package ru.imagebook.client.common.view.auth;

import ru.imagebook.client.common.ctl.auth.AuthView;
import ru.imagebook.client.common.ctl.register.RegisterMessages;
import ru.imagebook.client.common.ctl.user.UserMessages;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.gwt.ui.MessageBox;
import ru.saasengine.client.ctl.auth.LoginMessage;
import ru.saasengine.client.model.auth.Credentials;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthViewImpl extends View implements AuthView {
	private final AuthConstants constants;
	private final CommonConstants appConstants;
	// private final AuthBundle bundle;

	private DialogBox dialogBox;
	private TextBox userNameField;
	private PasswordTextBox passwordField;
	private Button loginButton;
	private final AuthMessages messages;

	@Inject
	public AuthViewImpl(Dispatcher dispatcher, AuthConstants constants,
			CommonConstants appConstants, AuthMessages messages) {
		super(dispatcher);

		this.constants = constants;
		this.appConstants = appConstants;
		// this.bundle = bundle;
		this.messages = messages;
	}

	@Override
	public void showLoginDialog(final Credentials credentials, Vendor vendor) {
		dialogBox = new DialogBox(false, true);

		dialogBox.setText(messages.invitation(vendor.getName()));

		HorizontalPanel panel = new HorizontalPanel();

		// Image logo = new Image(bundle.logoIntro());
		// logo.addStyleName("auth-logo");
		// panel.add(logo);

		FlexTable table = new FlexTable();

		table.setWidget(0, 0, new Label(constants.userName() + ":"));

		userNameField = new TextBox();
		userNameField.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				credentials.setUserName(userNameField.getValue());
			}
		});
		userNameField.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				credentials.setUserName(userNameField.getValue());
			}
		});
		userNameField.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
						&& loginButton.isEnabled())
					send(new LoginMessage(credentials));
			}
		});
		table.setWidget(0, 1, userNameField);

		table.setWidget(1, 0, new Label(constants.password() + ":"));

		passwordField = new PasswordTextBox();
		passwordField.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				credentials.setPassword(passwordField.getValue());
			}
		});
		passwordField.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				credentials.setPassword(passwordField.getValue());
			}
		});
		passwordField.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
						&& loginButton.isEnabled())
					send(new LoginMessage(credentials));
			}
		});
		table.setWidget(1, 1, passwordField);

		FlexCellFormatter formatter = table.getFlexCellFormatter();
		formatter.setColSpan(2, 0, 2);
		loginButton = new Button(constants.enter(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				send(new LoginMessage(credentials));
			}
		});
		table.setWidget(2, 0, loginButton);

		if (vendor.getType() == VendorType.IMAGEBOOK) {
			Anchor anchor = new Anchor(constants.registerAnchor());
			anchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					send(RegisterMessages.SHOW_REGISTER_FORM);

					event.preventDefault();
				}
			});
			table.setWidget(3, 0, anchor);
			table.getCellFormatter().setStyleName(3, 0, "register-anchor");
			// table.getFlexCellFormatter().setColSpan(3, 0, 2);
		}

		Anchor anchor = new Anchor(constants.recoverAnchor());
		anchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				send(UserMessages.SHOW_RECOVER_FORM);

				event.preventDefault();
			}
		});
		table.setWidget(3, 1, anchor);
		table.getCellFormatter().setStyleName(3, 1, "register-anchor");
		table.getFlexCellFormatter().setHorizontalAlignment(3, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);
		// table.getFlexCellFormatter().setColSpan(3, 0, 2);

		panel.add(table);

		dialogBox.setWidget(panel);

		dialogBox.center();

		userNameField.setFocus(true);
	}

	@Override
	public void loginFailed() {
		new MessageBox(appConstants.error(), constants.authFailed(), appConstants)
				.show();
	}

	@Override
	public void hideLoginDialog() {
		if (dialogBox != null)
			dialogBox.hide();
	}

	@Override
	public void concurrentLogin() {
		MessageBox box = new MessageBox(appConstants.warning(),
				constants.concurrentLogin(), appConstants);
		box.setShowButton(false);
		box.show();
	}
}
