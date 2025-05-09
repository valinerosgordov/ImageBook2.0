package ru.saasengine.client.model.usersettings;

import java.util.*;

import ru.minogin.core.client.bean.BasePersistentBean;
import ru.minogin.core.client.rpc.Transportable;
import ru.saasengine.client.model.filter.Filter;
import ru.saasengine.client.model.ui.UserDesktop;

public class UserSettings extends BasePersistentBean {
	private static final long serialVersionUID = 201678321124192228L;

	public static final String TYPE_NAME = "app.user.UserSettings";

	private static final String LOCALE = "locale";
	private static final String FORM_LOCALES = "formLocales";
	private static final String CUSTOM_FORM_LOCALES = "customFormLocales";
	private static final String FIRST_VISIT = "firstVisit";
	private static final String SETTINGS = "settings";
	private static final String GUI_SETTINGS = "guiSettings";
	private static final String GRID_FILTERS = "gridFilters";
	private static final String USER_DESKTOP = "userDesktop";

	public UserSettings() {
		setFormLocales(new LinkedHashSet<String>());
		set(CUSTOM_FORM_LOCALES, new HashMap<String, Set<String>>());
		setFirstVisit(true);
		set(SETTINGS, new HashMap<String, Transportable>());
		set(GUI_SETTINGS, new HashMap<String, Transportable>());
		set(GRID_FILTERS, new HashMap<String, Filter>());
		setUserDesktop(new UserDesktop());
	}

	UserSettings(UserSettings prototype) {
		super(prototype);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public String getLocale() {
		return get(LOCALE);
	}

	public void setLocale(String locale) {
		set(LOCALE, locale);
	}

	public Set<String> getFormLocales() {
		return get(FORM_LOCALES);
	}

	public void setFormLocales(Set<String> formLocales) {
		set(FORM_LOCALES, formLocales);
	}

	public Map<String, Set<String>> getCustomFormLocales() {
		return get(CUSTOM_FORM_LOCALES);
	}

	public boolean isFirstVisit() {
		return (Boolean) get(FIRST_VISIT);
	}

	public void setFirstVisit(boolean firstVisit) {
		set(FIRST_VISIT, firstVisit);
	}

	public Map<String, Object> getGUISettings() {
		return get(GUI_SETTINGS);
	}

	public Map<String, Filter> getGridFilters() {
		return get(GRID_FILTERS);
	}

	public UserDesktop getUserDesktop() {
		return get(USER_DESKTOP);
	}

	public void setUserDesktop(UserDesktop userDesktop) {
		set(USER_DESKTOP, userDesktop);
	}

	@Override
	public UserSettings copy() {
		return new UserSettings(this);
	}
}
