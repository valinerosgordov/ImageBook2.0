package ru.imagebook.server.web.editor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.minogin.core.server.gwt.ClientParametersWriter;

@Controller
public class EditorController {
	@Autowired
	private VendorService vendorService;
	@Autowired
	private ServerConfig serverConfig;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLogin(HttpServletRequest request, String loginFailed,
			Model model) {
		model.addAttribute("contextUrl", serverConfig.getEditorPrefix());

		Vendor vendor = vendorService.getVendorByCurrentSite();
		model.addAttribute("serviceName", vendor.getName());

		if (loginFailed != null) {
			model.addAttribute("loginFailed", true);
		}	

		ClientParametersWriter writer = new ClientParametersWriter();
		writer.setParam("login", true);
		writer.write(model);

		return "editor/login";
	}

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String getIndex(HttpServletRequest request, Model model) {
		model.addAttribute("editorPrefix", serverConfig.getEditorPrefix());
		
		Vendor vendor = vendorService.getVendorByCurrentSite();
		model.addAttribute("serviceName", vendor.getName());

        if (vendor.getType() == VendorType.IMAGEBOOK) {
            model.addAttribute("yandexMetrika", true);
        }

		return "editor/index";
	}
}
