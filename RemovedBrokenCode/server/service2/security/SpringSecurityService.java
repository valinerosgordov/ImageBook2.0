package ru.imagebook.server.service2.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**

 * @since 09.12.2014
 */
public interface SpringSecurityService {
    void login(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
