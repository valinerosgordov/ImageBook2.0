package ru.imagebook.client.common.ctl.register;

import ru.imagebook.shared.model.Module;

public interface RegisterView {
	void showLinks(Module module);

	Module getModule();
	
	void showForm();

	void infoRegisterResult();

	void hideForm();

	void alertUserExists();

    void alertCaptchaIsInvalid();
}
