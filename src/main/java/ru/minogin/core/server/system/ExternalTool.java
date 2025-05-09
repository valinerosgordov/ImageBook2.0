package ru.minogin.core.server.system;

public abstract class ExternalTool {
	protected Exec createExec() {
		return new Exec();
	}
}
