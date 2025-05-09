package ru.imagebook.client.common.service.admin;

import ru.saasengine.client.service.app.AppService;

import com.google.inject.Singleton;

@Singleton
public class AppServiceImpl implements AppService {
	public static final String CODE = "3373a745-553a-47fb-a3fd-328dfd67b922";

	@Override
	public String getAppCode() {
		return CODE;
	}

	@Override
	public String getAppName() {
		return "Система управления"; // TODO localize
	}
}