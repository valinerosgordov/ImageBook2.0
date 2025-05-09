package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;

public class ConfirmMessageBox {
	private Listener<BaseEvent> noListener;
	
	public ConfirmMessageBox(String title, String message, final Listener<BaseEvent> listener) {
		MessageBox.confirm(title, message, new Listener<MessageBoxEvent>() {
			public void handleEvent(MessageBoxEvent mbe) {
				Button button = mbe.getButtonClicked();
				if (Dialog.YES.equals(button.getItemId())) {
					if (listener != null)
						listener.handleEvent(null);
				}
				else if (Dialog.NO.equals(button.getItemId())) {
					if (noListener != null)
						noListener.handleEvent(null);
				}
			}
		});
	}
	
	public void setNoListener(Listener<BaseEvent> noListener) {
		this.noListener = noListener;
	}
}
