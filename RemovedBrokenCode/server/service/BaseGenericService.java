package ru.imagebook.server.service;

import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.server.repository.BaseGenericRepository;
import ru.imagebook.server.repository.Repository;

import java.util.List;

/**
 * Created by zinchenko on 18.09.14.
 */
public abstract class BaseGenericService<T, I> implements Service<T, I> {

    private Repository<T, I> repository;

    public BaseGenericService(Repository<T, I> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public T find(I id) {
        return repository.find(id);
    }

    @Override
    @Transactional
    public void save(T entity) {
        repository.save(entity);
    }

    @Override
    @Transactional
    public void update(T entity) throws NoEntityPermissionException {
        checkOwner(entity);
        repository.update(entity);
    }

    @Override
    public T merge(T entity) throws NoEntityPermissionException {
        checkOwner(entity);
        return repository.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(I id) throws NoEntityPermissionException {
        checkOwnerById(id);
        repository.deleteById(id);
    }

    @Override
    public void delete(T entity) throws NoEntityPermissionException {
        checkOwner(entity);
        repository.delete(entity);
    }

    @Override
    public Long getSize() {
        return repository.getSize();
    }

    protected void checkOwner(T entity) throws NoEntityPermissionException {}

    protected void checkOwnerById(I id) throws NoEntityPermissionException  {}

}
