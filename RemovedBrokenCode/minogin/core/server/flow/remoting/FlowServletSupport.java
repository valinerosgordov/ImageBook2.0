package ru.minogin.core.server.flow.remoting;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.ServletAspect;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.hibernate.Dehibernator;

public class FlowServletSupport {
	private final RemotingTxService service;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final ServletContext context;

	public FlowServletSupport(RemotingTxService service,
			HttpServletRequest request, HttpServletResponse response,
			ServletContext context) {
		this.service = service;
		this.request = request;
		this.response = response;
		this.context = context;
	}

	public List<Message> send(Message message) throws Exception {
		try {
			if (!message.hasAspect(RemotingAspect.REMOTE))
				throw new AccessDeniedError(
						"Non remote message being sent via remoting servlet.");

			if (message.hasAspect(ServletAspect.SERVLET)) {
				RemoteServletAspect.setRequest(message, request);
				RemoteServletAspect.setResponse(message, response);
				RemoteServletAspect.setContext(message, context);
			}

			List<Message> clientMessages = service.txSend(message);

			for (Message clientMessage : clientMessages) {
				if (clientMessage.hasAspect(HibernateAspect.HIBERNATE)) {
					new Dehibernator().clean(clientMessage);
					// new Hibernate().unhibernate(clientMessage);
				}
			}

			return clientMessages;
		}
		catch (Exception e) {
			ServiceLogger.log(e);

			if (e instanceof MessageError) {
				MessageError messageError = (MessageError) e;
				Message errorMessage = messageError.getErrorMessage();
				errorMessage.addAspects(RemotingAspect.CLIENT);
				errorMessage.reply(message);
				List<Message> clientMessages = new ArrayList<Message>();
				clientMessages.add(errorMessage);
				return clientMessages;
			}
			else
				throw e;
		}
	}
}
