package ru.imagebook.server.web;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Objects;

import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.server.service.BillService;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.YaKassaService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.common.AccessDeniedError;

@Controller
@RequestMapping("/ya_kassa")
public class YaKassaController extends MainController {
    private static final Logger LOG = Logger.getLogger(YaKassaController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BillService billService;

    @Autowired
    private YaKassaService yaKassaService;

	@Override
	protected void layout(Model model, PageModel page) {
        super.layout(model, page);
        page.setWide(true);

        model.addAttribute("fullWidth", true);
    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public String yandexKassaPay(@RequestParam int billId, Model model) {
        User user = authService.getCurrentUser();

        Bill bill = getBillById(billId);
        if (!bill.getUser().equals(user)) {
            throw new AccessDeniedError();
        }

        orderService.setComputedValues(bill);

        Vendor vendor = user.getVendor();
        model.addAttribute("vendor", vendor);
        model.addAttribute("sum", bill.getTotal());
        model.addAttribute("customerNumber", user.getUserName());
        model.addAttribute("orderNumber", bill.getId());

        String cpsEmail = user.getFirstEmail() != null ? user.getFirstEmail().getEmail() : null;
        if (cpsEmail != null) {
            model.addAttribute("cps_email", cpsEmail);
        }

        String cpsPhone = user.getFirstPhone() != null ? user.getFirstPhone().getPhone() : null;
        if (cpsPhone != null) {
            model.addAttribute("cps_phone", cpsPhone);
        }
        model.addAttribute("customerContact", Objects.firstNonNull(cpsEmail, cpsPhone));

        return "yaPay";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String yandexKassaCheckOrder(@RequestParam("orderNumber") String orderNumber,
                                        @RequestParam Map<String, Object> params, Model model) {
        LOG.info(String.format(">> Yandex.Kassa checkOrder [orderNumber=%s,params=%s]", orderNumber, params));

        Bill bill = getBillById(Integer.parseInt(orderNumber));
        yaKassaService.processRequest(params, model, bill);

        LOG.info(String.format("<< Yandex.Kassa checkOrder [orderNumber=%s]", orderNumber));
        return "yaResponse";
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public String yandexKassaPaymentAviso(@RequestParam("orderNumber") String orderNumber,
                                          @RequestParam Map<String, Object> params, Model model) {
        LOG.info(String.format(">> Yandex.Kassa paymentAviso [orderNumber=%s,params=%s]", orderNumber, params));

        int billId = Integer.parseInt(orderNumber);
        Bill bill = getBillById(billId);
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        boolean isRequestValid = yaKassaService.processRequest(params, model, bill);
        if (isRequestValid) {
            billService.markPaid(billId);
            LOG.info("Bill marked as paid: " + billId);
            yaKassaService.notifyBillPaid(vendor, bill);
        }

        LOG.info(String.format("<< Yandex.Kassa paymentAviso [orderNumber=%s]", orderNumber));
        return "yaResponse";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String yandexKassaSuccess(@RequestParam("orderNumber") String orderNumber, Model model) {
        Vendor vendor = getVendorByBillId(Integer.parseInt(orderNumber));
        return "redirect:http://" + vendor.getOfficeUrl();
    }

    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public String yandexKassaFail(@RequestParam("orderNumber") String orderNumber, Model model) {
        Vendor vendor = getVendorByBillId(Integer.parseInt(orderNumber));
        model.addAttribute("vendor", vendor);
        return "yaFail";
    }

    private Bill getBillById(int billId) {
        Bill bill = orderService.getBill(billId);
        if (bill == null) {
            throw new RuntimeException("No such bill: " + billId);
        }
        return bill;
    }

    private Vendor getVendorByBillId(int billId) {
        Bill bill = getBillById(billId);
        User user = bill.getUser();
        return user.getVendor();
    }
}