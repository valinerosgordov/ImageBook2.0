package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.servlet.SimpleCaptchaServlet;
import nl.captcha.text.producer.NumbersAnswerProducer;

public class ExtendedCaptchaServlet extends SimpleCaptchaServlet {
	public static final String CAPTCHA_ATTRIBUTE_NAME = "simpleCaptcha";

	private static final long serialVersionUID = -4012849212581426604L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();

		int width = Integer.valueOf(this.getInitParameter("width"));
		int height = Integer.valueOf(this.getInitParameter("height"));

		Captcha captcha = new Captcha.Builder(width, height)
			.addText(new NumbersAnswerProducer())
			.gimp()
			.addNoise()
			.addBackground(new GradiatedBackgroundProducer())
			.build();

		CaptchaServletUtil.writeImage(resp, captcha.getImage());
		session.setAttribute(CAPTCHA_ATTRIBUTE_NAME, captcha);
	}
}