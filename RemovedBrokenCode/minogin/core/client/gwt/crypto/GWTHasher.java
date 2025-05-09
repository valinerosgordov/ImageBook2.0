package ru.minogin.core.client.gwt.crypto;

import ru.minogin.core.client.NotImplementedException;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.crypto.SHA512;

public class GWTHasher implements Hasher {
	@Override
	public String hash(String text) {
		if (text == null)
			throw new NullPointerException();
		
		return SHA512.encode(text);
	}

	@Override
	public boolean check(String text, String hash) {
		return SHA512.checkpw(text, hash);
	}
	
	@Override
	public String md5(String text) {
		throw new NotImplementedException();
	}
}
