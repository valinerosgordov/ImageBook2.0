package ru.imagebook.server.service;

/**
 * Created by zinchenko on 13.09.14.
 */
public class FeedbackConfig {

    private Integer pageSize;

    private String feedbackAnswerUserName;

    private String facebookHref;

    private String vkApiId;

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
