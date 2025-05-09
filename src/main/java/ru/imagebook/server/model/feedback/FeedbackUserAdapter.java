package ru.imagebook.server.model.feedback;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.site.FeedbackUser;

/**
 * Created by zinchenko on 06.10.14.
 */
public class FeedbackUserAdapter {

    @JsonIgnore
    private User user;

    public FeedbackUserAdapter(User user, FeedbackUser feedbackUser) {
        this.user = user;
    }

    public String getEmail() {
        if (user.getFirstEmail() != null) {
            return user.getFirstEmail().getEmail();
        }
        return null;
    }

    public String getName() {
        return user.getName();
    }

    public String getPhone() {
        if (user.getFirstPhone() != null) {
            return user.getFirstPhone().getPhone();
        }
        return null;
    }

    public Integer getId(){
        return user.getId();
    }


}
