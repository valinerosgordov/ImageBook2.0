package ru.imagebook.client.app.view.order;

import com.google.gwt.event.shared.GwtEvent;


public class PrepareToApplyCodeEvent extends GwtEvent<PrepareToApplyCodeEventHandler> {
    public static Type<PrepareToApplyCodeEventHandler> TYPE = new Type<PrepareToApplyCodeEventHandler>();

    private final int orderId;
    private final String code;
    private String deactivationCode;

    public PrepareToApplyCodeEvent(int orderId, String code) {
        this.orderId = orderId;
        this.code = code;
    }

    public PrepareToApplyCodeEvent(int orderId, String code, String deactivationCode) {
        this(orderId, code);
        this.deactivationCode = deactivationCode;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCode() {
        return code;
    }

    public String getDeactivationCode() {
        return deactivationCode;
    }

    @Override
    public Type<PrepareToApplyCodeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PrepareToApplyCodeEventHandler handler) {
        handler.onPrepareToApplyCode(this);
    }
}
