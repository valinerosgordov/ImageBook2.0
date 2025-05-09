package ru.imagebook.client.app.view.bonus;

import com.google.gwt.event.shared.GwtEvent;


public class CreateBonusRequestEvent extends GwtEvent<CreateBonusRequestEventHandler> {
    public static Type<CreateBonusRequestEventHandler> TYPE = new Type<CreateBonusRequestEventHandler>();

    private final String requestValue;

    public CreateBonusRequestEvent(String requestValue) {
        this.requestValue = requestValue;
    }

    public String getRequestValue() {
        return requestValue;
    }

    @Override
    public Type<CreateBonusRequestEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CreateBonusRequestEventHandler handler) {
        handler.onBonusStatusRequest(this);
    }
}
