package ru.imagebook.server.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectController {
	@RequestMapping(value = "/newyear", method = RequestMethod.GET)
	public String get() {
		return "redirect:http://imagebook.ru/new_year";
	}
}
