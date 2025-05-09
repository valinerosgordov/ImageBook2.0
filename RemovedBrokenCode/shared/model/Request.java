package ru.imagebook.shared.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Request extends BaseEntityBean {
	private static final long serialVersionUID = -5877815823485274578L;

	public static final String NUMBER = "number";
	public static final String URGENT = "urgent";
	public static final String DATE = "date";
	public static final String ORDERS = "orders";
	public static final String STATE = "state";
	public static final String PAID = "paid";
	public static final String CONFIRMED = "confirmed";
	public static final String TOTAL = "total";
	public static final String TOTAL2 = "total2";
	public static final String BILL_NUMBER = "billNumber";
	public static final String BILL_DATE = "billDate";

	public Request() {
		setDate(new Date());
		setOrders(new HashSet<Order<?>>());
		setUrgent(false);
		setState(RequestState.REQUEST);
		setPaid(false);
		setConfirmed(false);
		setTotal(0);
		setTotal2(0);
	}

	public Integer getNumber() {
		return get(NUMBER);
	}

	public void setNumber(Integer number) {
		set(NUMBER, number);
	}
	
	public boolean isUrgent() {
		return (Boolean) get(URGENT);
	}

	public void setUrgent(boolean urgent) {
		set(URGENT, urgent);
	}

	public Date getDate() {
		return get(DATE);
	}

	public void setDate(Date date) {
		set(DATE, date);
	}

	public Set<Order<?>> getOrders() {
		return get(ORDERS);
	}

	public void setOrders(Set<Order<?>> orders) {
		set(ORDERS, orders);
	}

	public void addOrder(Order<?> order) {
		order.setRequest(this);
		getOrders().add(order);
	}

	public int getState() {
		return (Integer) get(STATE);
	}

	public void setState(int state) {
		set(STATE, state);
	}

	public boolean isPaid() {
		return (Boolean) get(PAID);
	}

	public void setPaid(boolean paid) {
		set(PAID, paid);
	}

	public boolean isConfirmed() {
		return (Boolean) get(CONFIRMED);
	}

	public void setConfirmed(boolean confirmed) {
		set(CONFIRMED, confirmed);
	}

	public void removeOrder(Order<?> order) {
		order.setRequest(null);
		getOrders().remove(order);
	}

	public int getTotal() {
		return (Integer) get(TOTAL);
	}

	public void setTotal(int total) {
		set(TOTAL, total);
	}

	public int getTotal2() {
		return (Integer) get(TOTAL2);
	}

	public void setTotal2(int total2) {
		set(TOTAL2, total2);
	}

	public String getBillNumber() {
		return get(BILL_NUMBER);
	}

	public void setBillNumber(String billNumber) {
		set(BILL_NUMBER, billNumber);
	}

	public Date getBillDate() {
		return get(BILL_DATE);
	}

	public void setBillDate(Date billDate) {
		set(BILL_DATE, billDate);
	}
}
