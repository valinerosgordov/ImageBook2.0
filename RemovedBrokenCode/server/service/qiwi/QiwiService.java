package ru.imagebook.server.service.qiwi;

import java.io.Writer;

import ru.imagebook.shared.model.User;

public interface QiwiService {
	void qiwiPay(User user, int billId, String userName, Writer writer);

	void qiwiSuccess(Writer writer);

	void qiwiFail(Writer writer);
}
