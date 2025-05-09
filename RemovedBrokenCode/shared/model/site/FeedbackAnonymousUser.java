package ru.imagebook.shared.model.site;

import ru.minogin.core.client.bean.BaseEntityBean;
import ru.minogin.core.shared.model.BaseEntityImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zinchenko on 12.09.14.
 */
@Entity
@Table(name = "feedback_anonymous_user")
public class FeedbackAnonymousUser extends BaseEntityImpl {

    private String name;

    private String phone;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
