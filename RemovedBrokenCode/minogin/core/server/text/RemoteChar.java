package ru.minogin.core.server.text;

import ru.minogin.core.client.text.Char;

public class RemoteChar extends Char {
	@Override
	public boolean isLetter(char c) {
		return Character.isLetter(c);
	}
}
