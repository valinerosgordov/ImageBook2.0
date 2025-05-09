package ru.imagebook.shared.model.site;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.*;
import ru.imagebook.shared.model.User;
import ru.minogin.core.shared.model.BaseEntityImpl;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zinchenko on 12.09.14.
 */
@Entity
@Table(name = "feedback_user")
public class FeedbackUser extends BaseEntityImpl {

    public static final String FEEDBACK_INTERNAL_USER = "feedbackInternalUser";

    private String profession;

    private String office;

    private Integer version;

    @JsonIgnore
    private FeedbackAnonymousUser feedbackAnonymousUser;

    @JsonIgnore
    private User feedbackInternalUser;

    @Transient
    public Integer getInternalUserId() {
        if(!isAnonymous()) {
            return feedbackInternalUser.getId();
        }
        return null;
    }

    @Transient
    public String getName() {
        if(isAnonymous()) {
            return feedbackAnonymousUser.getName();
        } else {
            return feedbackInternalUser.getName();
        }
    }

    @Transient
    public String getEmail() {
        if(isAnonymous()) {
            return feedbackAnonymousUser.getEmail();
        }

        if (feedbackInternalUser.getFirstEmail() != null) {
            return feedbackInternalUser.getFirstEmail().getEmail();
        } else {
            return null;
        }
    }

    @Transient
    public String getPhone() {
        if (isAnonymous()) {
            return feedbackAnonymousUser.getPhone();
        }

        if (feedbackInternalUser.getFirstPhone() != null) {
            return feedbackInternalUser.getFirstPhone().getPhone();
        } else {
            return null;
        }
    }

    @Transient
    private boolean isAnonymous() {
        return feedbackAnonymousUser != null;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @ManyToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "feedback_anonymous_user_id")
    public FeedbackAnonymousUser getFeedbackAnonymousUser() {
        return feedbackAnonymousUser;
    }

    public void setFeedbackAnonymousUser(FeedbackAnonymousUser feedbackAnonymousUser) {
        this.feedbackAnonymousUser = feedbackAnonymousUser;
    }

    @ManyToOne
    @JoinColumn(name = "feedback_internal_user_id")
    public User getFeedbackInternalUser() {
        return feedbackInternalUser;
    }

    public void setFeedbackInternalUser(User feedbackInternalUser) {
        this.feedbackInternalUser = feedbackInternalUser;
    }
}
