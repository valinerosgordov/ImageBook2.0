package ru.saasengine.server.repository.beanstore;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.saasengine.server.model.BeanStoreItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class BeanStoreRepositoryImpl implements BeanStoreRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String getJSON(String id) {
        Session session = entityManager.unwrap(Session.class);
        BeanStoreItem item = (BeanStoreItem) session.get(BeanStoreItem.class, id);

        return item != null ? item.getJson() : null;
    }

    @Override
    public void saveJSON(String id, String json) {
        Session session = entityManager.unwrap(Session.class);

        BeanStoreItem item = new BeanStoreItem(id, json);
        session.saveOrUpdate(item);
    }
}
