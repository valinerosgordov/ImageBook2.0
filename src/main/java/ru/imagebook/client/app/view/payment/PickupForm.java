package ru.imagebook.client.app.view.payment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.User;


public class PickupForm extends AbstractPickupForm {
    interface PickupUiBinder extends UiBinder<Widget, PickupForm> {
    }
    private static PickupUiBinder uiBinder = GWT.create(PickupUiBinder.class);

    public PickupForm(User user) {
        super(user);
    }

    @Override
    public Widget initWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @Override
    public void fetch(Address address) {
        super.fetch(address);
        address.setCountry(Address.DEFAULT_COUNTRY);
    }

    @Override
    public void fill(Address address) {
        if (address.getId() == null) {
            clear();
            super.setDefaultValues();
            return;
        }
        super.fill(address);
    }
}
