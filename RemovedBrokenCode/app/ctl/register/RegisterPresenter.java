package ru.imagebook.client.app.ctl.register;

/**

 * @since  08.12.2014
 */
public interface RegisterPresenter {
	void onRegister(String email, String password, String captcha);
}
