package ru.minogin.core.server.sms;

import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.http.XHttpClient;

public class SmsSender {
	private static final String URL = "http://gate.mobilmoney.ru:80";

	private final String username;
	private final String password;
	private final String from;

	public SmsSender(String username, String password, String from) {
		this.username = username;
		this.password = password;
		this.from = from;
	}

	public void send(String to, String content) {
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("username", username);
		freeMarker.set("password", password);
		freeMarker.set("from", from);
		freeMarker.set("to", to);
		freeMarker.set("content", content);
		String xml = freeMarker.process("request.xml", Locales.RU);

		XHttpClient client = new XHttpClient();
		client.postXml(URL, xml);
	}
}
