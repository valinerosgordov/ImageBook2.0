package ru.imagebook.shared.model;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import ru.minogin.core.client.bean.BaseEntityBean;

public class OrderFilter extends BaseEntityBean {
	private static final long serialVersionUID = -2806022835079437958L;

	public static final String STATES = "states";
	public static final String VENDOR = "vendor";
	public static final String BONUS_CODE = "bonusCode";

	public OrderFilter() {
		setStates(new HashSet<Integer>());
	}

	public void apply(OrderFilter filter) {
		setStates(filter.getStates());
		setVendor(filter.getVendor());
		setBonusCode(filter.getBonusCode());
	}

	public Set<Integer> getStates() {
		return get(STATES);
	}

	public void setStates(Set<Integer> states) {
		set(STATES, states);
	}

	public Vendor getVendor() {
		return get(VENDOR);
	}

	public void setVendor(Vendor vendor) {
		set(VENDOR, vendor);
	}

	public String getBonusCode() {return get(BONUS_CODE); }

	public void setBonusCode( String bonusCode) { set(BONUS_CODE, bonusCode);}
}
