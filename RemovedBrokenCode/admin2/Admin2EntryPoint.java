package ru.imagebook.client.admin2;

import ru.imagebook.client.admin2.ctl.Admin2Controller;
import ru.imagebook.client.admin2.view.Admin2ViewImpl;

import com.google.gwt.core.client.EntryPoint;

public class Admin2EntryPoint implements EntryPoint {
	@Override
	public void onModuleLoad() {
		Admin2Controller controller = new Admin2Controller(new Admin2ViewImpl());
		controller.start();
	}
}
