package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.client.common.service.admin.AppServiceImpl;
import ru.imagebook.server.ctl.flash.ShowPreviewMessage;
import ru.imagebook.server.service.preview.PreviewService;
import ru.imagebook.server.service.site.PageNotFoundError;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.minogin.core.server.http.ServletUtil;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class PreviewServlet extends FlowServlet {
    private static final String PREVIEW_SERVICE = "previewService";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PreviewService previewService = getBean(PREVIEW_SERVICE);

        try {
            String number = request.getParameter("id");
            if (number == null) {
                int orderId = new Integer(request.getParameter("orderId"));
                String sessionId = request.getParameter("sessionId");
                if (sessionId != null) {
                    Cookie cookie = new Cookie("sessionId", sessionId);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                } else {
                    sessionId = ServletUtil.getCookie(request, AppServiceImpl.CODE + "_sid");
                }
                ShowPreviewMessage message = new ShowPreviewMessage(orderId, response.getWriter());
                SessionAspect.setSessionId(message, sessionId);
                send(message);
            } else {
                previewService.showOrderPreviewExt(number, response.getWriter());
            }
        } catch (PageNotFoundError ex) {
            ServiceLogger.log(ex);
            ex.printStackTrace();
            previewService.showPageNotFound(response.getWriter());
        }
    }
}
