package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class PushMessage extends BaseMessage {
    private static final long serialVersionUID = 3359377949765852555L;

    public PushMessage() {
        super(AuthMessages.PUSH);

        addAspects(RemotingAspect.CLIENT);
    }
}
