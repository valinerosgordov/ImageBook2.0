package ru.minogin.core.client.security;

public class PasswordGeneratorImpl implements PasswordGenerator {
	private final XRandom random;

	public PasswordGeneratorImpl(XRandom random) {
		this.random = random;
	}

	@Override
	public String generate() {
		return generate(6);
	}

	@Override
	public String generate(int len) {
		int zeroCode = '0';
		int aCode = 'a';

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			char c;
			int k = random.nextInt(10 + 26);
			if (k < 10)
				c = (char) (zeroCode + k);
			else
				c = (char) (aCode + (k - 10));
			sb.append(c);
		}

		return sb.toString();
	}
}
