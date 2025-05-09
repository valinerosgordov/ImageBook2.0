package ru.minogin.core.server.security;

import java.util.Random;

import ru.minogin.core.client.security.XRandom;

public class XRandomImpl implements XRandom {
	private Random random = new Random();

	@Override
	public int nextInt(int n) {
		return random.nextInt(n);
	}
}
