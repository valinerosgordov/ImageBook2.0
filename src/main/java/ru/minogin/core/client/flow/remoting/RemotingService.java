package ru.minogin.core.client.flow.remoting;

import java.util.List;

import ru.minogin.core.client.flow.Message;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("remotingService")
public interface RemotingService extends RemoteService {
	List<Message> send(Message message) throws Exception;
}
