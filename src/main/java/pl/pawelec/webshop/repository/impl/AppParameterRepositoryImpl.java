/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.AppParameter;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.AppParameterRepository;
import pl.pawelec.webshop.service.exception.NoParametersKeyFoundException;

import javax.persistence.NoResultException;

@Repository
public class AppParameterRepositoryImpl extends AbstrRepository<AppParameter> implements AppParameterRepository {

    public AppParameter getByUniqueKey(String symbol, String name) {
        try {
            return (AppParameter) getEntityManager()
                    .createQuery("from AppParameter WHERE symbol = :symbol AND name = :name")
                    .setParameter("symbol", symbol)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException nre) {
            throw new NoParametersKeyFoundException(symbol, name);
        }
    }

}
