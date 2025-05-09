package ru.imagebook.shared.model;

import java.util.Date;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Mailing extends BaseEntityBean {
	private static final long serialVersionUID = -8147245977275256050L;

	public static final String DATE = "date";
	public static final String NAME = "name";
    public static final String NAME_FROM = "nameFrom";
    public static final String EMAIL_FROM = "emailFrom";
	public static final String SUBJECT = "subject";
	public static final String CONTENT = "content";
	public static final String TYPE = "type";
	public static final String TOTAL = "total";
	public static final String SENT = "sent";
	public static final String REPORT = "report";
	public static final String STATE = "state";
	public static final String VENDOR = "vendor";

	public Mailing() {
		setDate(new Date());
		setType(MailingType.ALL);
		setTotal(0);
		setSent(0);
		setState(MailingState.NEW);
	}

	public Date getDate() {
		return get(DATE);
	}

	public void setDate(Date date) {
		set(DATE, date);
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

    public String getNameFrom() {
        return get(NAME_FROM);
    }

    public void setNameFrom(String nameFrom) {
        set(NAME_FROM, nameFrom);
    }

    public String getEmailFrom() {
        return get(EMAIL_FROM);
    }

    public void setEmailFrom(String emailFrom) {
        set(EMAIL_FROM, emailFrom);
    }

    public String getSubject() {
		return get(SUBJECT);
	}

	public void setSubject(String subject) {
		set(SUBJECT, subject);
	}

	public String getContent() {
		return get(CONTENT);
	}

	public void setContent(String content) {
		set(CONTENT, content);
	}

	public int getType() {
		return (Integer) get(TYPE);
	}

	public void setType(int type) {
		set(TYPE, type);
	}

	public int getTotal() {
		return (Integer) get(TOTAL);
	}

	public void setTotal(int total) {
		set(TOTAL, total);
	}

	public int getSent() {
		return (Integer) get(SENT);
	}

	public void setSent(int sent) {
		set(SENT, sent);
	}

	public String getReport() {
		return get(REPORT);
	}

	public void setReport(String report) {
		set(REPORT, report);
	}

	public int getState() {
		return (Integer) get(STATE);
	}

	public void setState(int state) {
		set(STATE, state);
	}
	
	public Vendor getVendor() {
		return get(VENDOR);
	}
	
	public void setVendor(Vendor vendor) {
		set(VENDOR, vendor);
	}
}
