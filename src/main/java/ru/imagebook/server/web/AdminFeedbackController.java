package ru.imagebook.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zinchenko on 14.09.14.
 */
@Controller
@RequestMapping(value = "/feedback")
public class AdminFeedbackController extends FeedbackController {


    @RequestMapping("/test")
    public String test() {
        return "admin2/feedbackTest";
    }

    @Override
    protected ClientConfig createClientConfig() {
        AdminClientConfig adminClientConfig = new AdminClientConfig();
        adminClientConfig.setTotalItems(feedbackService.getSize());
        return adminClientConfig;
    }

    protected static class AdminClientConfig extends ClientConfig {

        private Long totalItems;

        public Long getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(Long totalItems) {
            this.totalItems = totalItems;
        }
    }

}
