package ru.minogin.core.server.flow.remoting;

import java.util.List;

import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.remoting.RemotingService;
import ru.minogin.core.server.spring.SpringContextSupport;

public class RemotingServiceServlet extends XRemoteServiceServlet implements RemotingService {
	private static final long serialVersionUID = 3635300746276875382L;

	public static final String REMOTING_TX_SERVICE = "remotingTxService";

	@SuppressWarnings("unchecked")
	private <T> T getBean(String name) {
		return (T) SpringContextSupport.getBean(getServletContext(), name);
	}

	@Override
	public List<Message> send(Message message) throws Exception {
		RemotingTxService service = getBean(REMOTING_TX_SERVICE);
		FlowServletSupport support = new FlowServletSupport(service, perThreadRequest.get(),
				perThreadResponse.get(), getServletContext());
		return support.send(message);
	}
}
