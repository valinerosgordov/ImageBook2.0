package ru.imagebook.client.editor.ctl.file;

import ru.imagebook.client.editor.ctl.file.FileMessages;
import ru.imagebook.shared.model.editor.ImageLayoutType;
import ru.imagebook.shared.model.editor.NotificationType;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ShowNotificationMessage extends BaseMessage {
	private static final long serialVersionUID = 3L;

	public static final String TYPE = "type";
	public static final String IMAGE_TYPE = "imageType";
	public static final String CHECK_ONLY_FOR_COMMON_ORDER = "checkOnlyForCommonOrder";

	ShowNotificationMessage() {}

	public ShowNotificationMessage(int type, int imageType, boolean checkOnlyForCommonOrder) {
		super(FileMessages.SHOW_NOTIFICATION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(TYPE, type);
		set(IMAGE_TYPE, imageType);
		set(CHECK_ONLY_FOR_COMMON_ORDER, checkOnlyForCommonOrder);
	}

	public Integer getType() {
		return (Integer) get(TYPE);
	}

	public Integer getImageLayoutType() {
		return (Integer) get(IMAGE_TYPE);
	}

	public boolean isCheckOnlyForCommonOrder() { return get(CHECK_ONLY_FOR_COMMON_ORDER); }
}
