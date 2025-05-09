package ru.imagebook.shared.service.admin.delivery;

import ru.minogin.core.client.push.PushMessage;

public class ProgressParseFileMessage implements PushMessage{
	private static final long serialVersionUID = 6299196734252713868L;
	
	private double percent;
	
	public ProgressParseFileMessage(){}
	
	public ProgressParseFileMessage(double percent) {
		this.setPercent(percent);
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getPercent() {
		return percent;
	}
}
