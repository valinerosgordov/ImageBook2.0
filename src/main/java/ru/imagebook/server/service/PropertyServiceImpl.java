package ru.imagebook.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.server.repository.PropertyRepository;
import ru.imagebook.shared.model.Property;

/**
 * Created by zinchenko on 15.09.14.
 */
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    @Transactional
    public Property get(String key) {
        return propertyRepository.get(key);
    }

    @Override
    @Transactional
    public String getValue(String key) {
        return propertyRepository.getValue(key);
    }

    @Override
    @Transactional
    public void save(Property property) {
        propertyRepository.save(property);
    }

    @Override
    @Transactional
    public void delete(Property property) {
        propertyRepository.delete(property);
    }

    @Override
    @Transactional
    public void delete(String propertyKey) {
        propertyRepository.delete(propertyKey);
    }

    public PropertyRepository getPropertyRepository() {
        return propertyRepository;
    }

    public void setPropertyRepository(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
}
