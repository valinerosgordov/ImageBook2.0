package ru.imagebook.server.repository;

import ru.imagebook.shared.model.Property;

/**
 * Created by zinchenko on 15.09.14.
 */
public interface PropertyRepository {

    Property get(String key);

    String getValue(String key);

    void save(Property property);

    void delete(Property property);

    void delete(String propertyKey);

}
