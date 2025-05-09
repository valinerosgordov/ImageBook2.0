package ru.imagebook.client.calc;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class CalcEntryPoint implements EntryPoint {
	private final CalcInjector injector = GWT.create(CalcInjector.class);

	@Override
	public void onModuleLoad() {
		Calc calc = injector.getCalc();
		calc.start();
	}
}
