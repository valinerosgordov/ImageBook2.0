package ru.imagebook.server.web.admin;

import java.awt.Color;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ru.imagebook.server.service.DeliveryService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service.request.RequestService;
import ru.imagebook.server.service2.admin.SiteService;
import ru.imagebook.server.service2.admin.ZoneServerService;
import ru.imagebook.server.service2.weight.WeightService;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.Tag;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.file.TempFile;
import ru.minogin.core.server.flow.download.Downloads;

@Controller
public class AdminController {
	@Autowired
	private SiteService service;
	@Autowired
	private WeightService weightService;
	@Autowired
	private ZoneServerService zoneServerService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	RequestService requestService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLogin(String loginFailed, Model model) {
		Vendor vendor = vendorService.getVendorByCurrentSite();
		model.addAttribute("serviceName", vendor.getName());

		if (loginFailed != null)
			model.addAttribute("loginFailed", true);

		return "admin/login";
	}

	private float w3cColorBrightness(Color color) {
		return (float) ((color.getRed() * 299) + (color.getGreen() * 587) + (color.getBlue() * 114)) / 1000f;
	}

	private int w3cColorDifference(Color color1, Color color2) {
		return (Math.max(color1.getRed(), color2.getRed()) - Math.min(color1.getRed(), color2.getRed()))
				+ (Math.max(color1.getGreen(), color2.getGreen()) - Math.min(color1.getGreen(),
						color2.getGreen()))
				+ (Math.max(color1.getBlue(), color2.getBlue()) - Math.min(color1.getBlue(),
						color2.getBlue()));
	}

	private boolean w3cAreColorsVisibile(Color color1, Color color2) {
		return Math.abs(w3cColorBrightness(color1) - w3cColorBrightness(color2)) > 125
				&& w3cColorDifference(color1, color2) > 500;
	}

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String getIndex(Model model) {
		Vendor vendor = vendorService.getVendorByCurrentSite();
		model.addAttribute("serviceName", vendor.getName());

		List<Vendor> vendors = vendorService.loadVendors();

		StringBuffer styles = new StringBuffer();
		for (Vendor vendor2 : vendors) {
			String color = vendor2.getColor();
			try {
				Color vendorColor = new Color(Integer.parseInt(color, 16));
				String contr = "000000";
				if (!w3cAreColorsVisibile(vendorColor, new Color(Integer.parseInt(contr, 16)))) {
					contr = "FFFFFF";
				}
				styles.append(" .color-" + color + " { background-color: #" + color + "; color: #" + contr
						+ ";}");
			}
			catch (NumberFormatException e) {
				// do nothing
			}
		}
		model.addAttribute("colorStyles", styles);

		return "admin/index";
	}

	@RequestMapping(value = { "/sections" }, method = RequestMethod.GET)
	public String getSections(Model model) {
		Section section = service.loadSections();
		model.addAttribute("section", section);

		List<Tag> tags = service.loadTags();
		model.addAttribute("tags", tags);

		return "admin/sections";
	}

	@RequestMapping(value = { "/saveSection" }, method = RequestMethod.POST)
	public String saveSection(@RequestParam int id, @RequestParam String h1,
			@RequestParam String key,
			@RequestParam String title, @RequestParam String footer, @RequestParam int tagId, Model model) {
		Section section = service.updateSection(id, h1, key, title, footer, tagId);
		model.addAttribute("section", section);

		return "admin/saveSection";
	}

	@RequestMapping(value = { "/weightReport" }, method = RequestMethod.GET)
	public void getWeightReport(HttpServletRequest request, HttpServletResponse response) {
		TempFile tempFile = weightService.createWeightReport();
		Downloads.startDownload(tempFile.getFile(), tempFile.getName(), request, response);
	}

	@RequestMapping(value = { "/uploadZones" }, method = RequestMethod.POST)
	public void uploadZoneFile(@RequestParam("uploadField") MultipartFile file,
			HttpServletResponse response) {
		zoneServerService.uploadAndParseFile(file);
		response.setStatus(HttpServletResponse.SC_OK);

	}

	@RequestMapping(value = { "/posthouse.xls" }, method = RequestMethod.GET)
	public void getPosthouseExcel(HttpServletRequest request, HttpServletResponse response) {
		TempFile tempFile = deliveryService.createPosthouseExcel();
		Downloads.startDownload(tempFile.getFile(), tempFile.getName(), request, response);
	}

	@RequestMapping(value = { "/barcodes.doc" }, method = RequestMethod.GET)
	public void getBarcodes(HttpServletRequest request, HttpServletResponse response) {
		TempFile tempFile = deliveryService.createBarcodes();
		Downloads.startDownload(tempFile.getFile(), tempFile.getName(), request, response);
	}

	@RequestMapping(value = { "/generateAndSendRequests" }, method = RequestMethod.GET)
	public void getBarcodes(HttpServletResponse response) {
		try {
			requestService.generateAndSendRequests();
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			ServiceLogger.log(e);
			Exceptions.rethrow(e);
		}
	}
}
