package ru.imagebook.client.common.service.editor;

import ru.saasengine.client.service.app.AppService;

import com.google.inject.Singleton;

@Singleton
public class AppServiceImpl implements AppService {
	@Override
	public String getAppCode() {
		return "3373a745-553a-47fb-a3fd-328dfd67b922";
	}

	@Override
	public String getAppName() {
		return "Редактор альбомов"; // TODO localize
	}
}
