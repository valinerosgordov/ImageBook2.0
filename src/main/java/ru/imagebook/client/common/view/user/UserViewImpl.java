package ru.imagebook.client.common.view.user;

import ru.imagebook.client.common.ctl.user.RecoverPasswordMessage;
import ru.imagebook.client.common.ctl.user.UserView;
import ru.imagebook.shared.model.Module;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.gwt.ui.MessageBox;
import ru.minogin.core.client.gwt.ui.XTextBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UserViewImpl extends View implements UserView {
	private final UserConstants constants;
	private final CommonConstants appConstants;
	private DialogBox recoverFormWindow;

	@Inject
	public UserViewImpl(Dispatcher dispatcher, UserConstants constants, CommonConstants appConstants) {
		super(dispatcher);

		this.constants = constants;
		this.appConstants = appConstants;
	}

	@Override
	public void showRecoverForm(final Module module) {
		recoverFormWindow = new DialogBox(false, true);
		recoverFormWindow.setText(constants.recoverHeading());
		VerticalPanel panel = new VerticalPanel();
		panel.addStyleName("recover-panel");
		panel.setWidth("300px");

		HorizontalPanel emailPanel = new HorizontalPanel();
		emailPanel.add(new HTML(constants.emailField() + ":"));
		final XTextBox emailField = new XTextBox();
		emailPanel.add(emailField);
		panel.add(emailPanel);

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(new Button(constants.recoverButton(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String email = emailField.getValue();
				if (email != null)
					send(new RecoverPasswordMessage(email, module));
				else
					new MessageBox(appConstants.error(), constants.emailNotSpecified(), appConstants).show();
			}
		}));
		buttonPanel.add(new Button(appConstants.close(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				recoverFormWindow.hide();
			}
		}));
		panel.add(buttonPanel);

		recoverFormWindow.add(panel);
		recoverFormWindow.center();

		emailField.setFocus(true);
	}

	@Override
	public void hideRecoverForm() {
		recoverFormWindow.hide();
	}

	@Override
	public void infoRecoverMailSent() {
		final DialogBox box = new DialogBox(false, true);
		box.setText(constants.recoverHeading());
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("300px");
		panel.add(new HTML(constants.recoverMailSent()));
		Button button = new Button(appConstants.ok(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				box.hide();
			}
		});
		panel.add(button);
		panel.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_CENTER);
		box.add(panel);
		box.center();
	}
}
