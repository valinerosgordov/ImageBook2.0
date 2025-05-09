package ru.minogin.core.client.security;

public interface PasswordGenerator {
	String generate();

	String generate(int len);
}
