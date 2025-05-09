package ru.imagebook.shared.model;

public interface Role {
	public static final int USER = 1;
	public static final int ADMIN = 2;
	public static final int OPERATOR = 3;
	public static final int ROOT = 4;
	public static final int SITE_ADMIN = 5;
	public static final int DELIVERY_MANAGER = 6;
	public static final int FINISHING_MANAGER = 7;
	public static final int VENDOR = 8;

	public static final String TYPE = "type";

	int getType();

	void setType(int type);

	String getStringType();
}
