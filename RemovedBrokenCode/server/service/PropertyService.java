package ru.imagebook.server.service;

import ru.imagebook.shared.model.Property;

/**
 * Created by zinchenko on 15.09.14.
 */
public interface PropertyService {

    String FEEDBACK_TEXT = "feedback.text";
    String BANNER_APP_TEXT = "banner.app.text";
    String BANNER_EDITOR_TEXT = "banner.editor.text";
    String BANNER_APP_PAYMENT_DELIVERY_TEXT = "banner.app.payment.delivery.text";

    Property get(String key);

    String getValue(String key);

    void save(Property property);

    void delete(Property property);

    void delete(String propertyKey);

}
