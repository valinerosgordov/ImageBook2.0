package ru.imagebook.client.admin.ctl.mailing;

import java.util.List;

import ru.imagebook.shared.model.Mailing;

public interface MailingView {
	void hideAddForm();

	void hideEditForm();

	void showMailings(List<Mailing> mailings);

	void showSection(String locale, String email);

	void hideTestForm();

	void enableSendButton();
}
