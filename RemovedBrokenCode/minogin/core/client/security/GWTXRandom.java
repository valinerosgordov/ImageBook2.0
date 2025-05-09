package ru.minogin.core.client.security;

import com.google.gwt.user.client.Random;

public class GWTXRandom implements XRandom {
	@Override
	public int nextInt(int n) {
		return Random.nextInt(n);
	}
}
