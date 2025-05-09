package ru.saasengine.client.model.lang;

import ru.minogin.core.client.common.*;

public class ScriptBuilder implements Builder<Script> {
	@Override
	public Script newInstance() {
		return new Script();
	}
}
