package ru.minogin.core.server.flow.remoting;

import java.util.List;

import ru.minogin.core.client.flow.Message;

public interface RemotingTxService {
	List<Message> txSend(Message message) throws Exception;
}
