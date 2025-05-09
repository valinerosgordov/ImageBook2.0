package ru.imagebook.client.app.view.personal.address;

import com.google.gwt.event.shared.GwtEvent;


public class DeleteAddressEvent extends GwtEvent<DeleteAddressEventHandler> {
    public static Type<DeleteAddressEventHandler> TYPE = new Type<DeleteAddressEventHandler>();

    private final Integer addressId;

    public DeleteAddressEvent(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    @Override
    public Type<DeleteAddressEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DeleteAddressEventHandler handler) {
        handler.onDeleteAddress(this);
    }
}
