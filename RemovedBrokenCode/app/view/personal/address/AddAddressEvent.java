package ru.imagebook.client.app.view.personal.address;

import com.google.gwt.event.shared.GwtEvent;

import ru.imagebook.shared.model.Address;


public class AddAddressEvent extends GwtEvent<AddAddressEventHandler> {
    public static Type<AddAddressEventHandler> TYPE = new Type<AddAddressEventHandler>();

    private final Address address;

    public AddAddressEvent(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public Type<AddAddressEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddAddressEventHandler handler) {
        handler.onAddAddress(this);
    }
}
