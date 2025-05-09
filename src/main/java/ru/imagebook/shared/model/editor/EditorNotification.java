package ru.imagebook.shared.model.editor;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.bean.BaseEntityBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EditorNotification extends BaseEntityBean {
	private static final long serialVersionUID = 1L;

	public static final String USER = "user_id";
	public static final String TYPE = "type";

	public EditorNotification() {}

	public EditorNotification(int userId, int type) {
		setUser(userId);
		setType(type);
	}

	public Integer getUser() { return get(USER); }

	public void setUser(Integer user) { set(USER, user); }

	public void setType(int type) { set(TYPE, type); }

	public int getType() { return get(TYPE); }
}
