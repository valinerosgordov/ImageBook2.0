package ru.imagebook.client.calc;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules( { CalcModule.class })
public interface CalcInjector extends Ginjector {
	Calc getCalc();
}
