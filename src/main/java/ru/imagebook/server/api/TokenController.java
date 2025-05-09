package ru.imagebook.server.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service2.security.ImagebookTokenBasedRememberMeServices;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;

/**

 * @since 2014-11-28
 */
@Controller
@RequestMapping("/token")
public class TokenController {
    protected final Logger log = Logger.getLogger(TokenController.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PersistentTokenRepository tokenRepository;

    @Autowired
    private ImagebookTokenBasedRememberMeServices rememberMeServices;

    @Autowired
    private VendorService vendorService;

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    public String verifyToken(@RequestParam("series") String series, @RequestParam("token") String presentedToken) {
        PersistentRememberMeToken token = tokenRepository.getTokenForSeries(series);
        if (token == null || !presentedToken.equals(token.getTokenValue())) {
            return null;
        }
        String username = rememberMeServices.getUsernameFromToken(token);
        Vendor vendor = rememberMeServices.getVendorFromToken(token);
        User user = userService.getUser(username, vendor);
        return user != null ? user.getUserName() : null;
    }

    @RequestMapping(value = "/allocate", method = RequestMethod.POST)
    @ResponseBody
    public String allocateToken(@RequestParam("username") String username,
                                @RequestParam("vendorKey") String vendorKey,
                                @RequestParam("vendorPassword") String vendorPassword) {
        Vendor vendor = vendorService.authenticateVendor(vendorKey, vendorPassword);
        User user = userService.getUser(username, vendor);
        if (user == null) {
            log.warn(String.format("User with username='%s' and vendor='%s' doesn't exist", username, vendor.getKey()));
            return null;
        }
        PersistentRememberMeToken token = rememberMeServices.generatePersistentToken(username, vendor);
        tokenRepository.createNewToken(token);
        return token.getSeries() + ":" + token.getTokenValue();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void removeTokens(@RequestParam("series") String series, @RequestParam("token") String presentedToken) {
        PersistentRememberMeToken token = tokenRepository.getTokenForSeries(series);
        if (token == null || !presentedToken.equals(token.getTokenValue())) {
            return;
        }
        tokenRepository.removeUserTokens(token.getUsername());
    }
}
