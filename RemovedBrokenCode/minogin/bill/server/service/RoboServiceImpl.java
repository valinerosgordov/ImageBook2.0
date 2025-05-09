package ru.minogin.bill.server.service;

import org.springframework.stereotype.Service;

import ru.minogin.bill.shared.RoboConfig;
import ru.minogin.bill.shared.RoboInfo;
import ru.minogin.util.server.freemarker.FreeMarker;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

@Service
public class RoboServiceImpl implements RoboService {
	@Override
	public RoboInfo createInfo(RoboConfig config, int orderId, int sum, String desc) {
		String username = config.getUsername();
		String code = username + ":" + sum + ":" + orderId + ":"
				+ config.getPassword1();
		String crc = Hashing.md5().newHasher().putString(code, Charsets.UTF_8)
				.hash().toString();
		return new RoboInfo(Robo.URL, username, sum, orderId, desc, crc);
	}

	@Override
	public String createHTML(RoboConfig config, int orderId, int sum, String desc) {
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("info", createInfo(config, orderId, sum, desc));
		freeMarker.set("url", Robo.URL);
		return freeMarker.process("robo.ftl", "ru");
	}

	@Override
	public void checkResult(RoboConfig config, int orderId, String sum, String crc) {
		String code = sum + ":" + orderId + ":" + config.getPassword2();
		String correctCRC = Hashing.md5().newHasher()
				.putString(code, Charsets.UTF_8).hash().toString();
		if (!correctCRC.equalsIgnoreCase(crc))
			throw new RuntimeException("CRC check failed.");
	}

	@Override
	public void checkSuccess(RoboConfig config, int orderId, String sum, String crc) {
		String code = sum + ":" + orderId + ":" + config.getPassword1();
		String correctCRC = Hashing.md5().newHasher()
				.putString(code, Charsets.UTF_8).hash().toString();
		if (!correctCRC.equalsIgnoreCase(crc))
			throw new RuntimeException("CRC check failed.");
	}
}
