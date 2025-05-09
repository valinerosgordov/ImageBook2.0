package ru.imagebook.server.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Vendor;


@Controller
@RequestMapping("/export")
public class ExportApi {

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @RequestMapping(value = "/exportEmail", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public void exportEmailToExcel(@RequestParam("vendorId") Integer vendorId,
                                   @RequestParam("commonUsers") Boolean commonUsers,
                                   @RequestParam("photographers") Boolean photographers,
                                   HttpServletResponse response) {
        Vendor vendor = vendorService.getVendorById(vendorId);
        userService.exportUserEmailsToExcel(vendor, commonUsers, photographers, response);
    }
}
