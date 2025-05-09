package ru.imagebook.server.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Vendor;

/**

 * @since 2015-02-13
 */
@Controller
@RequestMapping("/export")
public class ExportEmailController {

    @Autowired
    protected VendorService vendorService;

    @RequestMapping("/exportEmail/test")
    public String test() {
        return "admin2/export/exportEmailTest";
    }

    @ResponseBody
    @RequestMapping("/exportEmail/initApp")
    public InitAppBean initApp() {
        InitAppBean initAppBean = new InitAppBean();
        initAppBean.setCurrentVendor(vendorService.getVendorByCurrentSite());
        initAppBean.setVendorList(vendorService.loadVendors());
        return initAppBean;
    }

    protected static class InitAppBean {
        private Vendor currentVendor;
        private List<Vendor> vendorList;

        public Vendor getCurrentVendor() {
            return currentVendor;
        }

        public void setCurrentVendor(Vendor currentVendor) {
            this.currentVendor = currentVendor;
        }

        public List<Vendor> getVendorList() {
            return vendorList;
        }

        public void setVendorList(List<Vendor> vendorList) {
            this.vendorList = vendorList;
        }
    }
}
