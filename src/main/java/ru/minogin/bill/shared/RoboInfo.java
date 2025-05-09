package ru.minogin.bill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RoboInfo implements IsSerializable {
	private String url;
	private String username;
	private Integer sum;
	private Integer orderId;
	private String desc;
	private String crc;

	RoboInfo() {
	}

	public RoboInfo(String url, String username, Integer sum, Integer orderId, String desc,
			String crc) {
		this.url = url;
		this.username = username;
		this.sum = sum;
		this.orderId = orderId;
		this.desc = desc;
		this.crc = crc;
	}
	
	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public Integer getSum() {
		return sum;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public String getDesc() {
		return desc;
	}

	public String getCrc() {
		return crc;
	}
}
