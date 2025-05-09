package ru.imagebook.shared.model.app;

import java.io.Serializable;

public class PickPointData implements Serializable {

	private int cost;
	private int timeMin;
	private int timeMax;
	private String postamateID;
	private String rateZone;
	private Double trunkCoeff;
	private String address;
	private int weightGr;


	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getTimeMin() {
		return timeMin;
	}

	public void setTimeMin(int timeMin) {
		this.timeMin = timeMin;
	}

	public int getTimeMax() {
		return timeMax;
	}

	public void setTimeMax(int timeMax) {
		this.timeMax = timeMax;
	}

	public String getPostamateID() {
		return postamateID;
	}

	public void setPostamateID(String postamateID) {
		this.postamateID = postamateID;
	}

	public String getRateZone() {
		return rateZone;
	}

	public void setRateZone(String rateZone) {
		this.rateZone = rateZone;
	}

	public Double getTrunkCoeff() {
		return trunkCoeff;
	}

	public void setTrunkCoeff(Double trunkCoeff) {
		this.trunkCoeff = trunkCoeff;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getWeightGr() {
		return weightGr;
	}

	public void setWeightGr(int weightGr) {
		this.weightGr = weightGr;
	}

	@Override
	public String toString() {
		return "PickPointData{" +
				"cost=" + cost +
				", timeMin=" + timeMin +
				", timeMax=" + timeMax +
				", postamateID='" + postamateID + '\'' +
				", rateZone='" + rateZone + '\'' +
				", trunkCoeff=" + trunkCoeff +
				", address='" + address + '\'' +
				", weightGr=" + weightGr +
				'}';
	}
}
