package ru.imagebook.client.common.service.order;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.common.service.UserService;

@Singleton
public class FlashViewer {
    @Inject
    private UserService userService;

    public String getUrl(int orderId, String sessionId) {
        String flashUrl = userService.getUser().getVendor().getFlashUrl();
        return "http://" + flashUrl + "/?orderId=" + orderId + "&sessionId=" + sessionId;
    }
}