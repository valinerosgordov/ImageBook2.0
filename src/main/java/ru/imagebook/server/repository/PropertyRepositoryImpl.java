package ru.imagebook.server.repository;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.imagebook.shared.model.Property;
import ru.minogin.core.server.hibernate.BaseRepository;

/**
 * Created by zinchenko on 15.09.14.
 */
@Repository
public class PropertyRepositoryImpl extends BaseRepository implements PropertyRepository {

    @Override
    public Property get(String key) {
        return (Property) createQuery("from Property where key = :key")
                .setString(Property.KEY, key)
                .uniqueResult();
    }

    @Override
    public String getValue(String key) {
        Property property = get(key);
        if(property == null) {
            return null;
        }
        return property.getValue();
    }

    @Override
    public void save(Property property) {
        getSession().merge(property);
    }

    @Override
    public void delete(Property property) {
        getSession().delete(property);
    }

    @Override
    public void delete(String propertyKey) {
        createQuery("delete from Property where key = :key")
                .setString(Property.KEY, propertyKey)
                .executeUpdate();
    }
}
