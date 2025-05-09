package ru.minogin.core.client.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntityBean extends BaseBean implements EntityBean {
    private static final long serialVersionUID = 6266619692328940483L;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return get(ID);
    }

    @Override
    public void setId(Integer id) {
        set(ID, id);
    }
}
