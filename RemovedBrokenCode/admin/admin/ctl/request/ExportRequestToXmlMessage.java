package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ExportRequestToXmlMessage extends BaseMessage {   
    private static final long serialVersionUID = -5324706197448335169L;
    
    public ExportRequestToXmlMessage() {
        super(RequestMessages.EXPORT_REQUEST_TO_XML_RESULT);

        addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
    }
}