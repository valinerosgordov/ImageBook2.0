package ru.imagebook.server.service2.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Vendor;

/**

 * @since 18.03.2015
 */
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private VendorService vendorService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        Vendor vendor = vendorService.getVendorByCurrentSite();
        setDefaultTargetUrl("http://" + vendor.getSite());
        super.onLogoutSuccess(request, response, authentication);
    }
}
