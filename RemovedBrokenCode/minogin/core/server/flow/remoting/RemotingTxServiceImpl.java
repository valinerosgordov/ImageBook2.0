package ru.minogin.core.server.flow.remoting;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;

public class RemotingTxServiceImpl implements RemotingTxService {
	private final Dispatcher dispatcher;
	private final RemotingPostController postController;

	public RemotingTxServiceImpl(Dispatcher dispatcher, RemotingPostController postController) {
		this.dispatcher = dispatcher;
		this.postController = postController;
	}

	/* 
	 * Do not put any additional code like hibernate unproxying and unlazying into this method because
	 * it is transactional so all the bean changes will reflect to the database.
	 */
	@Override
	@Transactional
	public List<Message> txSend(Message message) throws Exception {
		dispatcher.send(message);

		return postController.getClientMessages();
	}
}
