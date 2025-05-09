package ru.minogin.core.client.flow.remoting;

import java.util.List;

import ru.minogin.core.client.flow.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemotingServiceAsync {
	void send(Message message, AsyncCallback<List<Message>> callback);
}
