package ru.minogin.core.client.model;

public enum OrderDir {
	ASC,
	DESC;
	
	public OrderDir getInvertedDir() {
		return this == ASC ? DESC : ASC;
	}
}
