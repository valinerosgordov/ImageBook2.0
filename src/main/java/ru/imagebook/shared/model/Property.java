package ru.imagebook.shared.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Created by zinchenko on 15.09.14.
 */
@Entity
@Table(name = "property")
public class Property {

    public static final String KEY = "key";
    public static final String VALUE = "value";

    private String key;

    private String value;

    @Id
    @Column(name = "property_key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "value")
    @Type(type="text")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
