package ru.imagebook.server.service2.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.*;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.util.server.spring.SpringUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;


public class ImagebookTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {
    private static final int MIN_COOKIE_TOKENS_LENGTH = 2;
    private static final String USER_KEY_DELIMITER = "::";

    private PersistentTokenRepository tokenRepository;
    private VendorService vendorService;

    public ImagebookTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService,
                                                 PersistentTokenRepository tokenRepository,
                                                 VendorService vendorService) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
        this.vendorService = vendorService;
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
                                                 HttpServletResponse response) {
        if (cookieTokens.length < MIN_COOKIE_TOKENS_LENGTH) {
            throw new InvalidCookieException("Cookie token did not contain " + MIN_COOKIE_TOKENS_LENGTH +
                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        PersistentRememberMeToken token = tokenRepository.getTokenForSeries(presentedSeries);
        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }

        // We have a match for this user/series combination
        if (!presentedToken.equals(token.getTokenValue())) {
            // Token doesn't match series value. Delete all logins for this user and throw an exception to warn them.
            tokenRepository.removeUserTokens(token.getUsername());
            throw new CookieTheftException(messages.getMessage("PersistentTokenBasedRememberMeServices.cookieStolen",
                    "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
        }

        if (isTokenExpired(token)) {
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }

        // Check user exists.
        String username = getUsernameFromToken(token);
        return getUserDetailsService().loadUserByUsername(username);
    }

    protected boolean isTokenExpired(PersistentRememberMeToken token) {
        long tokenExpiryTime = token.getDate().getTime() + getTokenValiditySeconds() * DateUtils.MILLIS_PER_SECOND;
        return tokenExpiryTime < System.currentTimeMillis();
    }

    public PersistentRememberMeToken generatePersistentToken(String username, Vendor vendor) {
        String userKey = getUserKey(username, vendor);
        return new PersistentRememberMeToken(userKey, generateSeriesData(), generateTokenData(), new Date());
    }

    public String getUserKey(String username, Vendor vendor) {
        String vendorKey;
        if (vendor == null) {
            Vendor v = vendorService.getVendorByCurrentSite();
            vendorKey = v.getKey();
        } else {
            vendorKey = vendor.getKey();
        }

        return username + USER_KEY_DELIMITER + vendorKey;
    }

    public String getUsernameFromToken(PersistentRememberMeToken token) {
        return StringUtils.substringBefore(token.getUsername(), USER_KEY_DELIMITER);
    }

    public Vendor getVendorFromToken(PersistentRememberMeToken token) {
        String vendorKey = StringUtils.substringAfter(token.getUsername(), USER_KEY_DELIMITER);
        return vendorService.getVendorByKey(vendorKey);
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                                  Authentication successfulAuthentication) {
        String username = successfulAuthentication.getName();

        logger.debug("Creating new persistent login for user " + username);

        PersistentRememberMeToken persistentToken = generatePersistentToken(username, null);
        try {
            tokenRepository.createNewToken(persistentToken);
            addCookie(persistentToken, request, response);
        }
        catch (DataAccessException e) {
            logger.error("Failed to save persistent token ", e);
        }
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[]{token.getSeries(), token.getTokenValue()}, getTokenValiditySeconds(), request,
                response);
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(getCookieName(), cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setPath(getCookiePath(request));

        String serverName = SpringUtil.getRequest().getServerName();
        if (serverName.equals("localhost") || serverName.equals("127.0.0.1")) {
            cookie.setDomain("localhost");
        } else {
            Vendor vendor = vendorService.getVendorByCurrentSite();
            cookie.setDomain("." + vendor.getSite());
        }
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }

    @Override
    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Cancelling cookie");
        Cookie cookie = new Cookie(getCookieName(), null);
        cookie.setMaxAge(0);

        String serverName = SpringUtil.getRequest().getServerName();
        if (serverName.equals("localhost") || serverName.equals("127.0.0.1")) {
            cookie.setDomain("localhost");
        } else {
            Vendor vendor = vendorService.getVendorByCurrentSite();
            cookie.setDomain("." + vendor.getSite());
        }
        cookie.setPath(getCookiePath(request));
        response.addCookie(cookie);
    }
}
