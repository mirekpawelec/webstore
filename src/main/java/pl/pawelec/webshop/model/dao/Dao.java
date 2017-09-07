/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model.dao;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mirek
 */
public interface Dao<T extends Object> {
    void create(T entity);
    void update(T entity);
    void delete(T entity);
    void deleteById(Serializable Id);
    void deleteAll();
    T getOneById(Serializable Id);
    List<T> getAll();
    Long count();
    boolean exists(Serializable Id);
}