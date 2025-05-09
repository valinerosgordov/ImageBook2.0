package ru.imagebook.client.app.view.order;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by Rifat on 20.12.2016.
 */
public class OrderSelectedOrdersEvent extends GwtEvent<OrderSelectedOrdersEventHandler> {
    public static Type<OrderSelectedOrdersEventHandler> TYPE = new Type<OrderSelectedOrdersEventHandler>();

    public OrderSelectedOrdersEvent() {

    }

    @Override
    public Type<OrderSelectedOrdersEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(OrderSelectedOrdersEventHandler handler) {
        handler.onOrderSelectedOrders(this);
    }
}