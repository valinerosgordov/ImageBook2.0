package ru.imagebook.server.service;

import java.util.List;

/**
 * Created by zinchenko on 18.09.14.
 */
public interface Service<T, I> {

    List<T> findAll();

    T find(I id);

    void save(T entity);

    void update(T entity) throws NoEntityPermissionException ;

    T merge(T entity) throws NoEntityPermissionException ;

    void deleteById(I id) throws NoEntityPermissionException ;

    void delete(T entity) throws NoEntityPermissionException ;

    Long getSize();

}
