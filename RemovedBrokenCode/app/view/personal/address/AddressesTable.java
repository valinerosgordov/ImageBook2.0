package ru.imagebook.client.app.view.personal.address;

import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.AlertCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

import ru.imagebook.client.app.view.CompilerFactory;
import ru.imagebook.client.app.view.personal.AbstractParamTable;
import ru.imagebook.client.app.view.personal.PersonalConstants;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.lang.template.Compiler;


public class AddressesTable extends AbstractParamTable {
    private final PersonalConstants personalConstants;
    private final CommonConstants appConstants;
    private final CompilerFactory compilerFactory;
    private final AddressForm addressForm;
    private final HandlerManager handlerManager = new HandlerManager(this);

    private User user;

    @Inject
    public AddressesTable(PersonalConstants personalConstants, CommonConstants appConstants,
                          CompilerFactory compilerFactory, AddressForm addressForm) {
        this.personalConstants = personalConstants;
        this.appConstants = appConstants;
        this.compilerFactory = compilerFactory;
        this.addressForm = addressForm;

        setAddItemAnchorText(personalConstants.addAddress());
        addAddressFormHandlers(addressForm);
    }

    private void addAddressFormHandlers(AddressForm addressForm) {
        addressForm.addValueChangeHandler(new ValueChangeHandler<Address>() {
            @Override
            public void onValueChange(ValueChangeEvent<Address> event) {
                handlerManager.fireEvent(new AddAddressEvent(event.getValue()));
                showAddItemAnchor();
            }
        });
        addressForm.addCancelButtonClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showAddItemAnchor();
            }
        });
    }

    public void showAddresses(User user) {
        this.user = user;

        initTable();

        Compiler compiler = compilerFactory.getCompiler();
        for (final Address address : user.getAddresses()) {
            String text = compiler.compile(personalConstants.addressTemplate(), address);
            addItem(new HTML(text), appConstants.delete(), new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    showDeleteAddressConfirmation(address);
                }
            });
        }
    }

    private void showDeleteAddressConfirmation(final Address address) {
        Bootbox.Dialog.create()
            .setMessage(personalConstants.confirmAddressDeletion())
            .setCloseButton(false)
            .setTitle(appConstants.deletionConfirmation())
            .addButton(appConstants.ok(), "btn-primary", new AlertCallback() {
                @Override
                public void callback() {
                    handlerManager.fireEvent(new DeleteAddressEvent(address.getId()));
                }})
            .addButton(appConstants.cancel(), "btn-default")
            .show();
    }

    @Override
    public IsWidget showAddItemForm() {
        addressForm.fill(user);
        return addressForm;
    }

    public HandlerRegistration addAddAddressEventHandler(AddAddressEventHandler handler) {
        return handlerManager.addHandler(AddAddressEvent.TYPE, handler);
    }

    public HandlerRegistration addDeleteAddressEventHandler(DeleteAddressEventHandler handler) {
        return handlerManager.addHandler(DeleteAddressEvent.TYPE, handler);
    }
}
