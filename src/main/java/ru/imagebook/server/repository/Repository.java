package ru.imagebook.server.repository;

import java.util.List;

/**
 * Created by zinchenko on 18.09.14.
 */
public interface Repository<T, I>  {

    List<T> findAll();

    T find(I id);

    void save(T entity);

    void update(T entity);

    T merge(T entity);

    void deleteById(I id);

    void delete(T entity);

    Long getSize();

}
