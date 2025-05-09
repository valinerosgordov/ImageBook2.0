package ru.imagebook.server.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.imagebook.server.service.NoEntityPermissionException;
import ru.imagebook.server.service.Service;

import java.util.List;

/**
 * Created by zinchenko on 17.09.14.
 */
public abstract class BaseGenericRestController<T, I> {

    private Service<T, I> service;

    protected BaseGenericRestController(Service<T, I> service) {
        this.service = service;
    }

    @RequestMapping()
    public @ResponseBody
    List<T> getAll() {
        return service.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    void get(@PathVariable I id) {
        service.find(id);
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    T save(@RequestBody T entity) {
        service.save(entity);
        return entity;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody
    T update(@RequestBody T entity) throws NoEntityPermissionException {
        service.update(entity);
        return entity;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    void deleteById(@PathVariable I id) throws NoEntityPermissionException  {
        service.deleteById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    void delete(@RequestBody T entity) throws NoEntityPermissionException  {
        service.delete(entity);
    }

}
