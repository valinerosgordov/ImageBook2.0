package ru.imagebook.server.ctl.flash;

import java.io.Writer;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ShowPreviewMessage extends BaseMessage {
    public static final String ORDER_ID = "orderId";
    public static final String WRITER = "writer";

    public ShowPreviewMessage(int orderId, Writer writer) {
        super(PreviewMessages.SHOW_PREVIEW);

        addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

        set(ORDER_ID, orderId);
        set(WRITER, writer);
    }

    public int getOrderId() {
        return (Integer) get(ORDER_ID);
    }

    public Writer getWriter() {
        return get(WRITER);
    }
}

