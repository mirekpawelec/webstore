package pl.pawelec.webshop.service;

import java.util.List;

public interface CrudService<T extends Object> {
    void create(T entity);
    void update(T entity);
    void delete(T entity);
    void deleteById(Long id);
    void deleteAll();
    T getOneById(Long id);
    List<T> getAll();
    Long count();
    boolean exists(Long id);
}
