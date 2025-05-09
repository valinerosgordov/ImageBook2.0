package ru.imagebook.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.imagebook.server.model.feedback.FeedbackUserAdapter;
import ru.imagebook.server.service.FeedbackConfig;
import ru.imagebook.server.service.FeedbackUserService;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.auth.XSecurityService;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.site.FeedbackUser;
import ru.imagebook.shared.model.site.Recommendation;

import java.util.List;

/**
 * Created by zinchenko on 14.09.14.
 */
@Controller
@RequestMapping(value = "/feedback")
public class SiteFeedbackController extends FeedbackController {

    @Autowired
    protected XSecurityService securityService;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected UserService userService;

    @Autowired
    private FeedbackUserService feedbackUserService;

    @Override
    protected InitAppBean createInitAppBean() {
        SiteInitAppBean siteInitAppBean = new SiteInitAppBean();
        siteInitAppBean.setRecommendations(recommendationService.findAll());

        siteInitAppBean.setFeedbackUser(createFeedbackUser());
        return siteInitAppBean;
    }

    private FeedbackUser createFeedbackUser() {
        int currentUserId = authService.getCurrentUserId();
        if(currentUserId == 0) {
            return null;
        }
        FeedbackUser feedbackUser = feedbackUserService.findFeedbackUser(currentUserId);
        if (feedbackUser != null) {
            return feedbackUser;
        }
        feedbackUser = new FeedbackUser();
        feedbackUser.setFeedbackInternalUser(userService.getUser(currentUserId));
        return feedbackUser;
    }

    @Override
    protected ClientConfig createClientConfig() {
        SiteClientConfig siteClientConfig = new SiteClientConfig();
        siteClientConfig.setFacebookHref(feedbackConfig.getFacebookHref());
        siteClientConfig.setVkApiId(feedbackConfig.getVkApiId());
        return siteClientConfig;
    }

    protected static class SiteInitAppBean extends InitAppBean{

        private List<Recommendation> recommendations;

        private FeedbackUser feedbackUser;

        public FeedbackUser getFeedbackUser() {
            return feedbackUser;
        }

        public void setFeedbackUser(FeedbackUser feedbackUser) {
            this.feedbackUser = feedbackUser;
        }

        public List<Recommendation> getRecommendations() {
            return recommendations;
        }

        public void setRecommendations(List<Recommendation> recommendations) {
            this.recommendations = recommendations;
        }

    }

    protected static class SiteClientConfig extends ClientConfig {
        private String facebookHref;
        private String vkApiId;

        public String getFacebookHref() {
            return facebookHref;
        }

        public void setFacebookHref(String facebookHref) {
            this.facebookHref = facebookHref;
        }

        public String getVkApiId() {
            return vkApiId;
        }

        public void setVkApiId(String vkApiId) {
            this.vkApiId = vkApiId;
        }

    }

    public XSecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(XSecurityService securityService) {
        this.securityService = securityService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public FeedbackUserService getFeedbackUserService() {
        return feedbackUserService;
    }

    public void setFeedbackUserService(FeedbackUserService feedbackUserService) {
        this.feedbackUserService = feedbackUserService;
    }
}
