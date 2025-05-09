package ru.imagebook.server.service.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gwtrpcspring.RemoteServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.app.service.LoginRemoteService;
import ru.imagebook.server.service2.security.SpringSecurityService;

/**

 * @since 09.12.2014
 */
@Service
public class LoginRemoteServiceImpl implements LoginRemoteService {
    @Autowired
    private SpringSecurityService springSecurityService;

    @Transactional
    @Override
    public void login(String username, String password) {
        HttpServletRequest request = RemoteServiceUtil.getThreadLocalRequest();
        HttpServletResponse response = RemoteServiceUtil.getThreadLocalResponse();
        springSecurityService.login(username, password, request, response);
    }
}
