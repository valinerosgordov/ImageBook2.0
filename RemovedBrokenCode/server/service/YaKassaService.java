package ru.imagebook.server.service;

import java.util.Map;

import org.springframework.ui.Model;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Vendor;


public interface YaKassaService {
    boolean processRequest(Map<String, Object> params, Model model, Bill bill);

    boolean checkMD5(Map<String, Object> params, String shopPassword);

    void notifyBillPaid(Vendor vendor, Bill bill);
}
