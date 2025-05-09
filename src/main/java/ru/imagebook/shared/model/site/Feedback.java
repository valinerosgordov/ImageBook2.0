package ru.imagebook.shared.model.site;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.*;
import ru.minogin.core.shared.model.BaseEntityImpl;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.String;
import java.util.Date;

/**
 * Created by zinchenko on 16.09.14.
 */
@Entity
@Table(name = "feedback")
public class Feedback extends BaseEntityImpl {

    public static final String CREATE_DATE = "createDate";

    private Date createDate;

    private Integer version;

    private FeedbackUser feedbackUser;

    private String message;

    @JsonManagedReference
    private FeedbackAnswer feedbackAnswer;

    @OneToOne(mappedBy = "feedback", cascade = CascadeType.REMOVE)
    public FeedbackAnswer getFeedbackAnswer() {
        return feedbackAnswer;
    }

    public void setFeedbackAnswer(FeedbackAnswer feedbackAnswer) {
        this.feedbackAnswer = feedbackAnswer;
    }

    @Type(type = "text")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @OneToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "feedback_user_id")
    public FeedbackUser getFeedbackUser() {
        return feedbackUser;
    }

    public void setFeedbackUser(FeedbackUser feedbackUser) {
        this.feedbackUser = feedbackUser;
    }
}
