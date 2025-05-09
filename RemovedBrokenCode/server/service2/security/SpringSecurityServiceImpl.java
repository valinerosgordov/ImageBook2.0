package ru.imagebook.server.service2.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import ru.minogin.core.client.exception.Exceptions;

/**

 * @since 09.12.2014
 */
@Service
public class SpringSecurityServiceImpl implements SpringSecurityService {
    private static transient final Logger LOG = LoggerFactory.getLogger(SpringSecurityServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ImagebookTokenBasedRememberMeServices rememberMeServices;

    @Override
    public void login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        final boolean debug = LOG.isDebugEnabled();

        try {
            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);
            Authentication authResult = authenticationManager.authenticate(authRequest);

            if (debug) {
                LOG.debug("Authentication success: " + authResult);
            }

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authResult);

            HttpSession session = request.getSession();
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            //TODO securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

            rememberMeServices.loginSuccess(request, response, authResult);
        } catch (Exception ex) {
            Exceptions.rethrow(ex);
        }
    }
}
