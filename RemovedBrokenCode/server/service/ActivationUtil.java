package ru.imagebook.server.service;

import ru.minogin.core.client.crypto.Hasher;

@Deprecated
public class ActivationUtil {
	public static String getActivationCode(Hasher hasher, int userId, int emailId) {
		return hasher.md5(ActivationService.SECRET + userId + emailId);
	}
}
