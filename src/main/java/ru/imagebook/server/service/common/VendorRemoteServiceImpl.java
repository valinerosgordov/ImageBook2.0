package ru.imagebook.server.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.common.service.VendorRemoteService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Vendor;


@Service
public class VendorRemoteServiceImpl implements VendorRemoteService {
    @Autowired
    private VendorService vendorService;

    @Transactional
    @Override
    public Vendor getVendor() {
        return vendorService.getVendorByCurrentSite();
    }
}
