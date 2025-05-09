package ru.imagebook.client.calc;

import ru.imagebook.client.calc.ctl.CalcView;
import ru.imagebook.client.calc.view.CalcViewImpl;
import ru.minogin.core.client.app.ApplicationModule;
import ru.minogin.core.client.flow.BaseDispatcher;
import ru.minogin.core.client.flow.Dispatcher;

public class CalcModule extends ApplicationModule {
	@Override
	protected void configure() {
		bind(Dispatcher.class).to(BaseDispatcher.class);

		bind(CalcView.class).to(CalcViewImpl.class);
	}
}
