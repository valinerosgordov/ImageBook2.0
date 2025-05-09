package ru.minogin.core.server.flow.remoting;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

public class FlowServlet extends SpringContextAwareServlet {
	private static final long serialVersionUID = -902487304698900020L;

	public static final String REMOTING_TX_SERVICE = "remotingTxService";

	private ThreadLocal<HttpServletRequest> perThreadRequest = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> perThreadResponse = new ThreadLocal<HttpServletResponse>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		prepareRequestResponse(request, response);

		perThreadRequest.set(request);
		perThreadResponse.set(response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		prepareRequestResponse(request, response);

		perThreadRequest.set(request);
		perThreadResponse.set(response);
	}

	private void prepareRequestResponse(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
	}

	protected List<Message> send(Message message) {
		RemotingTxService service = getBean(REMOTING_TX_SERVICE);
		FlowServletSupport support = new FlowServletSupport(service, perThreadRequest.get(),
				perThreadResponse.get(), getServletContext());
		try {
			return support.send(message);
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}
}
