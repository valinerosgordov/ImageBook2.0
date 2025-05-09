package ru.imagebook.client.editor.ctl.order;

import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

import java.util.Date;

public class ShowEditorCommonErrorMessage extends BaseMessage {
    private static final long serialVersionUID = 1L;
    public static final String ERROR_MESSAGE = "errorMessage";

    ShowEditorCommonErrorMessage() {
    }

    public ShowEditorCommonErrorMessage(final String errorMessage) {
        super(OrderMessages.SHOW_EDITOR_COMMON_MESSAGE);
        addAspects(RemotingAspect.CLIENT);
        //addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

        set(ERROR_MESSAGE, errorMessage);
    }

    public String getErrorMessage() { return get(ERROR_MESSAGE); }
}
