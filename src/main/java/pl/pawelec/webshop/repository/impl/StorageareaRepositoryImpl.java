/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.Storagearea;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.StorageareaRepository;

import java.util.List;

@Repository
public class StorageareaRepositoryImpl extends AbstrRepository<Storagearea> implements StorageareaRepository {

    @Override
    public List<Storagearea> getByDescription(String wholeDescriptionOrPart) {
        return getEntityManager().createQuery("FROM Storagearea WHERE description LIKE :description").setParameter("description", "%" + wholeDescriptionOrPart + "%").getResultList();
    }

}
