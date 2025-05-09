package ru.imagebook.server.web.flash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.imagebook.server.service2.flash.FlashXml;
import ru.imagebook.server.service2.flash.XmlFlashService;

@Controller
public class FlashController {
	@Autowired
	private XmlFlashService service;

	@RequestMapping(value = "/xml/{orderCode}/{width}", method = RequestMethod.GET)
	public String getXml(@PathVariable String orderCode, @PathVariable int width, Model model) {
		FlashXml xml = service.createFlashXml(orderCode, width);
		model.addAttribute("xml", xml);

		return "flash/flashXml";
	}
}
