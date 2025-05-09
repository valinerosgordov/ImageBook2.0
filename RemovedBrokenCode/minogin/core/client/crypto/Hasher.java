package ru.minogin.core.client.crypto;

public interface Hasher {
	String hash(String text);
	
	boolean check(String text, String hash);
	
	String md5(String text);
}
