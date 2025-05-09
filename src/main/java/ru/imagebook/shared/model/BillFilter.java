package ru.imagebook.shared.model;

import ru.minogin.core.client.bean.BaseEntityBean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BillFilter extends BaseEntityBean {
	private static final long serialVersionUID = 1L;

	private Integer numBill;
	private Integer totalLimitFrom, totalLimitTo;
	private Integer state;
	private Date dateFrom, dateTo;
	private String customer;
	private boolean isAdv;
	private Integer albumNumber;

	public BillFilter() {
		this.albumNumber = null;
		this.numBill = null;
		this.totalLimitFrom = null;
		this.totalLimitTo = null;
		this.state = null;
		this.dateFrom = null;
		this.dateTo = null;
		this.customer = null;
		this.isAdv = false;
	}

	public BillFilter (final Integer numBill, final Integer totalLimitFrom, final Integer totalLimitTo, final Integer state,
					  final Date dateFrom, final Date dateTo, final String customer, final boolean isAdv, final Integer albumNumber) {
		this.albumNumber = albumNumber;
		this.numBill = numBill;
		this.totalLimitFrom = totalLimitFrom;
		this.totalLimitTo = totalLimitTo;
		this.state = state;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.customer = customer;
		this.isAdv = isAdv;
	}

	public Integer getNumBill() {
		return numBill;
	}

	public Integer getTotalLimitFrom() {
		return totalLimitFrom;
	}

	public Integer getTotalLimitTo() {
		return totalLimitTo;
	}

	public Integer getState() {
		return state;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public String getCustomer() {
		return customer;
	}

	public boolean isAdv() {
		return isAdv;
	}

	public Integer getAlbumNumber() {
		return albumNumber;
	}
}
