package ru.imagebook.client.app.view.order;

import com.google.gwt.event.shared.GwtEvent;


public class SetOrderParamsEvent extends GwtEvent<SetOrderParamsEventHandler> {
    public static Type<SetOrderParamsEventHandler> TYPE = new Type<SetOrderParamsEventHandler>();

    private final int orderId;
    private final Integer colorId;
    private final Integer coverLam;
    private final Integer pageLam;

    public SetOrderParamsEvent(int orderId, Integer colorId, Integer coverLam, Integer pageLam) {
        this.orderId = orderId;
        this.colorId = colorId;
        this.coverLam = coverLam;
        this.pageLam = pageLam;
    }

    public int getOrderId() {
        return orderId;
    }

    public Integer getColorId() {
        return colorId;
    }

    public Integer getCoverLam() {
        return coverLam;
    }

    public Integer getPageLam() {
        return pageLam;
    }

    @Override
    public Type<SetOrderParamsEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SetOrderParamsEventHandler handler) {
        handler.onSaveOrderParams(this);
    }
}
