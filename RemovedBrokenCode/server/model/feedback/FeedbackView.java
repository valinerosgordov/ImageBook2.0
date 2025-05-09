package ru.imagebook.server.model.feedback;

import ru.imagebook.shared.model.site.FeedbackUser;

/**
 * Created by zinchenko on 13.09.14.
 */
public class FeedbackView {

    private String name;
//    private String profession;
//    private String office;
    private String email;
    private String phone;
    private String message;
    private Integer internalUserId;
    private FeedbackUser feedbackUser;
//    private Integer feedbackUserId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getProfession() {
//        return profession;
//    }
//
//    public void setProfession(String profession) {
//        this.profession = profession;
//    }
//
//    public String getOffice() {
//        return office;
//    }
//
//    public void setOffice(String office) {
//        this.office = office;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getInternalUserId() {
        return internalUserId;
    }

    public void setInternalUserId(Integer internalUserId) {
        this.internalUserId = internalUserId;
    }

//    public Integer getFeedbackUserId() {
//        return feedbackUserId;
//    }
//
//    public void setFeedbackUserId(Integer feedbackUserId) {
//        this.feedbackUserId = feedbackUserId;
//    }


    public FeedbackUser getFeedbackUser() {
        return feedbackUser;
    }

    public void setFeedbackUser(FeedbackUser feedbackUser) {
        this.feedbackUser = feedbackUser;
    }
}
