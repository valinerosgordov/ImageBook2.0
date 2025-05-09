package ru.minogin.core.server.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.exception.Exceptions;

public class HasherImpl implements Hasher {
	@Override
	public String hash(String text) {
		if (text == null)
			throw new NullPointerException();

		return BCrypt.hashpw(text, BCrypt.gensalt());
	}

	@Override
	public boolean check(String text, String hash) {
		return BCrypt.checkpw(text, hash);
	}

	@Override
	public String md5(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(text.getBytes());
			byte[] digest = md.digest();

			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < digest.length; i++) {
				String hex = Integer.toHexString(0xFF & digest[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch (NoSuchAlgorithmException e) {
			return Exceptions.rethrow(e);
		}
	}
}
