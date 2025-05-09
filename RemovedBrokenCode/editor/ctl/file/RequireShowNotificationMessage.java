package ru.imagebook.client.editor.ctl.file;

import ru.imagebook.client.editor.ctl.file.FileMessages;
import ru.imagebook.client.editor.ctl.order.OrderMessages;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.editor.NotificationType;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RequireShowNotificationMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String TYPE = "type";
	public static final String IMAGE_TYPE = "imageType";
	public static final String IS_SHOW_MESSAGE = "isShowMessage";

	RequireShowNotificationMessage() {}

	public RequireShowNotificationMessage(int type, int imageType, boolean isShowMessage) {
		super(FileMessages.REQIRE_SHOW_NOTIFICATION);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(TYPE, type);
		set(IMAGE_TYPE, imageType);
		set(IS_SHOW_MESSAGE, isShowMessage);
	}

	public Integer getType() {
		return (Integer) get(TYPE);
	}

	public Integer getImageLayoutType() {
		return (Integer) get(IMAGE_TYPE);
	}

	public boolean isShowMessage() {
		return (Boolean) get(IS_SHOW_MESSAGE);
	}
}
