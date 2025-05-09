package ru.imagebook.server.service;

import javax.servlet.http.HttpServletResponse;

import ru.imagebook.shared.model.Vendor;

public interface ActivationService {
	String USER_ID_PARAM = "a";
	String EMAIL_ID_PARAM = "b";
    String CODE_PARAM = "c";
    String SECRET = "aa3a02fa-0324-4aae-a333-100b5f8103f7";
    String EMAIL_ACTIVATION_URL_TEMPLATE = "http://%s/emailActivation?"
        + ActivationService.EMAIL_ID_PARAM + "=%s&" + ActivationService.CODE_PARAM + "=%s";

	void activateEmail(int emailId, String code);

	String getEmailActivationCode(int emailId);

    String getEmailActivationUrl(int emailId, Vendor vendor);

	void activate(int userId, int emailId, String code, HttpServletResponse response);
}
