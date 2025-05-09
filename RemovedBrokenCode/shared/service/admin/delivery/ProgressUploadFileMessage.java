package ru.imagebook.shared.service.admin.delivery;

import ru.minogin.core.client.push.PushMessage;

public class ProgressUploadFileMessage implements PushMessage{
	private static final long serialVersionUID = -2576016723991009030L;

	private double percent;
	
	public ProgressUploadFileMessage(){}
	
	public ProgressUploadFileMessage(double percent) {
		this.setPercent(percent);
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getPercent() {
		return percent;
	}
}

