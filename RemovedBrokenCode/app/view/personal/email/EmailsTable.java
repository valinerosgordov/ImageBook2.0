package ru.imagebook.client.app.view.personal.email;

import java.util.List;

import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.AlertCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.client.app.view.common.XTextBox;
import ru.imagebook.client.app.view.personal.AbstractParamTable;
import ru.imagebook.client.app.view.personal.PersonalConstants;
import ru.imagebook.shared.model.Email;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.format.EmailFormat;


public class EmailsTable extends AbstractParamTable {
    private final PersonalConstants personalConstants;
    private final CommonConstants appConstants;
    private final HandlerManager handlerManager = new HandlerManager(this);

    @Inject
    public EmailsTable(PersonalConstants personalConstants, CommonConstants appConstants) {
        this.personalConstants = personalConstants;
        this.appConstants = appConstants;

        setAddItemAnchorText(personalConstants.addEmail());
    }

    public void showEmails(List<Email> emails) {
        initTable();

        for (final Email email : emails) {
            String emailText = email.getEmail();
            if (!email.isActive()) {
                emailText += " (" + personalConstants.inactive() + ")";
            }

            addItem(emailText, appConstants.delete(), new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    showDeleteEmailConfirmation(email);
                }
            });
        }
    }

    private void showDeleteEmailConfirmation(final Email email) {
        Bootbox.Dialog.create()
            .setMessage(personalConstants.confirmEmailDeletion())
            .setCloseButton(false)
            .setTitle(appConstants.deletionConfirmation())
            .addButton(appConstants.ok(), "btn-primary", new AlertCallback() {
                @Override
                public void callback() {
                    handlerManager.fireEvent(new DeleteEmailEvent(email.getId()));
                }})
            .addButton(appConstants.cancel(), "btn-default")
            .show();
    }

    @Override
    public IsWidget showAddItemForm() {
        FlowPanel inputGroup = new FlowPanel();
        inputGroup.setStyleName("input-group");

        final XTextBox emailField = new XTextBox();
        emailField.setStyleName("form-control");
        emailField.setPlaceholder("example@domain.ru");
        inputGroup.add(emailField);

        FlowPanel inputGroupBtn = new FlowPanel();
        inputGroupBtn.setStyleName("input-group-btn");
        inputGroup.add(inputGroupBtn);

        Button saveButton = new Button(appConstants.save(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String email = emailField.getValue();
                if (email == null) {
                    Notify.error(personalConstants.emptyEmail());
                } else if (!email.matches(EmailFormat.EMAIL)) {
                    Notify.error(personalConstants.incorrectEmail());
                } else {
                    handlerManager.fireEvent(new AddEmailEvent(email));
                    showAddItemAnchor();
                }
            }
        });
        saveButton.setStyleName("btn btn-primary");
        inputGroupBtn.add(saveButton);

        Button cancelButton = new Button(appConstants.cancel(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showAddItemAnchor();
            }
        });
        cancelButton.setStyleName("btn btn-default");
        inputGroupBtn.add(cancelButton);

        return inputGroup;
    }

    public HandlerRegistration addAddEmailEventHandler(AddEmailEventHandler handler) {
        return handlerManager.addHandler(AddEmailEvent.TYPE, handler);
    }

    public HandlerRegistration addDeleteEmailEventHandler(DeleteEmailEventHandler handler) {
        return handlerManager.addHandler(DeleteEmailEvent.TYPE, handler);
    }
}
