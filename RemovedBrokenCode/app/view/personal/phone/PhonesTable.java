package ru.imagebook.client.app.view.personal.phone;

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
import ru.imagebook.shared.model.Phone;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.format.PhoneFormat;


public class PhonesTable extends AbstractParamTable {
    private final PersonalConstants personalConstants;
    private final CommonConstants appConstants;
    private final HandlerManager handlerManager = new HandlerManager(this);

    @Inject
    public PhonesTable(PersonalConstants personalConstants, CommonConstants appConstants) {
        this.personalConstants = personalConstants;
        this.appConstants = appConstants;

        setAddItemAnchorText(personalConstants.addPhone());
    }

    public void showPhones(List<Phone> phones) {
        initTable();

        for (final Phone phone: phones) {
            addItem(phone.getPhone(), appConstants.delete(), new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    showDeletePhoneConfirmation(phone);
                }
            });
        }
    }

    private void showDeletePhoneConfirmation(final Phone phone) {
        Bootbox.Dialog.create()
            .setMessage(personalConstants.confirmPhoneDeletion())
            .setCloseButton(false)
            .setTitle(appConstants.deletionConfirmation())
            .addButton(appConstants.ok(), "btn-primary", new AlertCallback() {
                @Override
                public void callback() {
                    handlerManager.fireEvent(new DeletePhoneEvent(phone.getId()));
                }})
            .addButton(appConstants.cancel(), "btn-default")
            .show();
    }

    @Override
    public IsWidget showAddItemForm() {
        FlowPanel inputGroup = new FlowPanel();
        inputGroup.setStyleName("input-group");

        final XTextBox phoneField = new XTextBox();
        phoneField.setStyleName("form-control");
        phoneField.setPlaceholder("+79261112233");
        inputGroup.add(phoneField);

        FlowPanel inputGroupBtn = new FlowPanel();
        inputGroupBtn.setStyleName("input-group-btn");
        inputGroup.add(inputGroupBtn);

        Button saveButton = new Button(appConstants.save(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String phone = phoneField.getValue();
                if (phone == null) {
                    Notify.error(personalConstants.emptyPhoneError());
                } else if (!phone.matches(PhoneFormat.PATTERN)) {
                    Notify.error(personalConstants.incorrectPhoneError());
                } else {
                    handlerManager.fireEvent(new AddPhoneEvent(phone));
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

    public HandlerRegistration addAddPhoneEventHandler(AddPhoneEventHandler handler) {
        return handlerManager.addHandler(AddPhoneEvent.TYPE, handler);
    }

    public HandlerRegistration addDeleteEmailEventHandler(DeletePhoneEventHandler handler) {
        return handlerManager.addHandler(DeletePhoneEvent.TYPE, handler);
    }
}
