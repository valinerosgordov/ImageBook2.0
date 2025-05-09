package ru.imagebook.client.common.view.register;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.common.ctl.register.RegisterMessage;
import ru.imagebook.client.common.ctl.register.RegisterMessages;
import ru.imagebook.client.common.ctl.register.RegisterView;
import ru.imagebook.client.common.ctl.user.UserMessages;
import ru.imagebook.shared.model.Module;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.format.EmailFormat;
import ru.minogin.core.client.gwt.ui.Link;
import ru.minogin.core.client.gwt.ui.MessageBox;
import ru.minogin.core.client.gwt.ui.XTextBox;

@Singleton
public class RegisterViewImpl extends View implements RegisterView {
	private final RegisterConstants constants;
	private DialogBox dialogBox;
	private final CommonConstants appConstants;	
	private Dispatcher dispatcher;
	private Module module;

	@Inject
	public RegisterViewImpl(Dispatcher dispatcher, RegisterConstants constants,
			CommonConstants appConstants) {
		super(dispatcher);

		this.dispatcher = dispatcher;
		this.constants = constants;
		this.appConstants = appConstants;
	}

	@Override
	public Module getModule() {
		return module;
	}
	
	@Override
	public void showLinks(Module module) {
		this.module = module;
		
		RootPanel registerPanel = RootPanel.get("register");
		registerPanel.add(new Link(constants.register(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dispatcher.send(RegisterMessages.SHOW_REGISTER_FORM);
			}
		}));

		RootPanel restorePanel = RootPanel.get("restore");
		restorePanel.add(new Link(constants.restore(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dispatcher.send(UserMessages.SHOW_RECOVER_FORM);
			}
		}));
	}

	@Override
	public void showForm() {
		dialogBox = new DialogBox(false, true);
		dialogBox.setText(constants.registerHeading());

		VerticalPanel panel = new VerticalPanel();

		FlexTable table = new FlexTable();
		table.getCellFormatter().setWidth(0, 0, "100px");
		table.setWidget(0, 0, new Label(constants.nameField() + ":"));
		panel.add(table);

		final XTextBox nameField = new XTextBox();
		table.setWidget(0, 1, nameField);

		table.setWidget(1, 0, new Label(constants.emailField() + ":"));

		final XTextBox emailField = new XTextBox();
		table.setWidget(1, 1, emailField);

		table.setWidget(2, 0, new Label(constants.captchaField() + ":"));

		final XTextBox captchaField = new XTextBox();
		table.setWidget(2, 1, captchaField);

		final Image captchaImage = new Image("/SimpleCaptcha.jpg");
		table.setWidget(3, 1, captchaImage);

		panel.add(table);

		String agreementDiv = "<div class=\"blockquote\">"
			+ "Нажимая кнопку \"Зарегистрироваться\" Вы подтверждаете, что ознакомились и согласны с<br/>"
			+ "<a href=\"http://imagebook.ru/var/files/doc/offer.pdf\" target=\"_blank\">публичной офертой</a> "
			+ "и <a href=\"http://imagebook.ru/soglashenie\" target=\"_blank\">соглашением об обработке персональных данных.</a></div>";
		panel.add(new HTMLPanel(agreementDiv));

		table = new FlexTable();

		Button button = new Button(constants.registerButton(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String name = nameField.getValue();
				String email = emailField.getValue();
				String captcha = captchaField.getValue();

				if (name == null || email == null || captcha == null) {
					new MessageBox(appConstants.error(), constants.emptyFieldsError(), appConstants).show();
				} else if (!email.matches(EmailFormat.EMAIL)) {
					new MessageBox(appConstants.error(), constants.incorrectEmail(), appConstants).show();
				} else {
					send(new RegisterMessage(name, email, captcha, module));
				}
			}
		});
		table.setWidget(0, 0, button);

		button = new Button(appConstants.cancel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
                hideForm();
				//send(RegisterMessages.CANCEL);
			}
		});
		button.addStyleName("register-cancel-button");
		table.setWidget(0, 1, button);
		table.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		panel.add(table);

		dialogBox.setWidget(panel);

		dialogBox.center();

		nameField.setFocus(true);
	}

	@Override
	public void hideForm() {
		dialogBox.hide();
	}

	@Override
	public void infoRegisterResult() {
		MessageBox box = new MessageBox(constants.resultTitle(), constants.resultMessage(),
				appConstants);
//		box.setListener(new MessageBoxListener() {
//			@Override
//			public void onOK() {
//				send(RegisterMessages.REGISTER_INFORMED);
//			}
//		});
		box.show();
	}

	@Override
	public void alertUserExists() {
		MessageBox box = new MessageBox(appConstants.error(), constants.userExistsError(), appConstants);
		box.show();
	}

    @Override
    public void alertCaptchaIsInvalid() {
		MessageBox box = new MessageBox(appConstants.error(), constants.captchaIsInvalidError(), appConstants);
		box.show();
    }
}
