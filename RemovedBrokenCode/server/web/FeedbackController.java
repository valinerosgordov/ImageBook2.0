package ru.imagebook.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.imagebook.server.model.web.Breadcrumb;
import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.server.service.FeedbackConfig;
import ru.imagebook.server.service.FeedbackService;
import ru.imagebook.server.service.PropertyService;
import ru.imagebook.server.service.RecommendationService;
import ru.imagebook.shared.model.site.Feedback;
import ru.imagebook.shared.model.site.Recommendation;
import ru.minogin.core.client.i18n.locale.Locales;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by zinchenko on 07.09.14.
 */
abstract public class FeedbackController extends MainController {

    @Autowired
    protected FeedbackConfig feedbackConfig;

    @Autowired
    protected MessageSource messages;

    @Autowired
    protected FeedbackService feedbackService;

    @Autowired
    protected PropertyService propertyService;

    @Autowired
    protected RecommendationService recommendationService;

    @Override
    protected void layout(Model model, PageModel page) {
        super.layout(model, page);
        page.setWide(true);

        model.addAttribute("fullWidth", true);

        List<Breadcrumb> breadcrumbs = page.getBreadcrumbs();
        breadcrumbs.add(new Breadcrumb("Главная", "/"));
        breadcrumbs.add(new Breadcrumb("Отзывы"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String feedback(Model model) {

        PageModel pageModel = new PageModel();
        pageModel.setH1(messages.getMessage("feedback", null,  new Locale(Locales.RU)));
        layout(model, pageModel);

        return "feedback";
    }

    @ResponseBody
    @RequestMapping("/initApp")
    public InitAppBean initApp() {
        InitAppBean initAppBean = createInitAppBean();
        initAppBean.setFeedbacks(feedbackService.getFirstPage());
        initAppBean.setFeedbackText(propertyService.getValue(PropertyService.FEEDBACK_TEXT));
        initAppBean.setClientConfig(completeClientConfig());
        initAppBean.setRecommendations(recommendationService.findAll());
        return initAppBean;
    }

    protected InitAppBean createInitAppBean() {
        return new InitAppBean();
    }

    private ClientConfig completeClientConfig() {
        ClientConfig clientConfig = createClientConfig();
        clientConfig.setPageSize(feedbackConfig.getPageSize());
        clientConfig.setFeedbackAnswerUserName(feedbackConfig.getFeedbackAnswerUserName());
        return clientConfig;
    }

    protected ClientConfig createClientConfig() {
        return new ClientConfig();
    }

    protected static class InitAppBean {

        private List<Feedback> feedbacks;

        private String feedbackText;

        private ClientConfig clientConfig;

        private List<Recommendation> recommendations;

        public List<Feedback> getFeedbacks() {
            return feedbacks;
        }

        public void setFeedbacks(List<Feedback> feedbacks) {
            this.feedbacks = feedbacks;
        }

        public String getFeedbackText() {
            return feedbackText;
        }

        public void setFeedbackText(String feedbackText) {
            this.feedbackText = feedbackText;
        }

        public ClientConfig getClientConfig() {
            return clientConfig;
        }

        public void setClientConfig(ClientConfig clientConfig) {
            this.clientConfig = clientConfig;
        }

        public List<Recommendation> getRecommendations() {
            return recommendations;
        }

        public void setRecommendations(List<Recommendation> recommendations) {
            this.recommendations = recommendations;
        }
    }

    protected static class ClientConfig {

        private Integer pageSize;

        private String feedbackAnswerUserName;

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public String getFeedbackAnswerUserName() {
            return feedbackAnswerUserName;
        }

        public void setFeedbackAnswerUserName(String feedbackAnswerUserName) {
            this.feedbackAnswerUserName = feedbackAnswerUserName;
        }
    }

    public FeedbackConfig getFeedbackConfig() {
        return feedbackConfig;
    }

    public void setFeedbackConfig(FeedbackConfig feedbackConfig) {
        this.feedbackConfig = feedbackConfig;
    }

    public MessageSource getMessages() {
        return messages;
    }

    public void setMessages(MessageSource messages) {
        this.messages = messages;
    }

    public FeedbackService getFeedbackService() {
        return feedbackService;
    }

    public void setFeedbackService(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    public PropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public RecommendationService getRecommendationService() {
        return recommendationService;
    }

    public void setRecommendationService(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }
}
