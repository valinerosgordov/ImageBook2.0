package ru.imagebook.client.common.ctl.user;

import ru.imagebook.shared.model.Module;

public interface UserView {
	void showRecoverForm(Module module);

	void hideRecoverForm();

	void infoRecoverMailSent();
}
