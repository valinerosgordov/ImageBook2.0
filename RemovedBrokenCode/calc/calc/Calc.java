package ru.imagebook.client.calc;

import ru.imagebook.client.calc.ctl.CalcController;
import ru.imagebook.client.calc.ctl.CalcMessages;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.remoting.RemotingPostController;

import com.google.inject.Inject;

public class Calc {
	private final Dispatcher dispatcher;
	private CalcController calcController;
	private RemotingPostController remotingPostController;

	@Inject
	public Calc(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Inject
	public void setCalcController(CalcController calcController) {
		this.calcController = calcController;
	}

	@Inject
	public void setRemotingPostController(RemotingPostController remotingPostController) {
		this.remotingPostController = remotingPostController;
	}

	public void start() {
		calcController.registerHandlers();

		remotingPostController.registerHandlers();

		dispatcher.send(CalcMessages.START);
	}
}
