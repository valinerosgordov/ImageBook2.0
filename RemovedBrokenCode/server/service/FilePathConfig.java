package ru.imagebook.server.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zinchenko on 27.09.14.
 */
public class FilePathConfig {

    public static final String RECOMMENDATION_ENTITY = "recommendation";
    public static final String PRODUCT_PHOTO_ENTITY = "product";
    public static final String PRODUCT_TYPE_PHOTO_ENTITY = "productType";

    private Map<String, String> entities = new HashMap<>();

    public String getPath(String entityName) {
        return entities.get(entityName);
    }

    public Map<String, String> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, String> entities) {
        this.entities = entities;
    }
}
