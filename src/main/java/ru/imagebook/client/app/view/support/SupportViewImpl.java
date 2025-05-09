package ru.imagebook.client.app.view.support;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.ctl.support.SupportPresenter;
import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.client.app.view.common.XTextArea;
import ru.imagebook.client.app.view.common.XTextBox;


@Singleton
public class SupportViewImpl implements SupportView {
	interface SupportUiBinder extends UiBinder<Widget, SupportViewImpl> {
	}
    private static SupportUiBinder uiBinder = GWT.create(SupportUiBinder.class);

    @UiField
    XTextBox subjectField;
    @UiField
    XTextArea textField;
    @UiField
    Button sendButton;
    @UiField
    Label formIncompleteLabel;

	private final SupportConstants constants;

    private SupportPresenter presenter;

	@Inject
	public SupportViewImpl(SupportConstants constants) {
		this.constants = constants;
	}

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @Override
    public void setPresenter(SupportPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Button getSendButton() {
        return sendButton;
    }

    @UiHandler("sendButton")
    public void onSendButtonClick(final ClickEvent event) {
        boolean isFormValid = validateForm();
        formIncompleteLabel.setVisible(!isFormValid);

        if (isFormValid) {
            presenter.sendRequest(subjectField.getValue(), textField.getValue());
        }
    }

    private boolean validateForm() {
        boolean isSubjectFieldEmpty = subjectField.isEmpty();
        boolean isTextFieldEmpty = textField.isEmpty();
        subjectField.setStyleName("has-error", isSubjectFieldEmpty);
        textField.setStyleName("has-error", isTextFieldEmpty);
        return !isSubjectFieldEmpty && !isTextFieldEmpty;
    }

    @Override
    public void notifyRequestSent() {
        Notify.info(constants.requestSent());
        clearForm();
    }

    private void clearForm() {
        subjectField.setValue(null);
        textField.setValue(null);
    }
}
