package ru.imagebook.server.web.admin2;

import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.imagebook.server.service.CodesImportService;
import ru.imagebook.server.service.OrderService;

@Controller
public class Admin2Controller {
	@Autowired
	private CodesImportService codesImportService;
	@Autowired
	OrderService orderService;

	@RequestMapping(value = { "/login" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String login(String loginFailed, Model model) {
		if (loginFailed != null) {
			model.addAttribute("loginFailed", true);
		}
		return "admin2/login";
	}

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String index() {
		return "admin2/index";
	}

	@RequestMapping(value = { "/uploadCodes" }, method = RequestMethod.POST)
	public void uploadContacts(InputStream inputStream, HttpServletResponse response) {
		codesImportService.uploadBarcodes(inputStream);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@RequestMapping(value = { "/upgrade" }, method = RequestMethod.GET)
	public void upgrade(HttpServletResponse response, ServletOutputStream os) throws Exception {
		os.println("Upgrade finished");
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
