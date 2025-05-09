package ru.minogin.auth.server.service;

import org.apache.shiro.crypto.hash.Sha256Hash;

import java.util.Random;

public class SecurityUtil {
	/** Generates SHA-256 password hash. */
	public static String hashPassword(String password) {
// RandomNumberGenerator rng = new SecureRandomNumberGenerator();
// Object salt = rng.nextBytes(); // TODO
		String hashedPassword = new Sha256Hash(password).toHex();
		return hashedPassword;
	}

	public static String generateNumericPassword(int length) {
		Random random = new Random();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}
