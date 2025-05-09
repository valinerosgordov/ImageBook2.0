package ru.imagebook.shared.model;

import java.util.Date;

import ru.minogin.core.client.bean.BaseEntityBean;

public class StatusRequest extends BaseEntityBean {
	private static final long serialVersionUID = -5635806187276900887L;

	public static final String USER = "user";
	public static final String REQUEST = "request";
	public static final String STATE = "state";
	public static final String CODE = "code";
	public static final String DATE = "date";
	public static final String SOURCE = "source";

	public enum Source {
		APP("ЛК"), SITE_FORM("Форма");

		private String name;

		Source(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public StatusRequest() {
		setState(StatusRequestState.NEW);
		setDate(new Date());
	}

	public User getUser() {
		return get(USER);
	}

	public void setUser(User user) {
		set(USER, user);
	}

	public String getRequest() {
		return get(REQUEST);
	}

	public void setRequest(String request) {
		set(REQUEST, request);
	}

	public int getState() {
		return (Integer) get(STATE);
	}

	public void setState(int state) {
		set(STATE, state);
	}

	public String getCode() {
		return get(CODE);
	}

	public void setCode(String code) {
		set(CODE, code);
	}

	public Date getDate() {
		return get(DATE);
	}

	public void setDate(Date date) {
		set(DATE, date);
	}

	public String getSource() {
		return get(SOURCE);
	}

	public void setSource(String source) {
		set(SOURCE, source);
	}

	public void setSource(Source source) {
		setSource(source.toString());
	}
}
